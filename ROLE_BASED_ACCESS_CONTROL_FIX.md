# Role-Based Access Control Fix - Summary

## Problem
Any authenticated user (including EMPLOYEE role) could create new users/employees via the `/api/internal/users` endpoint.

## Solution
Implemented role-based access control to restrict user creation to only **ADMIN** and **HR** roles.

---

## Changes Made

### 1. **Auth Service - SecurityConfig** ✅
**File:** `auth-service/src/main/java/com/merp/authservice/config/SecurityConfig.java`

Added role restriction:
```java
.requestMatchers("/api/internal/users").hasAnyRole("ADMIN", "HR")
```

### 2. **Auth Service - JwtUtil** ✅
**File:** `auth-service/src/main/java/com/merp/authservice/security/JwtUtil.java`

- Added `extractRoles()` method to extract roles from JWT
- Updated `generateToken()` to accept roles as parameter
- Kept backward compatibility with overloaded method

### 3. **Auth Service - JwtFilter** ✅
**File:** `auth-service/src/main/java/com/merp/authservice/security/JwtFilter.java`

- Extract roles from JWT token
- Convert roles to Spring Security authorities with `ROLE_` prefix
- Pass authorities to authentication token

### 4. **Auth Service - AuthService** ✅
**File:** `auth-service/src/main/java/com/merp/authservice/service/AuthService.java`

- Updated login method to pass user's actual role when generating JWT token
- Added import for `java.util.List`

---

## Testing

### Step 1: Make sure roles exist in database
```sql
-- Check existing roles
SELECT * FROM auth_db.roles;

-- If HR role doesn't exist, create it
INSERT INTO auth_db.roles (name) VALUES ('HR');
```

### Step 2: Create test users with different roles
```sql
-- Create ADMIN user (for testing)
-- Password: admin123
INSERT INTO auth_db.user (email, password, role_id) 
VALUES ('admin@test.com', '$2a$10$...(bcrypt hash)...', 
    (SELECT id FROM auth_db.roles WHERE name = 'ADMIN'));

-- Create EMPLOYEE user (for testing)
-- Password: employee123
INSERT INTO auth_db.user (email, password, role_id) 
VALUES ('employee@test.com', '$2a$10$...(bcrypt hash)...', 
    (SELECT id FROM auth_db.roles WHERE name = 'EMPLOYEE'));

-- Create HR user (for testing)
-- Password: hr123
INSERT INTO auth_db.user (email, password, role_id) 
VALUES ('hr@test.com', '$2a$10$...(bcrypt hash)...', 
    (SELECT id FROM auth_db.roles WHERE name = 'HR'));
```

### Step 3: Rebuild and restart auth-service
```bash
cd auth-service
mvn clean package
# Restart the service
```

### Step 4: Test Access Control

#### Test 1: Login as EMPLOYEE (should FAIL to create user)
```bash
# 1. Login as employee
POST http://localhost:8002/api/auth/login
{
  "email": "employee@test.com",
  "password": "employee123"
}

# 2. Copy the JWT token from response

# 3. Try to create user (should return 403 Forbidden)
POST http://localhost:8002/api/internal/users
Authorization: Bearer <employee_token>
{
  "email": "new.user@test.com",
  "password": "password123",
  "role": "EMPLOYEE",
  "firstName": "New",
  "lastName": "User",
  "department": "IT",
  "jobTitle": "Developer",
  "salary": 50000
}
```
**Expected Result:** `403 Forbidden` ❌

#### Test 2: Login as ADMIN (should SUCCESS)
```bash
# 1. Login as admin
POST http://localhost:8002/api/auth/login
{
  "email": "admin@test.com",
  "password": "admin123"
}

# 2. Copy the JWT token from response

# 3. Try to create user (should work)
POST http://localhost:8002/api/internal/users
Authorization: Bearer <admin_token>
{
  "email": "new.user@test.com",
  "password": "password123",
  "role": "EMPLOYEE",
  "firstName": "New",
  "lastName": "User",
  "department": "IT",
  "jobTitle": "Developer",
  "salary": 50000
}
```
**Expected Result:** `200 OK` with user created ✅

#### Test 3: Login as HR (should SUCCESS)
```bash
# 1. Login as HR
POST http://localhost:8002/api/auth/login
{
  "email": "hr@test.com",
  "password": "hr123"
}

# 2. Copy the JWT token from response

# 3. Try to create user (should work)
POST http://localhost:8002/api/internal/users
Authorization: Bearer <hr_token>
{
  "email": "another.user@test.com",
  "password": "password123",
  "role": "EMPLOYEE",
  "firstName": "Another",
  "lastName": "User",
  "department": "HR",
  "jobTitle": "HR Manager",
  "salary": 60000
}
```
**Expected Result:** `200 OK` with user created ✅

---

## Security Flow

```
1. User logs in → receives JWT token with their role
2. User sends request with JWT in Authorization header
3. JwtFilter extracts email and roles from JWT
4. JwtFilter creates authentication with authorities (ROLE_ADMIN, ROLE_HR, etc.)
5. SecurityFilterChain checks if user has required role
6. If YES → allow request
   If NO → return 403 Forbidden
```

---

## Important Notes

1. **Role names in JWT vs Spring Security:**
   - JWT stores: `"roles": ["ADMIN"]`
   - Spring Security expects: `ROLE_ADMIN`
   - JwtFilter adds the `ROLE_` prefix automatically

2. **HR Role must exist:**
   - Make sure the `HR` role exists in `auth_db.roles` table
   - Otherwise, users can't be assigned to HR role

3. **Password Hashing:**
   - When creating test users in DB, use BCrypt to hash passwords
   - Or create users via the API if you have an admin account

---

## Verification

Check that roles are properly set in JWT:
1. Login and get token
2. Go to https://jwt.io
3. Paste your token
4. Check the payload - should see:
   ```json
   {
     "userId": 1,
     "roles": ["ADMIN"],
     "sub": "admin@test.com",
     "iat": ...,
     "exp": ...
   }
   ```
