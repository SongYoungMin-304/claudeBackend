# Backend CLAUDE.md

## 프로젝트 정보

- **프로젝트 이름**: Backend
- **프레임워크**: Spring Boot 3.x
- **언어**: Kotlin
- **빌드 도구**: Gradle
- **Java 버전**: 17+

## 프로젝트 구조

```
src/
├── main/kotlin/com/project/backend/
│   ├── BackendApplication.kt (진입점)
│   ├── domain/
│   │   └── [feature명]/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       └── entity/
│   └── global/
│       └── config/
└── test/kotlin/
```

## 코딩 컨벤션

- Kotlin 공식 스타일 가이드 준수
- 파일명은 PascalCase (예: HelloController.kt)
- 클래스/인터페이스는 PascalCase
- 함수/변수는 camelCase
- 상수는 UPPER_SNAKE_CASE

## API 개발 규칙

- RESTful 원칙 준수
- 요청/응답은 JSON
- HTTP 상태코드 올바르게 사용
- 에러 응답은 일관된 형식 사용

## 테스트

- `src/test/kotlin` 아래에 테스트 작성
- 테스트 클래스명: `[클래스명]Test`
- 테스트 실행: `./gradlew test`

## 빌드 및 실행

- 빌드: `./gradlew build`
- 실행: `./gradlew bootRun`

## 주요 의존성

- Spring Boot Web
- Spring Data JPA
- Kotlin Stdlib
- Springdoc OpenAPI (Swagger)
