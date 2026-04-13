

# API Request Response
- POST: http://localhost:9092/api/v1/auth/register
```shell
{
    "fullName": "Rahul Sharma",
    "email": "rahul.sharma@example.com",
    "password": "StrongPass@123",
    "phone": "+919876543210",
    "role": "ROLE_USER",
    "verified": false
}
```

```shell
{
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWh1bC5zaGFybWFAZXhhbXBsZS5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwidXNlcklkIjoyLCJpYXQiOjE3NzYwNjk2MTUsImV4cCI6MTc3NjE1NjAxNX0.ksHPbi3n_rL4hNyqH8apiKnRgDZ9I-l15NXdb0MgVUE",
        "message": "Registration Successful",
        "title": "Welcome back, rahul.sharma@example.com!",
        "user": {
            "id": 2,
            "fullName": "Rahul Sharma",
            "email": "rahul.sharma@example.com",
            "password": null,
            "phone": "+919876543210",
            "role": "ROLE_USER",
            "verified": false,
            "lastLogined": null,
            "createdAt": null,
            "updatedAt": null
        }
    },
    "message": "User registered successfully",
    "success": true,
    "timestamp": "2026-04-13T14:10:15.639296"
}
```

---
- POST: http://localhost:9092/api/v1/auth/login
```shell
{
    "email": "rahul.sharma@example.com",
    "password": "StrongPass@123"
}
```

Response -Same as above


---
- GET: request 'http://localhost:9092/api/v1/users/me' \
--header 'Authorization: Bearer eyJ2----------0MgVUE'
Response -Same as above
---
- GET : request 'http://localhost:9092/api/v1/users' \
  --header 'Authorization: ••••••' \
  --auth-bearer-token 