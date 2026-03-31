# BACKEND KNOWLEDGE BASE

**Stack:** Kotlin 2.2.21 + Spring Boot 4.0.5 + Java 17

## OVERVIEW

Spring Boot REST API with domain-driven structure, JPA/H2 persistence, and OpenAPI documentation.

## STRUCTURE

```
src/main/kotlin/com/project/backend/
├── BackendApplication.kt     # Entry point
├── domain/
│   ├── hello/controller/
│   ├── user/
│   │   ├── controller/
│   │   ├── entity/
│   │   └── service/
│   └── post/
│       ├── controller/
│       ├── entity/
│       ├── repository/
│       └── service/
└── global/
    └── config/               # CorsConfig, SwaggerConfig
```

## WHERE TO LOOK

| Component | Path | Notes |
|-----------|------|-------|
| Entry | `BackendApplication.kt` | Main class |
| User CRUD | `domain/user/*` | Entity, controller, service |
| Post CRUD | `domain/post/*` | Entity, repository, service, controller |
| CORS | `global/config/CorsConfig.kt` | Already configured for frontend:3000 |
| Swagger | `global/config/SwaggerConfig.kt` | `/swagger-ui/index.html` |

## CODE MAP

### Key Classes

| Class | Type | Location | Purpose |
|-------|------|----------|---------|
| `User` | Entity | `domain/user/entity/` | JPA entity |
| `UserController` | Controller | `domain/user/controller/` | User REST endpoints |
| `UserService` | Service | `domain/user/service/` | User business logic |
| `Post` | Entity | `domain/post/entity/` | JPA entity |
| `PostController` | Controller | `domain/post/controller/` | Post REST endpoints |
| `PostService` | Service | `domain/post/service/` | Post business logic |
| `PostRepository` | Repository | `domain/post/repository/` | JPA repository |
| `CorsConfig` | Config | `global/config/` | CORS configuration |

## CONVENTIONS

- **Package:** `com.project.backend`
- **Naming:** PascalCase files/classes, camelCase functions/variables
- **Controller:** Return `ResponseEntity<T>`
- **Entity:** JPA annotations, `@Id` auto-generated
- **Service:** `@Service`, inject via constructor

## COMMANDS

```bash
./gradlew bootRun      # Start on port 8080
./gradlew build        # Build JAR
./gradlew test         # Run tests
```

## DEPENDENCIES

- `spring-boot-starter-web` - REST API
- `spring-boot-starter-data-jpa` - Database
- `spring-boot-starter-validation` - Bean validation
- `h2` - In-memory database
- `springdoc-openapi` - Swagger/OpenAPI

## ANTI-PATTERNS

- DO NOT add new CORS origins without updating `CorsConfig.kt`
- DO NOT bypass service layer (no logic in controllers)
- DO NOT use `var` for mutable state without justification
