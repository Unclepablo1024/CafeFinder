# Render Dashboard Settings Guide

This guide shows you exactly what options to select when creating your services in Render.

## Option 1: Using Blueprint (render.yaml) - EASIEST METHOD

### Steps:
1. Go to https://dashboard.render.com
2. Click **"New +"** → **"Blueprint"**
3. Connect your GitHub account (if not already connected)
4. Select your repository (the one containing `render.yaml`)
5. Click **"Apply"**
6. Render will automatically detect `render.yaml` and create both services
7. **Then set environment variables** (see below)

**Advantages**: Automatically creates both services with correct settings

---

## Option 2: Manual Setup (Step-by-Step)

### Backend Service Settings

1. **Go to Render Dashboard** → Click **"New +"** → **"Web Service"**

2. **Connect Repository**:
   - Connect GitHub (if not already connected)
   - Select your repository
   - Click **"Connect"**

3. **Basic Settings**:
   - **Name**: `cafe-finder-backend` (or any name you prefer)
   - **Region**: Choose closest to your users (e.g., `Oregon (US West)`)
   - **Branch**: `main` (or `master` - whatever your default branch is)
   - **Root Directory**: 
     - If your repo root is `CafeFinderFinal` → Set to: `CafeFinder`
     - If your repo root is `CafeFinder` → Leave **EMPTY**
   - **Runtime**: `Java`
   - **Build Command**: `cd backend && mvn clean package -DskipTests`
   - **Start Command**: `cd backend && java -jar target/cafe-finder-0.0.1-SNAPSHOT.jar`

4. **Plan**:
   - **Free**: $0/month (spins down after 15 min inactivity)
   - **Starter**: $7/month (always on)
   - Choose **Free** for testing, **Starter** for production

5. **Advanced Settings** (usually default is fine):
   - **Auto-Deploy**: `Yes` (deploys on every push to main branch)
   - **Health Check Path**: Leave empty (or use `/api/health` if you have one)

6. **Environment Variables** (Click "Add Environment Variable" for each):
   ```
   MONGODB_URI = mongodb+srv://username:password@cluster.mongodb.net/cafe_finder?retryWrites=true&w=majority
   CAFEFINDER_APP_JWT_SECRET = [Generate with: openssl rand -base64 32]
   GOOGLE_PLACES_API_KEY = [Your Google Places API key]
   CORS_ALLOWED_ORIGINS = [Leave empty for now, set after frontend deploys]
   ```
   **Note**: Don't set `PORT` - Render provides this automatically

7. **Click "Create Web Service"**

8. **Wait for deployment** and copy your backend URL (e.g., `https://cafe-finder-backend.onrender.com`)

---

### Frontend Service Settings

1. **Go to Render Dashboard** → Click **"New +"** → **"Static Site"**

2. **Connect Repository**:
   - Select the same repository
   - Click **"Connect"**

3. **Basic Settings**:
   - **Name**: `cafe-finder-frontend` (or any name you prefer)
   - **Branch**: `main` (or `master`)
   - **Root Directory**: 
     - If your repo root is `CafeFinderFinal` → Set to: `CafeFinder`
     - If your repo root is `CafeFinder` → Leave **EMPTY**
   - **Build Command**: `cd frontend && npm install && npm run build`
   - **Publish Directory**: `frontend/dist`

4. **Environment Variables** (Click "Add Environment Variable" for each):
   ```
   VITE_API_URL = https://cafe-finder-backend.onrender.com
   VITE_GOOGLE_PLACES_API_KEY = [Your Google Places API key]
   VITE_GOOGLE_MAPS_API_KEY = [Your Google Maps API key]
   ```
   **Important**: Replace `https://cafe-finder-backend.onrender.com` with your actual backend URL

5. **Click "Create Static Site"**

6. **Wait for deployment** and copy your frontend URL (e.g., `https://cafe-finder-frontend.onrender.com`)

---

### Final Step: Update CORS

1. Go back to your **Backend Service** in Render dashboard
2. Go to **"Environment"** tab
3. Update `CORS_ALLOWED_ORIGINS` to your frontend URL:
   ```
   CORS_ALLOWED_ORIGINS = https://cafe-finder-frontend.onrender.com
   ```
