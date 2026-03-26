# Spring Backend 개발 가이드

## 기술 스택
- **언어**: Java 17+
- **프레임워크**: Spring Boot 3.x
- **ORM**: Spring Data JPA / Hibernate
- **DB**: MySQL / PostgreSQL
- **보안**: Spring Security + JWT
- **빌드**: Gradle
- **테스트**: JUnit 5, Mockito

---

## 프로젝트 구조
```
src/main/java/com/example/
├── domain/
│   ├── user/
│   │   ├── controller/   # API 엔드포인트
│   │   ├── service/      # 비즈니스 로직
│   │   ├── repository/   # DB 접근
│   │   ├── entity/       # JPA 엔티티
│   │   └── dto/          # 요청/응답 객체
│   └── ...
├── global/
│   ├── config/           # 설정 클래스
│   ├── exception/        # 공통 예외 처리
│   ├── response/         # 공통 응답 형식
│   └── security/         # 인증/인가
└── Application.java
```

---

## 공통 응답 형식
```java
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "success", data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
```

---

## 예외 처리
```java
// 커스텀 예외
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}

// 전역 예외 핸들러
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
        return ResponseEntity
            .status(e.getErrorCode().getStatus())
            .body(ApiResponse.error(e.getErrorCode().getMessage()));
    }
}
```

---

## JPA 엔티티 기본 구조
```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
```

---

## JWT 인증 흐름
```
1. POST /api/auth/login  → AccessToken + RefreshToken 발급
2. 요청 헤더: Authorization: Bearer {accessToken}
3. AccessToken 만료 시 → POST /api/auth/refresh 로 재발급
4. RefreshToken 만료 시 → 재로그인 필요
```

---

## API 설계 규칙
| 동작 | HTTP Method | 예시 |
|------|------------|------|
| 목록 조회 | GET | `/api/users` |
| 단건 조회 | GET | `/api/users/{id}` |
| 생성 | POST | `/api/users` |
| 수정 | PUT / PATCH | `/api/users/{id}` |
| 삭제 | DELETE | `/api/users/{id}` |

- URL은 소문자 + 하이픈(`-`) 사용
- 복수형 명사 사용
- 버전 관리: `/api/v1/...`

---

## application.yml 기본 설정
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/dbname?serverTimezone=Asia/Seoul
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: validate  # 운영: validate, 개발: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

jwt:
  secret: your-secret-key
  access-expiration: 1800000    # 30분
  refresh-expiration: 604800000 # 7일
```

---

## 자주 쓰는 Gradle 명령어
```bash
./gradlew build          # 빌드
./gradlew bootRun        # 실행
./gradlew test           # 테스트
./gradlew clean build    # 클린 빌드
```

---

## 체크리스트
- [ ] 환경변수 분리 (.env / application-local.yml)
- [ ] DTO ↔ Entity 변환 로직 분리
- [ ] 비밀번호 BCrypt 암호화
- [ ] API 응답 형식 통일
- [ ] 전역 예외 처리 적용
- [ ] CORS 설정
- [ ] Swagger / SpringDoc 문서화
