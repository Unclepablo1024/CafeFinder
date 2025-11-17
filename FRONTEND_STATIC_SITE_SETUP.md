# Frontend Static Site Setup - Step by Step

## Render Dashboard Settings

### Step 1: Create New Static Site
1. Go to **Render Dashboard**: https://dashboard.render.com
2. Click **"New +"** button (top right)
3. Select **"Static Site"**

### Step 2: Connect Repository
1. If not connected, click **"Connect account"** → Connect GitHub
2. Select your repository: `Unclepablo1024/CafeFinder`
3. Click **"Connect"**

### Step 3: Configure Settings

Fill in these **exact** settings:

#### Basic Settings:
- **Name**: `cafe-finder-frontend` (or any name you prefer)
- **Region**: Choose closest to you (e.g., `Oregon (US West)`)
- **Branch**: `main` (or `master` - whatever your default branch is)
- **Root Directory**: 
  - ⚠️ **IMPORTANT**: 
    - If your repo root is `CafeFinderFinal` → Set to: `CafeFinder`
    - If your repo root is `CafeFinder` → Leave **EMPTY**
  
#### Build Settings:
- **Build Command**: `cd frontend && npm install && npm run build`
- **Publish Directory**: `frontend/dist`

#### Plan:
- **Free** (or choose Starter if you want always-on)

### Step 4: Set Environment Variables

Click **"Add Environment Variable"** for each:

1. **VITE_API_URL**
   - Value: `https://cafefinder-tr3j.onrender.com`
   - ⚠️ No trailing slash
   - ⚠️ No `/api` suffix

2. **VITE_GOOGLE_PLACES_API_KEY**
   - Value: Your Google Places API key

3. **VITE_GOOGLE_MAPS_API_KEY**
   - Value: Your Google Maps API key

### Step 5: Create and Deploy
1. Click **"Create Static Site"**
2. Wait for build to complete (2-5 minutes)
3. Copy your frontend URL (will be something like `https://cafe-finder-frontend.onrender.com`)

### Step 6: Update Backend CORS

After frontend deploys:

1. Go to **Backend service** (`cafefinder-tr3j`)
2. Go to **"Environment"** tab
3. Add/Edit `CORS_ALLOWED_ORIGINS`:
   - Value: Your frontend URL (e.g., `https://cafe-finder-frontend.onrender.com`)
   - ⚠️ No trailing slash
4. Click **"Save Changes"**
5. Go to **"Manual Deploy"** → **"Clear build cache & deploy"**

## Visual Guide

```
┌─────────────────────────────────────────┐
│ Connect Repository                      │
│ [Select: Unclepablo1024/CafeFinder]    │
├─────────────────────────────────────────┤
│ Name: cafe-finder-frontend              │
│ Region: [Oregon (US West)]             │
│ Branch: main                            │
│ Root Directory: CafeFinder (or empty)  │
├─────────────────────────────────────────┤
│ Build Command:                          │
│   cd frontend && npm install && npm... │
│ Publish Directory: frontend/dist        │
├─────────────────────────────────────────┤
│ Plan: [Free]                           │
├─────────────────────────────────────────┤
│ Environment Variables:                  │
│   VITE_API_URL =                        │
│     https://cafefinder-tr3j.onrender... │
│   VITE_GOOGLE_PLACES_API_KEY = [key]   │
│   VITE_GOOGLE_MAPS_API_KEY = [key]     │
└─────────────────────────────────────────┘
```

## Important Notes

### Root Directory
- **If your GitHub repo structure is:**
  ```
  CafeFinderFinal/
    └── CafeFinder/
        ├── backend/
        ├── frontend/
        └── render.yaml
  ```
  Then set **Root Directory** to: `CafeFinder`

- **If your GitHub repo structure is:**
  ```
  CafeFinder/
    ├── backend/
    ├── frontend/
    └── render.yaml
  ```
  Then leave **Root Directory** **EMPTY**

### Environment Variables Checklist
- [ ] `VITE_API_URL` = `https://cafefinder-tr3j.onrender.com` (your backend URL)
- [ ] `VITE_GOOGLE_PLACES_API_KEY` = Your Google Places API key
- [ ] `VITE_GOOGLE_MAPS_API_KEY` = Your Google Maps API key

### After Deployment
1. ✅ Frontend builds successfully
2. ✅ Copy frontend URL
3. ✅ Set `CORS_ALLOWED_ORIGINS` in backend = frontend URL
4. ✅ Redeploy backend
5. ✅ Visit frontend URL - should work!

## Troubleshooting

### Build Fails
- Check Root Directory is correct
- Verify `package.json` exists in `frontend/` directory
- Check build logs for specific errors

### Frontend Can't Connect to Backend
- Verify `VITE_API_URL` is set correctly
- Check `CORS_ALLOWED_ORIGINS` in backend matches frontend URL exactly
- Check browser console for CORS errors

### Blank Page
- Check browser console (F12) for errors
- Verify all environment variables are set
- Check that build completed successfully

