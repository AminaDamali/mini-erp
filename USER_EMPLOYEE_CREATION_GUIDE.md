# User & Employee Creation - Usage Guide

## Overview
The system now supports creating both a **User** (in auth_db) and an **Employee** (in hr_db) with a single API call to the auth-service.

## API Endpoint
```
POST http://localhost:8002/api/internal/users
```

## Request Body

### Option 1: Create User Only (Original)
```json
{
  "email": "user@example.com",
  "password": "password123",
  "role": "ADMIN"
}
```
**Result:** Only creates a user in the `auth_db.user` table.

---

### Option 2: Create User + Employee (New Feature)
```json
{
  "email": "john.doe@example.com",
  "password": "password123",
  "role": "EMPLOYEE",
  "firstName": "John",
  "lastName": "Doe",
  "department": "Engineering",
  "jobTitle": "Software Developer",
  "salary": 75000.00
}
```
**Result:** Creates both:
1. A user in the `auth_db.user` table
2. An employee in the `hr_db.employee` table (linked via `userId`)

---

## How It Works

### Architecture Flow
```
1. Client calls auth-service → POST /api/internal/users
2. Auth-service creates user in auth_db
3. Auth-service calls hr-service → POST /api/employees/internal
4. HR-service creates employee in hr_db with userId reference
5. Both records are now linked
```

### Database Structure

**auth_db.user:**
```
id | email              | password (hashed) | role_id
1  | john.doe@mail.com | $2a$10$...       | 2
```

**hr_db.employee:**
```
id | userId | firstName | lastName | email             | department  | jobTitle            | salary
1  | 1      | John      | Doe      | john.doe@mail.com | Engineering | Software Developer  | 75000.00
```

---

## Testing with Postman/cURL

### Example Request:
```bash
curl -X POST http://localhost:8002/api/internal/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jane.smith@example.com",
    "password": "secure123",
    "role": "EMPLOYEE",
    "firstName": "Jane",
    "lastName": "Smith",
    "department": "Marketing",
    "jobTitle": "Marketing Manager",
    "salary": 65000.00
  }'
```

### Expected Response:
```json
{
  "id": 1,
  "email": "jane.smith@example.com",
  "role": "EMPLOYEE"
}
```

---

## Important Notes

1. **Required Fields for User + Employee:**
   - `email`, `password`, `role` (always required)
   - `firstName`, `lastName` (triggers employee creation)
   - `department`, `jobTitle`, `salary` (optional but recommended)

2. **Role Must Exist:**
   - Make sure the role exists in the `auth_db.roles` table
   - Common roles: `ADMIN`, `EMPLOYEE`, `MANAGER`

3. **Error Handling:**
   - If employee creation fails, the user will still be created
   - Check logs for any HR service errors

4. **Services Must Be Running:**
   - Eureka Server (port 8000)
   - Auth Service (port 8002)
   - HR Service (port 8003)

---

## Verification

### Check User Created:
```sql
SELECT * FROM auth_db.user WHERE email = 'jane.smith@example.com';
```

### Check Employee Created:
```sql
SELECT * FROM hr_db.employee WHERE email = 'jane.smith@example.com';
```

### Check Link:
```sql
SELECT u.id as user_id, u.email, e.id as employee_id, e.firstName, e.lastName
FROM auth_db.user u
LEFT JOIN hr_db.employee e ON e.userId = u.id
WHERE u.email = 'jane.smith@example.com';
```

---

## Changes Made

### Auth Service:
1. ✅ Created `HrClient` (Feign client to call hr-service)
2. ✅ Added employee fields to `CreateUserRequest` DTO
3. ✅ Updated `UserService` to create employee via HrClient
4. ✅ Enabled `@EnableFeignClients` in `AuthServiceApplication`

### HR Service:
1. ✅ Created `/api/employees/internal` endpoint
2. ✅ Added `CreateEmployeeInternalRequest` DTO
3. ✅ Added `EmployeeResponse` DTO
4. ✅ Updated `EmployeeService` with `createEmployeeInternal()` method
5. ✅ Updated `EmployeeController` to expose internal endpoint

---

## Next Steps

1. **Rebuild Both Services:**
   ```bash
   cd auth-service
   mvn clean package
   
   cd ../hr-service
   mvn clean package
   ```

2. **Restart Services:**
   - Stop and restart both auth-service and hr-service
   - Make sure Eureka is running first

3. **Test the Integration:**
   - Use the example request above
   - Verify both database tables have the records
