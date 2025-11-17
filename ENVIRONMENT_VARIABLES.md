# Environment Variables for Render Deployment

## Backend Service Environment Variables

Set these in your **Backend Web Service** (Docker) in Render:

### Required Variables:

| Variable Name | Description | Example Value | How to Get |
|--------------|-------------|---------------|------------|
| `MONGODB_URI` | MongoDB Atlas connection string | `mongodb+srv://username:password@cluster0.xxxxx.mongodb.net/cafe_finder?retryWrites=true&w=majority` | From MongoDB Atlas → Connect → Connection String |
| `CAFEFINDER_APP_JWT_SECRET` | Secret key for JWT token signing | `aB3xK9mP2qR7vT5wY8zA1bC4dE6fG0hI` | Generate with: `openssl rand -base64 32` |
| `GOOGLE_PLACES_API_KEY` | Google Places API key | `AIzaSyBxxxxxxxxxxxxxxxxxxxxxxxxxxxxx` | From Google Cloud Console |
| `CORS_ALLOWED_ORIGINS` | Frontend URL(s) allowed for CORS | `https://cafe-finder-frontend.onrender.com` | Your frontend Render URL (set after frontend deploys) |

### Optional Variables (have defaults):

| Variable Name | Default Value | Description |
|--------------|---------------|-------------|
| `PORT` | `8082` | Server port (Render sets this automatically, don't set manually) |

---

## Frontend Service Environment Variables

Set these in your **Frontend Static Site** in Render:

### Required Variables:

| Variable Name | Description | Example Value | How to Get |
|--------------|-------------|---------------|------------|
| `VITE_API_URL` | Backend API URL | `https://cafe-finder-backend.onrender.com` | Your backend Render URL |
| `VITE_GOOGLE_PLACES_API_KEY` | Google Places API key | `AIzaSyBxxxxxxxxxxxxxxxxxxxxxxxxxxxxx` | From Google Cloud Console |
| `VITE_GOOGLE_MAPS_API_KEY` | Google Maps API key | `AIzaSyBxxxxxxxxxxxxxxxxxxxxxxxxxxxxx` | From Google Cloud Console |

---

## Step-by-Step: Setting Environment Variables in Render

### For Backend Service:

1. Go to your **Backend Service** in Render Dashboard
2. Click on **"Environment"** tab
3. Click **"Add Environment Variable"** for each variable
4. Enter the **Key** and **Value**
5. Click **"Save Changes"**

### For Frontend Service:

1. Go to your **Frontend Service** in Render Dashboard
2. Click on **"Environment"** tab
3. Click **"Add Environment Variable"** for each variable
4. Enter the **Key** and **Value**
5. Click **"Save Changes"**

---

## Quick Copy-Paste Format

### Backend Variables (copy these to Render):

```
MONGODB_URI=mongodb+srv://username:password@cluster0.xxxxx.mongodb.net/cafe_finder?retryWrites=true&w=majority
CAFEFINDER_APP_JWT_SECRET=your-generated-secret-here
GOOGLE_PLACES_API_KEY=your-google-places-api-key
CORS_ALLOWED_ORIGINS=https://cafe-finder-frontend.onrender.com
```

### Frontend Variables (copy these to Render):

```
VITE_API_URL=https://cafe-finder-backend.onrender.com
VITE_GOOGLE_PLACES_API_KEY=your-google-places-api-key
VITE_GOOGLE_MAPS_API_KEY=your-google-maps-api-key
```

---

## How to Get Each Value

### 1. MONGODB_URI

1. Go to [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Click on your cluster
3. Click **"Connect"**
4. Choose **"Connect your application"**
5. Copy the connection string
6. Replace `<password>` with your database user password
7. Replace `<dbname>` with `cafe_finder` (or your preferred database name)

**Example:**
```
mongodb+srv://myuser:mypassword@cluster0.abc123.mongodb.net/cafe_finder?retryWrites=true&w=majority
```

### 2. CAFEFINDER_APP_JWT_SECRET

Generate a secure random string:

**On Mac/Linux:**
```bash
openssl rand -base64 32
```

**On Windows (PowerShell):**
```powershell
-join ((48..57) + (65..90) + (97..122) | Get-Random -Count 32 | % {[char]$_})
```

**Or use an online generator:**
- Visit: https://www.random.org/strings/
- Length: 32
- Characters: Alphanumeric

**Example:**
```
aB3xK9mP2qR7vT5wY8zA1bC4dE6fG0hI3jK5lM7nO9pQ1rS3tU5vW7xY9z
```

### 3. GOOGLE_PLACES_API_KEY & VITE_GOOGLE_PLACES_API_KEY

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing
3. Enable **Places API**
4. Go to **Credentials** → **Create Credentials** → **API Key**
5. Copy the API key
6. (Optional) Restrict the API key to specific APIs and domains

**Note:** You can use the same API key for both backend and frontend, or create separate keys.

### 4. VITE_GOOGLE_MAPS_API_KEY

1. Same as above, but enable **Maps JavaScript API**
2. Create an API key (can be the same as Places API key if you want)

### 5. VITE_API_URL

1. After deploying your backend, copy the URL from Render
2. It will look like: `https://cafe-finder-backend.onrender.com`
3. **Don't include** `/api` at the end
4. **Don't include** trailing slash

### 6. CORS_ALLOWED_ORIGINS

1. After deploying your frontend, copy the URL from Render
2. It will look like: `https://cafe-finder-frontend.onrender.com`
3. **Don't include** trailing slash
4. If you have multiple origins, separate with commas: `https://frontend1.onrender.com,https://frontend2.onrender.com`

---

## Important Notes

### ⚠️ Security Warnings:

1. **Never commit these values to Git** - They're already in `.gitignore`
2. **Use different JWT secrets** for production vs development
3. **Restrict Google API keys** to specific domains in Google Cloud Console
4. **Keep MongoDB password secure** - Don't share it

### 🔄 Update Order:

1. **First**: Deploy backend and get backend URL
2. **Second**: Set `VITE_API_URL` in frontend with backend URL
3. **Third**: Deploy frontend and get frontend URL
4. **Fourth**: Update `CORS_ALLOWED_ORIGINS` in backend with frontend URL
5. **Fifth**: Redeploy backend to apply CORS changes

### ✅ Verification:

After setting variables:
- Backend: Visit `https://your-backend-url.onrender.com/api/health` - should work
- Frontend: Visit your frontend URL - should connect to backend
- Check browser console - no CORS errors

---

## Troubleshooting

### "Invalid MongoDB URI"
- Check that password is URL-encoded (special characters need encoding)
- Verify database name is correct
- Ensure IP whitelist includes Render's IPs (or use 0.0.0.0/0 for testing)

### "CORS errors"
- Verify `CORS_ALLOWED_ORIGINS` matches frontend URL exactly (no trailing slash)
- Check that backend was redeployed after setting CORS variable

### "API connection failed"
- Verify `VITE_API_URL` is correct (no trailing slash, no `/api` suffix)
- Check backend is running and accessible
- Verify CORS is configured correctly

### "JWT authentication failed"
- Ensure `CAFEFINDER_APP_JWT_SECRET` is set and not empty
- Verify it's the same value if you have multiple backend instances