4. Click **"Save Changes"**
5. Go to **"Manual Deploy"** → **"Clear build cache & deploy"**

---

## Visual Guide - What Each Section Looks Like

### Backend Service Creation Form:

```
┌─────────────────────────────────────────┐
│ Repository                               │
│ [Select your GitHub repo]               │
├─────────────────────────────────────────┤
│ Name: cafe-finder-backend               │
│ Region: [Oregon (US West)]              │
│ Branch: main                             │
│ Root Directory: CafeFinder (or empty)    │
├─────────────────────────────────────────┤
│ Runtime: Java                            │
│ Build Command:                           │
│   cd backend && mvn clean package...    │
│ Start Command:                           │
│   cd backend && java -jar target/...     │
├─────────────────────────────────────────┤
│ Plan: [Free] [Starter $7] [Pro $25]     │
│ Auto-Deploy: [Yes ✓]                    │
├─────────────────────────────────────────┤
│ Environment Variables:                  │
│   MONGODB_URI = [your connection string]│
│   CAFEFINDER_APP_JWT_SECRET = [secret]  │
│   GOOGLE_PLACES_API_KEY = [key]         │
│   CORS_ALLOWED_ORIGINS = [empty for now]│
└─────────────────────────────────────────┘
```

### Frontend Service Creation Form:

```
┌─────────────────────────────────────────┐
│ Repository                               │
│ [Select your GitHub repo]               │
├─────────────────────────────────────────┤
│ Name: cafe-finder-frontend               │
│ Branch: main                             │
│ Root Directory: CafeFinder (or empty)   │
├─────────────────────────────────────────┤
│ Build Command:                           │
│   cd frontend && npm install && npm...  │
│ Publish Directory: frontend/dist         │
├─────────────────────────────────────────┤
│ Environment Variables:                  │
│   VITE_API_URL = [backend URL]          │
│   VITE_GOOGLE_PLACES_API_KEY = [key]    │
│   VITE_GOOGLE_MAPS_API_KEY = [key]      │
└─────────────────────────────────────────┘
```

---

## Common Questions

### Q: What if I don't see "Java" as a runtime option?
**A**: Make sure you're creating a "Web Service", not a "Static Site". Java runtime is only available for Web Services.

### Q: What should I put in Root Directory?
**A**: 
- If your GitHub repo structure is:
  ```
  CafeFinderFinal/
    ├── CafeFinder/
    │   ├── backend/
    │   ├── frontend/
    │   └── render.yaml
  ```
  Then set Root Directory to: `CafeFinder`
  
- If your GitHub repo structure is:
  ```
  CafeFinder/
    ├── backend/
    ├── frontend/
    └── render.yaml
  ```
  Then leave Root Directory **EMPTY**

### Q: Should I use Free or Paid plan?
**A**: 
- **Free**: Good for testing, demos, low traffic. Services spin down after 15 min inactivity.
- **Starter ($7/month)**: Better for production - always on, faster response times.

### Q: What if the build fails?
**A**: 
1. Check the build logs in Render dashboard
2. Verify Root Directory is correct
3. Make sure all files are committed to GitHub
4. Check that build commands match your project structure

### Q: How do I know if my service is running?
**A**: 
- Backend: Visit `https://your-backend-url.onrender.com/api/health`
- Frontend: Visit your frontend URL - you should see your app

### Q: Can I change settings after deployment?
**A**: Yes! Go to your service → Settings tab → Edit any field → Save → Manual Deploy

---

## Quick Reference Checklist

**Backend:**
- [ ] Type: Web Service
- [ ] Runtime: Java
- [ ] Root Directory: `CafeFinder` (or empty)
- [ ] Build: `cd backend && mvn clean package -DskipTests`
- [ ] Start: `cd backend && java -jar target/cafe-finder-0.0.1-SNAPSHOT.jar`
- [ ] Plan: Free (or Starter)
- [ ] Set 4 environment variables

**Frontend:**
- [ ] Type: Static Site
- [ ] Root Directory: `CafeFinder` (or empty)
- [ ] Build: `cd frontend && npm install && npm run build`
- [ ] Publish: `frontend/dist`
- [ ] Set 3 environment variables

**After Both Deploy:**
- [ ] Update CORS in backend with frontend URL
- [ ] Test both services
- [ ] Verify API connection works

