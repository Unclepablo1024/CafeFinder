# Docker Deployment Guide for Render

Since Java runtime isn't available in your Render region, we're using **Docker** instead. This is actually a better approach as it gives you more control.

## What I've Created

1. **`backend/Dockerfile`** - Multi-stage Docker build for your Java app
2. **`.dockerignore`** - Excludes unnecessary files from Docker build
3. **Updated `render.yaml`** - Now uses Docker runtime

## Render Settings for Docker Deployment

### Backend Service (Docker)

1. **Go to Render Dashboard** → **"New +"** → **"Web Service"**

2. **Connect Repository**:
   - Connect GitHub (if not already)
   - Select your repository

3. **Settings**:
   - **Name**: `cafe-finder-backend`
   - **Region**: Choose closest to you
   - **Branch**: `main`
   - **Root Directory**: 
     - If repo root is `CafeFinderFinal` → Set to: `CafeFinder`
     - If repo root is `CafeFinder` → Leave **EMPTY**
   - **Runtime**: ⚠️ **Select "Docker"** (this is the key!)
   - **Dockerfile Path**: `backend/Dockerfile`
   - **Docker Context**: `.` (dot/period)
   - **Plan**: Free

4. **Environment Variables** (same as before):
   ```
   MONGODB_URI = [Your MongoDB Atlas connection string]
   CAFEFINDER_APP_JWT_SECRET = [Generate: openssl rand -base64 32]
   GOOGLE_PLACES_API_KEY = [Your Google Places API key]
   CORS_ALLOWED_ORIGINS = [Leave empty for now, set after frontend deploys]
   ```

5. **Click "Create Web Service"**

## How Docker Build Works

The Dockerfile uses a **multi-stage build**:

1. **Build Stage**: Uses Maven to compile your Java code
2. **Runtime Stage**: Uses a lightweight JRE to run the JAR file

This results in a smaller final image and faster deployments.

## Troubleshooting

### If Docker build fails:

1. **Check Dockerfile path**: Should be `backend/Dockerfile`
2. **Check Docker Context**: Should be `.` (current directory)
3. **Check Root Directory**: Must match your repo structure
4. **View build logs**: Look for specific error messages

### Common Issues:

- **"Cannot find Dockerfile"**: 
  - Verify Root Directory is correct
  - Check that `backend/Dockerfile` exists in your repo

- **"Build failed"**: 
  - Check build logs for Maven errors
  - Verify `pom.xml` is correct
  - Ensure all dependencies are available

- **"Port binding error"**: 
  - Make sure `application.properties` uses `${PORT:8082}`
  - Render automatically sets PORT environment variable

## Advantages of Docker Approach

✅ Works in all Render regions  
✅ More control over the build process  
✅ Consistent builds across environments  
✅ Can test locally with Docker  
✅ Smaller final image size (multi-stage build)

## Testing Locally (Optional)

You can test the Docker build locally:

```bash
cd CafeFinder
docker build -f backend/Dockerfile -t cafe-finder-backend .
docker run -p 8082:8082 \
  -e MONGODB_URI="your-mongodb-uri" \
  -e CAFEFINDER_APP_JWT_SECRET="test-secret" \
  -e GOOGLE_PLACES_API_KEY="your-key" \
  cafe-finder-backend
```

## Next Steps

1. **Commit the new files**:
   ```bash
   git add backend/Dockerfile .dockerignore render.yaml
   git commit -m "Add Docker support for Render deployment"
   git push
   ```

2. **Create the service in Render** with Docker runtime

3. **Set environment variables**

4. **Deploy and test**

The Docker approach is actually more reliable and portable than using Render's Java runtime!

