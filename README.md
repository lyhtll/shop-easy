# ShopEasy - 온라인 쇼핑몰 주문·결제 플랫폼

Spring Boot 기반의 RESTful API 서버입니다. 회원 관리, 상품 관리, 주문, 결제(Toss Payments 연동) 기능을 제공합니다.

## 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 4.0.6 |
| Security | Spring Security + JWT (jjwt 0.12.6) |
| ORM | Spring Data JPA + QueryDSL 5.1.0 |
| DB | H2 (개발) / PostgreSQL (운영) |
| Cache | Redis 7 |
| Migration | Flyway |
| 결제 | Toss Payments REST API |
| 배포 | Docker / docker-compose |
| API 명세 | Swagger (springdoc-openapi 2.8.0) |

---

## 테스트 웹 UI

앱 실행 후 **http://localhost:8080** 에 접속하면 쇼핑몰 웹 화면을 바로 사용할 수 있습니다.

| 기능 | 설명 |
|------|------|
| 상품 목록 | 카테고리·키워드·가격 범위 필터 + 페이지네이션 |
| 회원가입 / 로그인 | JWT 발급 및 자동 저장 (AccessToken 만료 시 자동 재발급) |
| 장바구니 | 수량 조절, 담기·삭제 |
| 주문 | 장바구니 → 주문 생성 |
| 결제 | Toss Payments 실결제 연동 (카드 결제 가능) |
| 주문 내역 | 상태 확인 (PENDING / PAID / REFUNDED / CANCELLED), 주문 취소 |

> Swagger보다 전체 흐름(상품 조회 → 주문 → 결제)을 한 번에 확인하기 용이합니다.

### Toss Payments 테스트 키

결제 기능은 Toss Payments **테스트 키**가 `application.yml`에 기본값으로 설정되어 있어 별도 환경변수 없이 바로 사용 가능합니다.

| 구분 | 키 |
|------|----|
| Secret Key (서버) | `test_sk_XLkKEypNArW4l5beDdj8lmeaxYG5` |
| Client Key (웹 UI) | `test_ck_Lex6BJGQOVDBb1pd6ma8W4w2zNbg` |

> 테스트 결제 시 카드번호(mastercard) `5555 5555 5555 4444`, 유효기간 `02/31` (Toss 테스트 환경 전용 카드번호)

---

## 실행 방법

### 방법 1 — Docker Compose (권장)

```bash
docker compose up --build
```

- 별도 설치 없이 Spring Boot 앱 + Redis가 함께 실행됩니다.
- H2 인메모리 DB를 사용하므로 PostgreSQL이 필요하지 않습니다.
- 앱 기동 시 테스트 데이터가 자동으로 삽입됩니다.

| URL | 설명 |
|-----|------|
| http://localhost:8080/swagger-ui.html | Swagger UI |
| http://localhost:8080/h2-console | H2 콘솔 (JDBC URL: `jdbc:h2:mem:shopeasy`) |

---

### 방법 2 — 로컬 직접 실행

**사전 요구 사항:** JDK 21, Redis

```bash
# Redis 실행 (Docker 사용 시)
docker run -d -p 6379:6379 redis:7-alpine

# 앱 실행
./gradlew bootRun
```

---

## 테스트 계정

앱 기동 시 아래 계정이 자동으로 생성됩니다.

| 이메일 | 비밀번호 | 역할 |
|--------|----------|------|
| admin@shop.com | Admin1234! | ADMIN |
| user@shop.com | User1234! | USER |
| user2@shop.com | User1234! | USER |

> **ADMIN** 계정은 상품 등록·수정·삭제 권한을 가집니다.

---

## 테스트 데이터 (자동 생성)

앱 시작 시 아래 상품 데이터가 자동으로 추가됩니다.

| 상품명 | 카테고리 | 가격 | 재고 |
|--------|----------|------|------|
| 갤럭시 S25 Ultra | ELECTRONICS | 1,599,000원 | 50 |
| 애플 아이폰 16 Pro | ELECTRONICS | 1,550,000원 | 30 |
| 나이키 에어맥스 270 | SPORTS | 149,000원 | 100 |
| 클린 코드 | BOOKS | 33,000원 | 200 |
| 린넨 오버사이즈 셔츠 | CLOTHING | 45,000원 | 80 |
| 제주 감귤 2kg | FOOD | 18,000원 | 150 |
| 로지텍 MX Master 3 | ELECTRONICS | 119,000원 | 60 |
| 요가 매트 6mm | SPORTS | 29,000원 | 120 |
| 에센셜 오일 라벤더 | BEAUTY | 15,000원 | 90 |
| 원목 책상 1200x600 | HOME | 250,000원 | 20 |

