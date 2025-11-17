# Render Deployment Guide for Cafe Finder

This guide will walk you through deploying your Cafe Finder application to Render.

## Prerequisites

1. **MongoDB Atlas Account** (free tier available)
   - Sign up at https://www.mongodb.com/cloud/atlas
   - Create a free cluster
   - Get your connection string

2. **Google Cloud Console Account**
   - Get Google Maps API key
   - Get Google Places API key
   - Enable required APIs: Maps JavaScript API, Places API

3. **Render Account**
   - Sign up at https://render.com (free tier available)

## Step-by-Step Deployment

### 1. Set Up MongoDB Atlas

1. Go to https://www.mongodb.com/cloud/atlas
2. Create a new cluster (free tier M0 is fine)
3. Create a database user:
   - Go to Database Access → Add New Database User
   - Choose Password authentication
   - Save the username and password
4. Whitelist IP addresses:
   - Go to Network Access → Add IP Address
   - Click "Allow Access from Anywhere" (0.0.0.0/0) for development
5. Get your connection string:
   - Go to Database → Connect → Connect your application
   - Copy the connection string
   - Replace `<password>` with your database user password
   - Replace `<dbname>` with `cafe_finder` (or your preferred database name)
   - Example: `mongodb+srv://username:password@cluster0.xxxxx.mongodb.net/cafe_finder?retryWrites=true&w=majority`

### 2. Deploy Backend to Render

1. **Push your code to GitHub** (if not already done):
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin <your-github-repo-url>
   git push -u origin main
   ```

2. **Create a new Web Service on Render**:
   - Go to https://dashboard.render.com
   - Click "New +" → "Web Service"
   - Connect your GitHub repository
   - Select the repository containing your code

3. **Configure the Backend Service**:
   - **Name**: `cafe-finder-backend`
   - **Root Directory**: Leave empty (or `CafeFinder` if your repo structure requires it)
   - **Environment**: `Java`
   - **Build Command**: `cd backend && mvn clean package -DskipTests`
   - **Start Command**: `cd backend && java -jar target/cafe-finder-0.0.1-SNAPSHOT.jar`
   - **Plan**: Free (or choose a paid plan)

4. **Set Environment Variables** (in Render dashboard):
   - `MONGODB_URI`: Your MongoDB Atlas connection string
   - `CAFEFINDER_APP_JWT_SECRET`: Generate a secure random string (e.g., use `openssl rand -base64 32`)
   - `GOOGLE_PLACES_API_KEY`: Your Google Places API key
   - `CORS_ALLOWED_ORIGINS`: Set this to your frontend URL (you'll update this after deploying frontend)
     - Initially: `https://cafe-finder-frontend.onrender.com` (or your frontend URL)
     - Format: `https://your-frontend-url.onrender.com` (no trailing slash)

5. **Deploy**:
   - Click "Create Web Service"
   - Wait for the build to complete
   - Note your backend URL (e.g., `https://cafe-finder-backend.onrender.com`)

### 3. Deploy Frontend to Render

1. **Create a new Static Site on Render**:
   - Go to https://dashboard.render.com
   - Click "New +" → "Static Site"
   - Connect your GitHub repository (same one)

2. **Configure the Frontend Service**:
   - **Name**: `cafe-finder-frontend`
   - **Root Directory**: Leave empty (or `CafeFinder` if your repo structure requires it)
   - **Build Command**: `cd frontend && npm install && npm run build`
   - **Publish Directory**: `frontend/dist`
   - **Plan**: Free (or choose a paid plan)

3. **Set Environment Variables** (in Render dashboard):
   - `VITE_API_URL`: Your backend URL (e.g., `https://cafe-finder-backend.onrender.com`)
   - `VITE_GOOGLE_PLACES_API_KEY`: Your Google Places API key
   - `VITE_GOOGLE_MAPS_API_KEY`: Your Google Maps API key

4. **Deploy**:
   - Click "Create Static Site"
   - Wait for the build to complete
   - Note your frontend URL (e.g., `https://cafe-finder-frontend.onrender.com`)

### 4. Update CORS Configuration

1. Go back to your **Backend Service** on Render
2. Update the `CORS_ALLOWED_ORIGINS` environment variable:
   - Set it to your frontend URL: `https://cafe-finder-frontend.onrender.com`
   - If you have multiple origins, separate them with commas: `https://frontend1.onrender.com,https://frontend2.onrender.com`
3. **Manual Deploy** → "Clear build cache & deploy" to apply changes

### 5. Verify Deployment

1. **Test Backend**:
   - Visit: `https://your-backend-url.onrender.com/api/health`
   - Should return a health check response

2. **Test Frontend**:
   - Visit your frontend URL
   - Try logging in or searching for cafes
   - Check browser console for any errors

3. **Common Issues**:
   - **CORS errors**: Make sure `CORS_ALLOWED_ORIGINS` matches your frontend URL exactly
   - **API connection errors**: Verify `VITE_API_URL` is set correctly in frontend
   - **Database connection errors**: Check MongoDB Atlas connection string and IP whitelist

## Using render.yaml (Alternative Method)

If you prefer using the `render.yaml` file:

1. **Ensure render.yaml is in your repository root**
2. **On Render Dashboard**:
   - Go to "New +" → "Blueprint"
   - Connect your GitHub repository
   - Render will automatically detect `render.yaml` and create services
3. **Set Environment Variables** in the Render dashboard for each service
4. **Deploy**

## Environment Variables Summary

### Backend Environment Variables:
```
MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/cafe_finder?retryWrites=true&w=majority
CAFEFINDER_APP_JWT_SECRET=your-secure-random-string-here
GOOGLE_PLACES_API_KEY=your-google-places-api-key
CORS_ALLOWED_ORIGINS=https://cafe-finder-frontend.onrender.com
```

### Frontend Environment Variables:
```
VITE_API_URL=https://cafe-finder-backend.onrender.com
VITE_GOOGLE_PLACES_API_KEY=your-google-places-api-key
VITE_GOOGLE_MAPS_API_KEY=your-google-maps-api-key
```

## Important Notes

1. **Free Tier Limitations**:
   - Services spin down after 15 minutes of inactivity
   - First request after spin-down may take 30-60 seconds
   - Consider upgrading to a paid plan for production

2. **Security**:
   - Never commit `.env` files or API keys to Git
   - Use Render's environment variables for all secrets
   - Keep your JWT secret secure and random

3. **Database**:
   - MongoDB Atlas free tier is sufficient for development
   - Consider upgrading for production workloads

4. **API Keys**:
   - Restrict Google API keys to specific domains in Google Cloud Console
   - Set HTTP referrer restrictions for Maps/Places APIs

## Troubleshooting

### Backend won't start:
- Check build logs in Render dashboard
- Verify Java version (should be 17)
- Check that `target/cafe-finder-0.0.1-SNAPSHOT.jar` exists after build

### Frontend can't connect to backend:
- Verify `VITE_API_URL` is set correctly
- Check CORS configuration in backend
- Ensure backend is running and accessible

### Database connection issues:
- Verify MongoDB Atlas connection string
- Check IP whitelist in MongoDB Atlas
- Ensure database user has correct permissions

### Build failures:
- Check that all dependencies are in `package.json` (frontend) and `pom.xml` (backend)
- Verify build commands are correct
- Check Render build logs for specific errors

## Next Steps

After successful deployment:
1. Test all features thoroughly
2. Set up monitoring and logging
3. Configure custom domains (if needed)
4. Set up automatic deployments from Git
5. Consider upgrading to paid plans for better performance

