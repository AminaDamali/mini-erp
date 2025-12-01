# Employee Update Feature - Documentation

## Overview
ADMIN and HR roles can now update employee information through the HR service API.

---

## API Endpoint

### Update Employee
```
PUT http://localhost:8003/api/employees/{id}
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

**Path Parameter:**
- `id` - The employee ID to update

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Smith",
  "department": "Engineering",
  "jobTitle": "Senior Developer",
  "salary": 85000.00
}
```

**Notes:**
- All fields are optional (partial updates supported)
- Only include fields you want to update
- Fields not provided will remain unchanged

---

## Response

### Success Response (200 OK)
```json
{
  "id": 1,
  "userId": 5,
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@example.com",
  "department": "Engineering",
  "jobTitle": "Senior Developer",
  "salary": 85000.00
}
```

### Error Responses

**404 Not Found** - Employee doesn't exist
```json
{
  "error": "Employee not found with ID: 123"
}
```

**403 Forbidden** - User doesn't have ADMIN or HR role
```json
{
  "error": "Access Denied"
}
```

**401 Unauthorized** - No valid JWT token provided
```json
{
  "error": "Unauthorized"
}
```

---

## Security

### Role-Based Access Control
- **Allowed Roles:** `ADMIN`, `HR`
- **Denied Roles:** `EMPLOYEE`, others
- **Authentication:** Required (JWT token in Authorization header)

### Security Configuration
```java
.requestMatchers("/api/employees/**").hasAnyRole("ADMIN", "HR")
```

---

## Testing Examples

### Test 1: Update Employee as ADMIN ✅

**Step 1: Login as ADMIN**
```bash
POST http://localhost:8002/api/auth/login
Content-Type: application/json

{
  "email": "admin@test.com",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Step 2: Update Employee**
```bash
PUT http://localhost:8003/api/employees/1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "salary": 90000.00,
  "jobTitle": "Lead Developer"
}
```

**Expected Result:** `200 OK` with updated employee data

---

### Test 2: Update Employee as HR ✅

**Step 1: Login as HR**
```bash
POST http://localhost:8002/api/auth/login
Content-Type: application/json

{
  "email": "hr@test.com",
  "password": "hr123"
}
```

**Step 2: Update Employee**
```bash
PUT http://localhost:8003/api/employees/1
Authorization: Bearer <hr_token>
Content-Type: application/json

{
  "department": "Marketing",
  "jobTitle": "Marketing Specialist"
}
```

**Expected Result:** `200 OK` with updated employee data

---

### Test 3: Update Employee as EMPLOYEE ❌

**Step 1: Login as EMPLOYEE**
```bash
POST http://localhost:8002/api/auth/login
Content-Type: application/json

{
  "email": "employee@test.com",
  "password": "employee123"
}
```

**Step 2: Try to Update Employee**
```bash
PUT http://localhost:8003/api/employees/1
Authorization: Bearer <employee_token>
Content-Type: application/json

{
  "salary": 100000.00
}
```

**Expected Result:** `403 Forbidden` - Access Denied

---

## Partial Update Example

You can update just one field without affecting others:

**Update only salary:**
```json
{
  "salary": 75000.00
}
```

**Update only department and job title:**
```json
{
  "department": "Sales",
  "jobTitle": "Sales Manager"
}
```

---

## Changes Made

### 1. **EmployeeUpdateRequest DTO** ✅
**File:** `hr-service/src/main/java/com/merp/hrservice/dto/EmployeeUpdateRequest.java`

```java
@Data
public class EmployeeUpdateRequest {
    private String firstName;
    private String lastName;
    private String department;
    private String jobTitle;
    private Double salary;
}
```

- All fields are optional for partial updates
- Removed `id` field (now in path parameter)

---

### 2. **EmployeeUpdateResponse DTO** ✅
**File:** `hr-service/src/main/java/com/merp/hrservice/dto/EmployeeUpdateResponse.java`

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String jobTitle;
    private Double salary;
}
```

- Returns complete employee information after update
- Includes all relevant fields

---

