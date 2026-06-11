# cấu trúc thư mục cho demo tấn công 
```
├── .mvn
│   └── wrapper
│       └── maven-wrapper.properties
├── src
│   └── main
│       ├── java
│       │   └── vn
│       │       └── edu
│       │           └── ut
│       │               └── Bao_mat_API
│       │                   ├── config
│       │                   │   └── SwaggerConfig.java
│       │                   ├── controller
│       │                   │   ├── AuthController.java
│       │                   │   ├── BookController.java
│       │                   │   ├── CommentController.java
│       │                   │   └── UserController.java
│       │                   ├── dto
│       │                   │   ├── request
│       │                   │   │   ├── BookRequest.java
│       │                   │   │   ├── CommentRequest.java
│       │                   │   │   ├── LoginRequest.java
│       │                   │   │   └── RegisterRequest.java
│       │                   │   └── response
│       │                   │       ├── AuthResponse.java
│       │                   │       ├── BookResponse.java
│       │                   │       └── CommentResponse.java
│       │                   ├── entity
│       │                   │   ├── Book.java
│       │                   │   ├── Comment.java
│       │                   │   └── User.java
│       │                   ├── repository
│       │                   │   ├── BookRepository.java
│       │                   │   ├── CommentRepository.java
│       │                   │   └── UserRepository.java
│       │                   ├── security
│       │                   │   ├── JwtAuthFilter.java
│       │                   │   ├── JwtUtils.java
│       │                   │   └── SecurityConfig.java
│       │                   ├── service
│       │                   │   ├── AuthService.java
│       │                   │   ├── BookService.java
│       │                   │   └── CommentService.java
│       │                   └── BaoMatApiApplication.java
│       └── resources
│           ├── static
│           ├── templates
│           ├── application.properties
│           └── data.sql
├── .gitattributes
├── .gitignore
├── DEMO_GUIDE.md
├── HELP.md
├── mvnw
├── mvnw.cmd
└── pom.xml
```
