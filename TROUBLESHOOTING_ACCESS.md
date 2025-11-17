# Troubleshooting: Can't Access the Page

## Quick Questions to Help Diagnose

1. **What URL are you trying to access?**
   - Backend: `https://cafefinder-tr3j.onrender.com`
   - Frontend: `https://your-frontend-url.onrender.com` (different URL)

2. **What error are you seeing?**
   - Whitelabel error page?
   - "This site can't be reached"?
   - Blank page?
   - CORS error?
   - Something else?

3. **Do you have a frontend deployed?**
   - Check Render Dashboard for a Static Site service

## Common Issues

### Issue 1: Trying to Access Backend URL Directly
❌ **Problem**: Visiting `https://cafefinder-tr3j.onrender.com` directly
✅ **Solution**: This is your **backend API**, not your website. You need a **frontend** deployed.

### Issue 2: Frontend Not Deployed
❌ **Problem**: Only backend is deployed, no frontend
✅ **Solution**: Deploy your frontend as a Static Site on Render

### Issue 3: Frontend Can't Connect to Backend
❌ **Problem**: Frontend loads but shows errors
✅ **Solution**: 
- Check `VITE_API_URL` is set in frontend environment variables
- Check `CORS_ALLOWED_ORIGINS` is set in backend environment variables

## What You Should Have

### Backend (Already Deployed ✅)
- URL: `https://cafefinder-tr3j.onrender.com`
- Type: Web Service (Docker)
- Status: Running ✅

### Frontend (Need to Deploy)
- URL: `https://your-frontend-name.onrender.com` (different URL)
- Type: Static Site
- Status: Need to create this

## Steps to Fix

### Step 1: Check if Frontend is Deployed
1. Go to Render Dashboard
2. Look for a **Static Site** service
3. If you don't see one, you need to deploy the frontend

### Step 2: Deploy Frontend (if not deployed)
1. Go to Render Dashboard → **"New +"** → **"Static Site"**
2. Connect your GitHub repository
3. Settings:
   - **Name**: `cafe-finder-frontend` (or any name)
   - **Root Directory**: `CafeFinder` (or empty, depending on your repo structure)
   - **Build Command**: `cd frontend && npm install && npm run build`
   - **Publish Directory**: `frontend/dist`
4. Environment Variables:
   - `VITE_API_URL` = `https://cafefinder-tr3j.onrender.com`
   - `VITE_GOOGLE_PLACES_API_KEY` = [Your API key]
   - `VITE_GOOGLE_MAPS_API_KEY` = [Your API key]
5. Click **"Create Static Site"**
6. Wait for deployment
7. Copy the frontend URL (will be different from backend)

### Step 3: Set CORS in Backend
1. Go to Backend service → **Environment** tab
2. Set `CORS_ALLOWED_ORIGINS` = Your frontend URL
3. Redeploy backend

### Step 4: Access Your App
Visit your **frontend URL** (not the backend URL)

## Quick Test

Test if backend is accessible:
```
https://cafefinder-tr3j.onrender.com/api/health
```
Should return: `{"status":"ok"}`

If this works, your backend is fine. The issue is likely:
- Frontend not deployed, OR
- Trying to access backend URL instead of frontend URL

