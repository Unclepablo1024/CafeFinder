# Quick Deployment Checklist

## Pre-Deployment Setup

### ✅ Code Changes Made
- [x] Updated `render.yaml` with correct configuration
- [x] Updated `application.properties` to use `${PORT}` environment variable
- [x] Fixed frontend API URL configuration
- [x] Created deployment documentation

### 🔧 Before Deploying

1. **MongoDB Atlas Setup**
   - [ ] Create MongoDB Atlas account
   - [ ] Create a free cluster
   - [ ] Create database user (save credentials)
   - [ ] Whitelist IP: `0.0.0.0/0` (or Render's IP ranges)
   - [ ] Get connection string (replace `<password>` and `<dbname>`)

2. **Google Cloud Console**
   - [ ] Get Google Maps API key
   - [ ] Get Google Places API key
   - [ ] Enable required APIs (Maps JavaScript API, Places API)
   - [ ] Set up API key restrictions (optional but recommended)

3. **GitHub Repository**
   - [ ] Push all code to GitHub
   - [ ] Verify `render.yaml` is in the repository
   - [ ] Ensure `.env` files are NOT committed (check `.gitignore`)

## Deployment Steps

### Backend Deployment

1. **Create Web Service on Render**
   - [ ] Go to Render Dashboard → New → Web Service
   - [ ] Connect GitHub repository
   - [ ] **Root Directory**: `CafeFinder` (if repo root is CafeFinderFinal) OR leave empty (if repo root is CafeFinder)
   - [ ] **Environment**: Java
   - [ ] **Build Command**: `cd backend && mvn clean package -DskipTests`
   - [ ] **Start Command**: `cd backend && java -jar target/cafe-finder-0.0.1-SNAPSHOT.jar`
   - [ ] **Plan**: Free

2. **Set Environment Variables**
   - [ ] `MONGODB_URI` = Your MongoDB Atlas connection string
   - [ ] `CAFEFINDER_APP_JWT_SECRET` = Generate secure random string
   - [ ] `GOOGLE_PLACES_API_KEY` = Your Google Places API key
   - [ ] `CORS_ALLOWED_ORIGINS` = (Set after frontend deploys) `https://your-frontend-url.onrender.com`

3. **Deploy**
   - [ ] Click "Create Web Service"
   - [ ] Wait for build to complete
   - [ ] Copy backend URL (e.g., `https://cafe-finder-backend.onrender.com`)
   - [ ] Test: Visit `https://your-backend-url.onrender.com/api/health`

### Frontend Deployment

1. **Create Static Site on Render**
   - [ ] Go to Render Dashboard → New → Static Site
   - [ ] Connect GitHub repository (same one)
   - [ ] **Root Directory**: `CafeFinder` (if repo root is CafeFinderFinal) OR leave empty
   - [ ] **Build Command**: `cd frontend && npm install && npm run build`
   - [ ] **Publish Directory**: `frontend/dist`
   - [ ] **Plan**: Free

2. **Set Environment Variables**
   - [ ] `VITE_API_URL` = Your backend URL (e.g., `https://cafe-finder-backend.onrender.com`)
   - [ ] `VITE_GOOGLE_PLACES_API_KEY` = Your Google Places API key
   - [ ] `VITE_GOOGLE_MAPS_API_KEY` = Your Google Maps API key

3. **Deploy**
   - [ ] Click "Create Static Site"
   - [ ] Wait for build to complete
   - [ ] Copy frontend URL (e.g., `https://cafe-finder-frontend.onrender.com`)

### Final Configuration

1. **Update CORS**
   - [ ] Go back to Backend service
   - [ ] Update `CORS_ALLOWED_ORIGINS` = Your frontend URL
   - [ ] Manual Deploy → "Clear build cache & deploy"

2. **Verify Everything Works**
   - [ ] Visit frontend URL
   - [ ] Try to search for cafes
   - [ ] Try to log in/register
   - [ ] Check browser console for errors
   - [ ] Test API calls are working

## Using Blueprint (render.yaml)

Alternative: Use Blueprint deployment
- [ ] Go to Render Dashboard → New → Blueprint
- [ ] Connect GitHub repository
- [ ] Render will auto-detect `render.yaml`
- [ ] Set all environment variables in Render dashboard
- [ ] Deploy

## Troubleshooting

### Backend Issues
- **Build fails**: Check Java version (should be 17), verify Maven dependencies
- **Won't start**: Check logs, verify JAR file exists at `target/cafe-finder-0.0.1-SNAPSHOT.jar`
- **Database connection fails**: Verify MongoDB URI, check IP whitelist

### Frontend Issues
- **Build fails**: Check Node version, verify all dependencies in package.json
- **Can't connect to API**: Verify `VITE_API_URL` is set correctly
- **CORS errors**: Update `CORS_ALLOWED_ORIGINS` in backend

### Common Solutions
- Clear build cache and redeploy
- Check Render logs for specific error messages
- Verify all environment variables are set correctly
- Ensure MongoDB Atlas cluster is running

## Security Reminders

- [ ] Never commit API keys or secrets to Git
- [ ] Use strong, random JWT secret
- [ ] Restrict Google API keys to specific domains
- [ ] Review MongoDB Atlas security settings
- [ ] Consider upgrading from free tier for production

## Post-Deployment

- [ ] Test all major features
- [ ] Set up automatic deployments (if desired)
- [ ] Configure custom domain (optional)
- [ ] Set up monitoring/logging (optional)
- [ ] Document your deployment URLs

