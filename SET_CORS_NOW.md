# Set CORS Environment Variable - Quick Guide

## Your Backend URL
✅ `https://cafefinder-tr3j.onrender.com` (This is your backend - we confirmed it works!)

## Your Frontend URL
❓ What is your frontend URL? (It should be different from the backend)

## How to Set CORS in Backend

### Step 1: Find Your Frontend URL
1. Go to Render Dashboard
2. Look for your **Frontend Static Site** service
3. Copy the URL (it will be something like `https://cafe-finder-frontend.onrender.com`)

### Step 2: Set CORS_ALLOWED_ORIGINS in Backend
1. Go to Render Dashboard
2. Click on your **Backend service** (`cafefinder-tr3j`)
3. Click **"Environment"** tab
4. Look for `CORS_ALLOWED_ORIGINS` variable:
   - If it exists: Click **"Edit"** → Update the value
   - If it doesn't exist: Click **"Add Environment Variable"**
5. **Key**: `CORS_ALLOWED_ORIGINS`
6. **Value**: Your frontend URL (e.g., `https://your-frontend-url.onrender.com`)
   - ⚠️ **No trailing slash** (don't include `/` at the end)
   - ⚠️ **Use https://** (not http://)
   - ⚠️ **Full URL** (e.g., `https://cafe-finder-frontend.onrender.com`)
7. Click **"Save Changes"**

### Step 3: Redeploy Backend
1. Go to **"Manual Deploy"** tab
2. Click **"Clear build cache & deploy"**
3. Wait for deployment to complete (2-3 minutes)

### Step 4: Test
1. Visit your frontend URL
2. Open browser console (F12)
3. Check for CORS errors - they should be gone!

## Example

If your frontend URL is `https://cafe-finder-frontend.onrender.com`:

**Backend Environment Variable:**
```
CORS_ALLOWED_ORIGINS = https://cafe-finder-frontend.onrender.com
```

## Multiple Frontends?

If you have multiple frontend URLs, separate with commas:
```
CORS_ALLOWED_ORIGINS = https://frontend1.onrender.com,https://frontend2.onrender.com
```

## Still Need Help?

Share your frontend URL and I'll tell you exactly what to set!

