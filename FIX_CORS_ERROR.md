# Fix CORS Error - Quick Guide

## The Problem

If `CORS_ALLOWED_ORIGINS` is not set, your backend defaults to `http://localhost:5173`, which blocks your production frontend URL.

## The Fix

### Step 1: Get Your Frontend URL
- Go to your Frontend service in Render
- Copy the URL (e.g., `https://cafe-finder-frontend.onrender.com`)

### Step 2: Set CORS_ALLOWED_ORIGINS in Backend
1. Go to **Backend Service** in Render Dashboard
2. Click **"Environment"** tab
3. Click **"Add Environment Variable"** (or edit if it exists)
4. **Key**: `CORS_ALLOWED_ORIGINS`
5. **Value**: Your frontend URL (e.g., `https://cafe-finder-frontend.onrender.com`)
   - ⚠️ **No trailing slash**
   - ⚠️ **Use https://**
   - ⚠️ **No /api or path**
6. Click **"Save Changes"**

### Step 3: Redeploy Backend
1. Go to **"Manual Deploy"** tab
2. Click **"Clear build cache & deploy"**
3. Wait for deployment to complete

### Step 4: Test
1. Visit your frontend URL
2. Open browser console (F12)
3. Check for CORS errors - they should be gone!

## Common CORS Error Messages

You might see these in browser console:
- ❌ `Access to fetch at '...' from origin '...' has been blocked by CORS policy`
- ❌ `No 'Access-Control-Allow-Origin' header is present`
- ❌ `CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource`

After fixing, these should disappear.

## Multiple Origins

If you need to allow multiple frontend URLs, separate with commas:
```
https://cafe-finder-frontend.onrender.com,https://cafe-finder-staging.onrender.com
```

## Verify It's Working

After setting the variable and redeploying:

1. **Check backend logs** - Should show no CORS errors
2. **Test API call** - Frontend should successfully connect to backend
3. **Browser console** - No CORS errors

## Still Not Working?

1. **Double-check the URL**:
   - Must match exactly (case-sensitive)
   - No trailing slash
   - Full URL with https://

2. **Verify backend redeployed**:
   - Check deployment logs
   - Environment variable should be visible in logs

3. **Clear browser cache**:
   - Hard refresh: Ctrl+Shift+R (Windows) or Cmd+Shift+R (Mac)

4. **Check both URLs**:
   - Backend: `https://your-backend.onrender.com`
   - Frontend: `https://your-frontend.onrender.com`
   - CORS_ALLOWED_ORIGINS should match frontend URL exactly

