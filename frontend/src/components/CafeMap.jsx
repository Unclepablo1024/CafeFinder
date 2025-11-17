import React, { useEffect, useRef, useState } from 'react'
import { MapPinIcon } from '@heroicons/react/24/outline'
import { Loader } from '@googlemaps/js-api-loader'

const CafeMap = ({
  cafes = [],
  center = { lat: 33.7490, lng: -84.3880 },
  zoom = 12,
  onCafeSelect,
  className = "w-full h-96 rounded-lg"
}) => {
  const mapRef = useRef(null)
  const googleMapRef = useRef(null)
  const markersRef = useRef([]) // Keep for backward compatibility
  const markersMapRef = useRef(new Map()) // Track markers by cafe ID
  const infoWindowRef = useRef(null)
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState(null)
  const apiKey = import.meta.env.VITE_GOOGLE_MAPS_API_KEY

  // Helper function to create fallback coffee icon
  const createFallbackIcon = () => {
    const encodeSvg = (svgString) => {
      return btoa(unescape(encodeURIComponent(svgString)))
    }

    const svgIcon = `
      <svg width="40" height="50" viewBox="0 0 40 50" fill="none" xmlns="http://www.w3.org/2000/svg">
        <ellipse cx="20" cy="47" rx="8" ry="3" fill="rgba(0,0,0,0.2)"/>
        <path d="M20 0C30.5 0 39 8.5 39 19C39 29.5 20 50 20 50S1 29.5 1 19C1 8.5 9.5 0 20 0Z" fill="#8B4513" stroke="#FFFFFF" stroke-width="2"/>
        <circle cx="20" cy="19" r="10" fill="#FFFFFF"/>
        <text x="20" y="25" text-anchor="middle" font-size="12" fill="#8B4513">☕</text>
        <circle cx="30" cy="10" r="6" fill="#FFD700" stroke="#FFFFFF" stroke-width="1"/>
        <text x="30" y="13" text-anchor="middle" font-size="8" fill="#8B4513" font-weight="bold">★</text>
      </svg>
    `

    return {
      url: 'data:image/svg+xml;base64,' + encodeSvg(svgIcon),
      scaledSize: new google.maps.Size(40, 50),
      anchor: new google.maps.Point(20, 50)
    }
  }

  // Helper function to get cafe photo from Google Places (with timeout)
  const getCafePhoto = (cafe, callback) => {
    if (!window.placesService) {
      callback(null)
      return
    }

    const timeoutId = setTimeout(() => {
      callback(null)
    }, 3000)

    const request = {
      query: `${cafe.name} ${cafe.address} ${cafe.city}`,
      fields: ['place_id', 'photos', 'name'],
      locationBias: {
        center: { lat: cafe.latitude, lng: cafe.longitude },
        radius: 100
      }
    }

    window.placesService.textSearch(request, (results, status) => {
      clearTimeout(timeoutId)

      if (status === google.maps.places.PlacesServiceStatus.OK && results && results[0]) {
        const place = results[0]
        if (place.photos && place.photos.length > 0) {
          try {
            const photoUrl = place.photos[0].getUrl({
              maxWidth: 100,
              maxHeight: 100
            })
            callback(photoUrl)
          } catch (error) {
            console.log('Error getting photo URL:', error)
            callback(null)
          }
        } else {
          callback(null)
        }
      } else {
        console.log('Places API error:', status)
        callback(null)
      }
    })
  }

  // Helper function to create marker for a cafe
  const createMarker = (cafe) => {
    try {
      const marker = new google.maps.Marker({
        position: { lat: cafe.latitude, lng: cafe.longitude },
        map: googleMapRef.current,
        title: cafe.name,
        icon: createFallbackIcon(),
        animation: google.maps.Animation.DROP
      })

      const infoContent = `
        <div style="max-width: 300px; padding: 12px; font-family: system-ui, -apple-system, sans-serif;">
          <div style="border-bottom: 2px solid #8B4513; padding-bottom: 8px; margin-bottom: 12px;">
            <h3 style="margin: 0 0 4px 0; color: #8B4513; font-size: 18px; font-weight: 600;">${cafe.name}</h3>
            <div style="display: flex; align-items: center; margin-bottom: 4px;">
              ${[...Array(5)].map((_, i) =>
                `<span style="color: ${i < Math.floor(cafe.avgRating || 4)} ? '#FFD700' : '#E5E7EB'; font-size: 14px;">★</span>`
              ).join('')}
              <span style="margin-left: 6px; color: #6B7280; font-size: 14px;">
                ${(cafe.avgRating || 4).toFixed(1)} (${cafe.reviewsCount || 0} reviews)
              </span>
            </div>
          </div>

          <div style="margin-bottom: 8px;">
            <p style="margin: 0 0 8px 0; color: #374151; font-size: 14px; line-height: 1.4;">
              ${cafe.description || 'Great coffee shop in Atlanta'}
            </p>
          </div>

          <div style="margin-bottom: 12px;">
            <div style="display: flex; align-items: center; margin-bottom: 4px; color: #6B7280; font-size: 13px;">
              <span style="margin-right: 6px;">📍</span>
              ${cafe.address}, ${cafe.city}
            </div>

            ${cafe.phone ? `
              <div style="display: flex; align-items: center; margin-bottom: 4px; color: #6B7280; font-size: 13px;">
                <span style="margin-right: 6px;">📞</span>
                ${cafe.phone}
              </div>
            ` : ''}

            <div style="display: flex; align-items: center; color: #6B7280; font-size: 13px;">
              <span style="margin-right: 6px;">${cafe.priceRange || '$$'}</span>
              ${cafe.wifi ? '<span style="margin-right: 8px;">📶 WiFi</span>' : ''}
              ${cafe.workFriendly ? '<span style="margin-right: 8px;">💻 Work-friendly</span>' : ''}
            </div>
          </div>

          <div style="display: flex; gap: 8px;">
            <button
              onclick="window.cafeMapClickHandler('${cafe.id}')"
              style="
                background: #8B4513;
                color: white;
                border: none;
                padding: 6px 12px;
                border-radius: 4px;
                font-size: 12px;
                cursor: pointer;
                font-weight: 500;
              "
            >
              View Details
            </button>
            ${cafe.currentStatus ? `
              <span style="
                background: ${cafe.currentStatus === 'open' ? '#10B981' : cafe.currentStatus === 'busy' ? '#F59E0B' : '#EF4444'};
                color: white;
                padding: 4px 8px;
                border-radius: 12px;
                font-size: 11px;
                text-transform: uppercase;
                font-weight: 500;
              ">
                ${cafe.currentStatus}
              </span>
            ` : ''}
          </div>
        </div>
      `

      marker.addListener('click', () => {
        infoWindowRef.current.setContent(infoContent)
        infoWindowRef.current.open(googleMapRef.current, marker)
        onCafeSelect?.(cafe)
      })

      marker.addListener('mouseover', () => {
        marker.setAnimation(google.maps.Animation.BOUNCE)
        setTimeout(() => marker.setAnimation(null), 750)
      })

      // Try to get photo and update marker icon
      getCafePhoto(cafe, (photoUrl) => {
        if (photoUrl) {
          const size = 50
          const svgIcon = `
            <svg width="${size}" height="${size + 10}" viewBox="0 0 ${size} ${size + 10}" xmlns="http://www.w3.org/2000/svg">
              <defs>
                <pattern id="photo-${cafe.id}" patternUnits="objectBoundingBox" width="1" height="1">
                  <image href="${photoUrl}" width="${size - 8}" height="${size - 8}" x="4" y="4" preserveAspectRatio="xMidYMid slice"/>
                </pattern>
                <filter id="shadow-${cafe.id}">
                  <feDropShadow dx="0" dy="2" stdDeviation="2" flood-opacity="0.3"/>
                </filter>
              </defs>
              <ellipse cx="${size/2}" cy="${size/2 + 3}" rx="${size/2 - 2}" ry="3" fill="rgba(0,0,0,0.2)"/>
              <circle cx="${size/2}" cy="${size/2}" r="${size/2 - 4}" fill="url(#photo-${cafe.id})" stroke="#8B4513" stroke-width="3" filter="url(#shadow-${cafe.id})"/>
              <circle cx="${size - 8}" cy="8" r="6" fill="#FFD700" stroke="#FFFFFF" stroke-width="1"/>
              <text x="${size - 8}" y="11" text-anchor="middle" font-size="7" fill="#8B4513" font-weight="bold">★</text>
            </svg>
          `

          try {
            const encodeSvg = (svgString) => btoa(unescape(encodeURIComponent(svgString)))
            marker.setIcon({
              url: 'data:image/svg+xml;base64,' + encodeSvg(svgIcon),
              scaledSize: new google.maps.Size(size, size + 10),
              anchor: new google.maps.Point(size/2, size + 10)
            })
            console.log(`📸 Updated ${cafe.name} with photo`)
          } catch (error) {
            console.log('Failed to set photo icon:', error)
          }
        }
      })

      return marker
    } catch (error) {
      console.error(`❌ Failed to create marker for ${cafe.name}:`, error)
      return null
    }
  }

  // If no API key, show a placeholder
  if (!apiKey || apiKey === 'demo-key') {
    return (
      <div className={`${className} bg-gradient-to-br from-coffee-50 to-cream-100 border-2 border-dashed border-coffee-200 flex flex-col items-center justify-center`}>
        <MapPinIcon className="h-16 w-16 text-coffee-400 mb-4" />
        <h3 className="text-lg font-semibold text-coffee-600 mb-2">Interactive Map</h3>
        <p className="text-coffee-500 text-center max-w-sm">
          Add your Google Maps API key to enable interactive maps with cafe locations
        </p>
        <div className="mt-4 text-sm text-coffee-400">
          {cafes.length} cafe{cafes.length !== 1 ? 's' : ''} in this area
        </div>

        {/* Show cafe markers as a list */}
        {cafes.length > 0 && (
          <div className="mt-4 max-h-32 overflow-y-auto w-full px-4">
            <div className="space-y-2">
              {cafes.slice(0, 5).map((cafe, index) => (
                <button
                  key={cafe.id || index}
                  onClick={() => onCafeSelect?.(cafe)}
                  className="flex items-center space-x-2 w-full text-left p-2 rounded bg-white/50 hover:bg-white/80 transition-colors"
                >
                  <span className="text-coffee-600">📍</span>
                  <div>
                    <div className="font-medium text-coffee-700">{cafe.name}</div>
                    <div className="text-xs text-coffee-500">{cafe.address}</div>
                  </div>
                </button>
              ))}
              {cafes.length > 5 && (
                <div className="text-xs text-coffee-400 text-center py-1">
                  +{cafes.length - 5} more cafes
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    )
  }

  // Set up global click handler for info window buttons
  useEffect(() => {
    window.cafeMapClickHandler = (cafeId) => {
      const selectedCafe = cafes.find(cafe => cafe.id === cafeId)
      if (selectedCafe) {
        onCafeSelect?.(selectedCafe)
      }
    }

    return () => {
      delete window.cafeMapClickHandler
    }
  }, [cafes, onCafeSelect])

  // Load Google Maps
  useEffect(() => {
    const initMap = async () => {
      try {
        const loader = new Loader({
          apiKey: apiKey,
          version: "weekly",
          libraries: ["maps", "places"]
        })

      await loader.load()

      if (mapRef.current) {
        const map = new google.maps.Map(mapRef.current, {
          center: center,
          zoom: zoom,
          styles: [
            {
              featureType: "poi.business",
              elementType: "labels",
              stylers: [{ visibility: "off" }]
            },
            {
              featureType: "poi.attraction",
              elementType: "labels",
              stylers: [{ visibility: "off" }]
            }
          ]
        })

        googleMapRef.current = map
        window.placesService = new google.maps.places.PlacesService(map)
        infoWindowRef.current = new google.maps.InfoWindow()
        setIsLoading(false)
      }
      } catch (err) {
        console.error('Error loading Google Maps:', err)
        setError('Failed to load map')
        setIsLoading(false)
      }
    }

    initMap()
  }, [apiKey, center.lat, center.lng, zoom])

  // Add markers for cafes with retry mechanism
  useEffect(() => {
    const attemptCreateMarkers = () => {
      console.log('=== MARKERS DEBUG ===')
      console.log('Map ready:', !!googleMapRef.current)
      console.log('Cafes count:', cafes.length)
      console.log('Cafes data:', cafes.slice(0, 3).map(c => ({
        name: c.name,
        lat: c.latitude,
        lng: c.longitude,
        hasLatLng: !!(c.latitude && c.longitude)
      })))
      console.log('=====================')

      if (!googleMapRef.current) {
        console.log('⏳ Waiting for Google Maps to load... will retry in 1s')
        setTimeout(attemptCreateMarkers, 1000)
        return
      }

      if (cafes.length === 0) {
        console.log('⏳ Waiting for cafes data...')
        return
      }

      console.log('🚀 Both map and cafes ready - updating markers!')
      updateMarkers()
    }

    const updateMarkers = () => {
      if (!googleMapRef.current) return

      const currentCafeIds = new Set(cafes.map(cafe => cafe.id))
      const existingCafeIds = new Set(markersMapRef.current.keys())

      // Remove markers for cafes that no longer exist
      for (const cafeId of existingCafeIds) {
        if (!currentCafeIds.has(cafeId)) {
          const marker = markersMapRef.current.get(cafeId)
          if (marker) {
            marker.setMap(null)
            markersMapRef.current.delete(cafeId)
            console.log(`🗑️ Removed marker for cafe ID: ${cafeId}`)
          }
        }
      }

      // Add new markers for cafes that don't have them yet
      cafes.forEach(cafe => {
        if (!cafe.id || !cafe.latitude || !cafe.longitude) return

        if (!markersMapRef.current.has(cafe.id)) {
          console.log(`➕ Adding new marker for ${cafe.name}`)
          const marker = createMarker(cafe)
          if (marker) {
            markersMapRef.current.set(cafe.id, marker)
          }
        }
      })

      console.log(`📊 Marker stats: ${markersMapRef.current.size} total markers`)

      // Fit bounds to show all markers
      if (markersMapRef.current.size > 1) {
        const bounds = new google.maps.LatLngBounds()
        markersMapRef.current.forEach(marker => {
          bounds.extend(marker.getPosition())
        })
        googleMapRef.current.fitBounds(bounds)
      }
    }

    const createAllMarkers = () => {
      console.log('Deprecated: using updateMarkers instead')
      updateMarkers()
    }

    attemptCreateMarkers()
  }, [cafes, onCafeSelect])

  if (error) {
    return (
      <div className={`${className} bg-red-50 border border-red-200 flex flex-col items-center justify-center`}>
        <MapPinIcon className="h-16 w-16 text-red-400 mb-4" />
        <h3 className="text-lg font-semibold text-red-600 mb-2">Map Error</h3>
        <p className="text-red-500 text-center">{error}</p>
      </div>
    )
  }

  return (
    <div className={`${className} relative overflow-hidden rounded-lg border border-coffee-200`}>
      {isLoading && (
        <div className="absolute inset-0 bg-coffee-50 flex items-center justify-center z-10">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-coffee-600"></div>
          <span className="ml-2 text-coffee-600">Loading map...</span>
        </div>
      )}
      <div ref={mapRef} className="w-full h-full" />
    </div>
  )
}

export default CafeMap