---

## API 명세

전체 API 명세는 Swagger UI에서 확인할 수 있습니다: http://localhost:8080/swagger-ui.html

### 주요 엔드포인트 요약

#### 인증 (`/api/auth`)
| 메서드 | 경로 | 설명 | 인증 |
|--------|------|------|------|
| POST | `/api/auth/signup` | 회원가입 | 불필요 |
| POST | `/api/auth/login` | 로그인 (AccessToken + RefreshToken 발급) | 불필요 |
| POST | `/api/auth/reissue` | 토큰 재발급 (Rotation 전략) | 불필요 |
| POST | `/api/auth/logout` | 로그아웃 (Redis RefreshToken 삭제) | 필요 |

#### 회원 (`/api/users`)
| 메서드 | 경로 | 설명 | 인증 |
|--------|------|------|------|
| GET | `/api/users/me` | 내 정보 조회 | 필요 |
| PATCH | `/api/users/me` | 내 정보 수정 | 필요 |

#### 상품 (`/api/products`)
| 메서드 | 경로 | 설명 | 인증 |
|--------|------|------|------|
| GET | `/api/products` | 상품 목록 조회 (동적 검색 + 페이징) | 불필요 |
| GET | `/api/products/{id}` | 상품 상세 조회 | 불필요 |
| POST | `/api/products` | 상품 등록 | ADMIN |
| PATCH | `/api/products/{id}` | 상품 수정 | ADMIN |
| DELETE | `/api/products/{id}` | 상품 삭제 | ADMIN |

#### 주문 (`/api/orders`)
| 메서드 | 경로 | 설명 | 인증 |
|--------|------|------|------|
| POST | `/api/orders` | 주문 생성 | 필요 |
| GET | `/api/orders` | 내 주문 목록 조회 | 필요 |
| GET | `/api/orders/{id}` | 주문 상세 조회 | 필요 |
| DELETE | `/api/orders/{id}` | 주문 취소 (PENDING 상태만 가능) | 필요 |

#### 결제 (`/api/payments`)
| 메서드 | 경로 | 설명 | 인증 |
|--------|------|------|------|
| POST | `/api/payments/confirm` | 결제 승인 (Toss Payments) | 필요 |
| POST | `/api/payments/{id}/cancel` | 결제 취소·환불 | 필요 |
| POST | `/api/payments/webhook` | Toss Payments 웹훅 수신 | 불필요 |

---

## JWT 인증 방식

1. `/api/auth/login`으로 로그인 후 `accessToken`을 발급받습니다.
2. 이후 요청의 `Authorization` 헤더에 `Bearer {accessToken}` 형식으로 포함합니다.
3. AccessToken 만료(30분) 시 `refreshToken`으로 `/api/auth/reissue`를 호출해 재발급합니다.

---

## 주문 상태 흐름

```
PENDING (주문 대기)
    ├── → PAID      (결제 완료)
    │         └── → REFUNDED (환불 완료)
    └── → CANCELLED (주문 취소, PENDING 상태에서만 가능)
```

---

## 상품 검색 파라미터

`GET /api/products` 엔드포인트는 아래 쿼리 파라미터를 지원합니다.

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `keyword` | String | 상품명 키워드 검색 (부분 일치) |
| `category` | Enum | 카테고리 필터 (ELECTRONICS, CLOTHING, FOOD, BOOKS, SPORTS, HOME, BEAUTY, OTHER) |
| `minPrice` | BigDecimal | 최소 가격 필터 |
| `maxPrice` | BigDecimal | 최대 가격 필터 |
| `page` | int | 페이지 번호 (0부터 시작, 기본값: 0) |
| `size` | int | 페이지 크기 (기본값: 20) |

---

## 프로젝트 구조

```
src/main/java/com/yunha/shopeasy
├── domain
│   ├── auth       # 인증 (회원가입, 로그인, 재발급, 로그아웃)
│   ├── user       # 회원 (내 정보 조회·수정)
│   ├── product    # 상품 (CRUD, QueryDSL 동적 검색)
│   ├── order      # 주문 (생성, 조회, 취소, 상태 머신)
│   └── payment    # 결제 (Toss Payments 연동, 웹훅)
└── global
    ├── common     # BaseEntity, BaseResponse
    ├── config     # JPA, Redis, QueryDSL, Web, DataInitializer
    ├── error      # GlobalExceptionHandler, CustomException
    └── security   # JWT Filter, Provider, SecurityConfig
```