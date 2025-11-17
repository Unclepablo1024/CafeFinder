# Fix 404 Error - Backend Routes Guide

## The Problem

You're seeing a 404 error because you're accessing a route that doesn't exist. All API endpoints start with `/api/`.

## Available API Endpoints

### Health Check
- **GET** `/api/health` - Check if backend is running
  - Test: `https://your-backend.onrender.com/api/health`
  - Should return: `{"status":"ok"}`

### Authentication
- **POST** `/api/auth/login` - User login
- **POST** `/api/auth/register` - User registration

### Cafes
- **GET** `/api/cafes/public/search` - Search cafes
- **GET** `/api/cafes/public/nearby` - Find nearby cafes
- **GET** `/api/cafes/public/popular` - Get popular cafes
- **GET** `/api/cafes/public/{id}` - Get cafe by ID
- **POST** `/api/cafes` - Create cafe (requires auth)
- **PUT** `/api/cafes/{id}` - Update cafe (requires auth)
- **DELETE** `/api/cafes/{id}` - Delete cafe (requires auth)
- **GET** `/api/cafes/{id}/menu` - Get cafe menu

### Reviews
- **GET** `/api/reviews/public/cafe/{cafeId}` - Get reviews for a cafe
- **GET** `/api/reviews/public/user/{userId}` - Get reviews by user
- **GET** `/api/reviews/public/recent` - Get recent reviews
- **POST** `/api/reviews` - Create review (requires auth)
- **PUT** `/api/reviews/{id}` - Update review (requires auth)
- **DELETE** `/api/reviews/{id}` - Delete review (requires auth)

### Busy Hours
- **GET** `/api/busy/public/cafe/{cafeId}` - Get busy hours for cafe
- **GET** `/api/busy/public/cafe/{cafeId}/current` - Get current busy status
- **POST** `/api/busy` - Report busy status (requires auth)

### Admin
- **GET** `/api/admin/reviews/pending` - Get pending reviews
- **GET** `/api/admin/reviews` - Get all reviews
- **GET** `/api/admin/stats` - Get admin statistics

## How to Test

### 1. Test Health Endpoint
Visit in browser:
```
https://your-backend-url.onrender.com/api/health
```

Should return:
```json
{"status":"ok"}
```

### 2. Test from Frontend
Your frontend should be making requests to endpoints like:
- `https://your-backend.onrender.com/api/cafes/public/search`
- `https://your-backend.onrender.com/api/auth/login`
- etc.

## Common Issues

### Issue 1: Accessing Root URL
❌ **Wrong**: `https://your-backend.onrender.com/`
✅ **Correct**: `https://your-backend.onrender.com/api/health`

### Issue 2: Frontend API URL Missing `/api`
❌ **Wrong**: `VITE_API_URL=https://backend.onrender.com`
✅ **Correct**: Frontend should append `/api` to requests, or use full paths

### Issue 3: Missing Trailing Slash or Extra Path
❌ **Wrong**: `https://backend.onrender.com/api/`
✅ **Correct**: `https://backend.onrender.com/api/health`

## Verify Frontend Configuration

Check your frontend environment variable:
- **VITE_API_URL** should be: `https://your-backend.onrender.com`
- Frontend code should append `/api/...` to make requests

Example frontend request:
```javascript
// VITE_API_URL = https://backend.onrender.com
axios.get('/api/cafes/public/search') 
// Makes request to: https://backend.onrender.com/api/cafes/public/search
```

## Quick Fix Checklist

1. ✅ Test `/api/health` endpoint in browser
2. ✅ Verify `VITE_API_URL` is set correctly in frontend
3. ✅ Check browser console for actual API calls being made
4. ✅ Verify frontend is using correct API paths (starting with `/api/`)

## Still Getting 404?

1. **Check the exact URL** you're accessing
2. **Check browser Network tab** to see what requests are being made
3. **Verify backend is actually running** - check Render logs
4. **Test health endpoint first** - `/api/health` should always work

