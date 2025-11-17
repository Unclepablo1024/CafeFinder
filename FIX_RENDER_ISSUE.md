# Fix: "mvn: command not found" Error in Render

## The Problem
Your service was created with **Node.js runtime** instead of **Java runtime**. That's why Maven (`mvn`) is not available.

## Solution: Fix the Service Runtime

You have **two options**:

---

## Option 1: Change Runtime in Existing Service (Quick Fix)

1. Go to your **Render Dashboard**
2. Click on your **backend service** (the one that's failing)
3. Go to **"Settings"** tab
4. Scroll down to **"Environment"** section
5. Find **"Runtime"** dropdown
6. Change it from **"Node"** to **"Java"**
7. Scroll down and click **"Save Changes"**
8. Go to **"Manual Deploy"** → **"Clear build cache & deploy"**

**Note**: If you don't see a Runtime dropdown, you may need to delete and recreate the service (see Option 2).

---

## Option 2: Delete and Recreate Service (Recommended)

### Step 1: Delete the Incorrect Service
1. Go to your **Render Dashboard**
2. Click on your backend service
3. Go to **"Settings"** tab
4. Scroll to the bottom
5. Click **"Delete Service"**
6. Confirm deletion

### Step 2: Create New Backend Service (Correctly)

1. Click **"New +"** → **"Web Service"**
2. Connect your GitHub repository
3. **IMPORTANT SETTINGS**:
   - **Name**: `cafe-finder-backend`
   - **Region**: Choose closest to you
   - **Branch**: `main`
   - **Root Directory**: 
     - If repo root is `CafeFinderFinal` → Set to: `CafeFinder`
     - If repo root is `CafeFinder` → Leave **EMPTY**
   - **Runtime**: ⚠️ **MUST BE "Java"** (NOT Node.js!)
   - **Build Command**: `cd backend && mvn clean package -DskipTests`
   - **Start Command**: `cd backend && java -jar target/cafe-finder-0.0.1-SNAPSHOT.jar`
   - **Plan**: Free

4. **Set Environment Variables**:
   ```
   MONGODB_URI = [Your MongoDB Atlas connection string]
   CAFEFINDER_APP_JWT_SECRET = [Generate: openssl rand -base64 32]
   GOOGLE_PLACES_API_KEY = [Your Google Places API key]
   CORS_ALLOWED_ORIGINS = [Leave empty for now]
   ```

5. Click **"Create Web Service"**

---

## Option 3: Use Blueprint (render.yaml) - Easiest

If you want Render to auto-configure everything:

1. **Delete the incorrectly created service** (if it exists)
2. Go to **"New +"** → **"Blueprint"**
3. Connect your GitHub repository
4. Render will detect `render.yaml` and create services automatically
5. **Still need to set environment variables manually** in Render dashboard

---

## How to Verify It's Fixed

After fixing, check the build logs. You should see:
- ✅ "Using Java version X" (NOT "Using Node.js version X")
- ✅ Maven commands executing successfully
- ✅ Build completing with "BUILD SUCCESS"

If you still see "Using Node.js", the runtime is still wrong.

---

## Files I've Added to Help

1. **`backend/system.properties`** - Specifies Java 17 for Render
2. **Updated `render.yaml`** - Correct Java configuration

Make sure to commit and push these changes:
```bash
git add backend/system.properties render.yaml
git commit -m "Fix Render deployment - add system.properties and update render.yaml"
git push
```

---

## Still Having Issues?

If the problem persists:

1. **Check the service type**: Make sure you created a **"Web Service"**, not a **"Static Site"**
2. **Verify Root Directory**: If your repo structure is `CafeFinderFinal/CafeFinder/backend/`, set Root Directory to `CafeFinder`
3. **Check build logs**: Look for the exact error message
4. **Verify Java runtime**: In build logs, it should say "Using Java version" not "Using Node.js version"

---

## Quick Checklist

- [ ] Service is a **Web Service** (not Static Site)
- [ ] Runtime is set to **Java** (not Node.js)
- [ ] Root Directory is correct (`CafeFinder` or empty)
- [ ] Build command: `cd backend && mvn clean package -DskipTests`
- [ ] Start command: `cd backend && java -jar target/cafe-finder-0.0.1-SNAPSHOT.jar`
- [ ] `system.properties` file is committed to repo
- [ ] Environment variables are set