### 3. **EmployeeService** ✅
**File:** `hr-service/src/main/java/com/merp/hrservice/service/EmployeeService.java`

```java
public EmployeeUpdateResponse updateEmployee(long id, EmployeeUpdateRequest request) {
    Employee emp = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

    // Update only non-null fields (partial update)
    if (request.getFirstName() != null) emp.setFirstName(request.getFirstName());
    if (request.getLastName() != null) emp.setLastName(request.getLastName());
    if (request.getDepartment() != null) emp.setDepartment(request.getDepartment());
    if (request.getJobTitle() != null) emp.setJobTitle(request.getJobTitle());
    if (request.getSalary() != null) emp.setSalary(request.getSalary());

    Employee saved = employeeRepository.save(emp);
    
    return new EmployeeUpdateResponse(...);
}
```

- Checks if employee exists before updating
- Only updates fields that are provided (not null)
- Returns properly formatted response DTO

---

### 4. **EmployeeController** ✅
**File:** `hr-service/src/main/java/com/merp/hrservice/controller/EmployeeController.java`

```java
@PutMapping("/{id}")
public EmployeeUpdateResponse updateEmployee(
        @PathVariable Long id, 
        @RequestBody EmployeeUpdateRequest request) {
    return employeeService.updateEmployee(id, request);
}
```

- Uses `@PutMapping` with path variable for employee ID
- Accepts `EmployeeUpdateRequest` in request body
- Returns `EmployeeUpdateResponse`

---

### 5. **SecurityConfig** ✅
**File:** `hr-service/src/main/java/com/merp/hrservice/config/SecurityConfig.java`

```java
.requestMatchers("/api/employees/**").hasAnyRole("ADMIN", "HR")
```

- Updated from `hasRole("ADMIN")` to `hasAnyRole("ADMIN", "HR")`
- Both ADMIN and HR can now manage employees

---

## Database Verification

After updating an employee, verify in the database:

```sql
-- Check employee was updated
SELECT * FROM hr_db.employee WHERE id = 1;

-- Verify specific fields
SELECT id, firstName, lastName, department, jobTitle, salary 
FROM hr_db.employee 
WHERE id = 1;
```

---

## Next Steps

1. **Rebuild hr-service:**
   ```bash
   cd hr-service
   mvn clean package
   ```

2. **Restart hr-service**

3. **Test the update functionality:**
   - Login as ADMIN or HR
   - Get the JWT token
   - Call PUT endpoint with employee ID and update data
   - Verify the response and database

4. **Verify access control:**
   - Try with EMPLOYEE role (should fail with 403)
   - Try without token (should fail with 401)

---

## Common Issues

### Issue 1: 403 Forbidden
**Cause:** User doesn't have ADMIN or HR role  
**Solution:** Login with ADMIN or HR account

### Issue 2: 404 Not Found
**Cause:** Employee ID doesn't exist  
**Solution:** Use a valid employee ID from the database

### Issue 3: 401 Unauthorized
**Cause:** Missing or invalid JWT token  
**Solution:** Include valid JWT token in Authorization header

### Issue 4: Fields not updating
**Cause:** Fields not included in request body  
**Solution:** Make sure to include the fields you want to update

---

## Postman Collection Example

```json
{
  "name": "Update Employee",
  "request": {
    "method": "PUT",
    "header": [
      {
        "key": "Authorization",
        "value": "Bearer {{jwt_token}}",
        "type": "text"
      },
      {
        "key": "Content-Type",
        "value": "application/json",
        "type": "text"
      }
    ],
    "body": {
      "mode": "raw",
      "raw": "{\n  \"firstName\": \"Jane\",\n  \"lastName\": \"Doe\",\n  \"department\": \"IT\",\n  \"jobTitle\": \"Software Engineer\",\n  \"salary\": 80000.00\n}"
    },
    "url": {
      "raw": "http://localhost:8003/api/employees/1",
      "protocol": "http",
      "host": ["localhost"],
      "port": "8003",
      "path": ["api", "employees", "1"]
    }
  }
}
```
