# Admin Account Credentials

## Admin Accounts Created

Three admin accounts have been added to the seed data. They will be created automatically when the application starts if they don't already exist.

### Admin Account 1
- **Username**: `admin`
- **Password**: `admin123`
- **Email**: `admin@cafefinder.com`
- **Name**: Admin User
- **Role**: ADMIN, USER
- **Bio**: CafeFinder Administrator

### Admin Account 2
- **Username**: `admin2`
- **Password**: `Admin2024!`
- **Email**: `admin2@cafefinder.com`
- **Name**: Sarah Johnson
- **Role**: ADMIN, USER
- **Bio**: Senior Administrator - Content Moderation

### Admin Account 3
- **Username**: `admin3`
- **Password**: `SecurePass123!`
- **Email**: `admin3@cafefinder.com`
- **Name**: Michael Chen
- **Role**: ADMIN, USER
- **Bio**: System Administrator - Technical Operations

## How to Use

1. **Commit and push the changes** to trigger a new deployment
2. **After deployment**, the admin accounts will be automatically created (if they don't already exist)
3. **Login** using any of the credentials above at your frontend login page

## Security Notes

⚠️ **Important**: These are default credentials. For production:
- Change passwords immediately after first login
- Use strong, unique passwords
- Consider implementing password reset functionality
- Enable two-factor authentication if available

## Testing

You can test login with any of these accounts:
- Frontend: Visit your frontend URL → Login page
- Backend API: `POST /api/auth/login` with username and password

## Quick Reference

```
Username: admin        Password: admin123
Username: admin2       Password: Admin2024!
Username: admin3       Password: SecurePass123!
```

