<img width="3117" height="1351" alt="the front cover" src="https://github.com/user-attachments/assets/6503429f-03dc-4800-a9c0-9efa72280a66" />

<p align="center">
  <!--
  <a href="https://wealthy-session-98c.notion.site/pida-1b490c66759880519df7f9c93c1ed2dd?pvs=4">
    <img src="https://img.shields.io/badge/소개페이지-blue?style=for-the-badge&logo=notion&logoColor=white" alt="소개페이지" />
  </a>
  -->
  <a href="https://www.instagram.com/sseu.dami/">
    <img src="https://img.shields.io/badge/@_sseu.dami-13BAFF?style=for-the-badge&logo=instagram&logoColor=white" alt="@_sseu.dami" />
  </a>
  <a href="https://github.com/Team-SSEUDAM/Sseudam-iOS">
    <img src="https://img.shields.io/badge/Sseudam--iOS-000000?style=for-the-badge&logo=github&logoColor=white" alt="Sseudam-Server" />
  </a>
</p>

# 쓰담 (Sseudam)

<img width="130" height="130" alt="AppIcon" src="https://github.com/user-attachments/assets/c8d8d6b1-8b0c-4e53-b05e-370eacec0c31" />

> **위치기반 환경 서비스 - 쓰레기통을 찾고, 제안하고, 펫과 함께 성장하세요**

쓰담은 사용자들이 주변의 쓰레기통 위치를 쉽게 찾고, 새로운 쓰레기통을 제안하며, 환경 보호 활동을 통해 가상 펫을 키우는 위치기반 커뮤니티 플랫폼입니다. PostGIS 기반의 공간 데이터베이스와 Spring Modulith 아키텍처로 구현된 확장 가능한 모듈형 모노리스 백엔드 시스템입니다.

<br>

## 🐱 주요 기능

|<img width="300" alt="screenshot3" src="https://github.com/user-attachments/assets/66a52426-1ecc-45e8-8a94-0adf147ab732" />|<img width="300" alt="screenshot4" src="https://github.com/user-attachments/assets/d2dd86ec-b542-4da5-b09f-a9a711ca7c18" />|<img width="300" alt="screenshot5" src="https://github.com/user-attachments/assets/bf42fe23-44a6-41cd-83ee-c96d63fba481" />|
|:----:|:----:|:----:|

### 📍 위치기반 쓰레기통 검색
- **PostGIS 공간 인덱싱**을 활용한 실시간 주변 쓰레기통 검색
- 쓰레기 종류별 필터링 (일반, 재활용 등)
- 지역별 카테고리 지원 (전국 주요 도시)
- Bounding Box 기반 효율적인 지도 뷰 쿼리

### 💡 커뮤니티 제안 및 신고 시스템
- **새 쓰레기통 제안**: 사진과 함께 신규 위치 등록 요청
- **문제 신고**: 기존 쓰레기통의 오류 정보 보고 (위치 오류, 종류 오류, 부재 등)
- 관리자 승인/거부 워크플로우
- S3 Presigned URL을 통한 직접 이미지 업로드
- Discord 웹훅을 통한 관리자 실시간 알림

### 🎮 게이미피케이션 펫 시스템
- **월간 펫 시즌**: 매월 새로운 펫 타입과 성장 시스템
- **포인트 기반 레벨링**: 활동(방문, 제안, 신고, 출석)에 따른 포인트 적립
- **5단계 성장 시스템**: LEVEL_1(1-99점) → SPECIAL(1000점+)
- 펫 진화 히스토리 추적 및 성장 기록
- Spring Modulith 이벤트 기반 비동기 포인트 처리

### ✅ 사용자 참여 트래킹
- **일일 출석 체크**: 연속 출석 스트릭 관리
- **스팟 방문 기록**: 하루 1회 방문 제한 및 히스토리 관리
- **포인트 히스토리**: 모든 포인트 적립/차감 내역 추적
- Firebase FCM을 통한 실시간 푸시 알림

### 🔐 인증 및 보안
- OAuth 2.0 소셜 로그인 (Google, Apple 등)
- RSA 기반 JWT 토큰 인증 (비대칭키 서명)
- Redis 기반 리프레시 토큰 관리
- Role 기반 접근 제어 (USER, ADMIN)

<br>

## 🏗️ 시스템 아키텍처

### 아키텍처 개요

쓰담은 **Modular Monolith** 아키텍처를 채택하여 마이크로서비스의 장점(모듈 독립성, 명확한 경계)과 모노리스의 장점(배포 단순성, 트랜잭션 일관성)을 결합했습니다.

**핵심 설계 원칙:**
- **Clean Architecture**: 도메인 중심 설계, Infrastructure 의존성 역전
- **Domain-Driven Design**: 명확한 Bounded Context 분리 (User, TrashSpot, Pet, Suggestion, Report 등)
- **Event-Driven**: Spring Modulith를 활용한 모듈 간 비동기 통신
- **Architecture Testing**: 모듈 경계 및 의존성 규칙 자동 검증

### 모듈 구조

```
📦 Sseudam-Server
├── 🎯 sseudam-core/                    # 핵심 비즈니스 로직 (Clean Architecture)
│   ├── core-domain/                     # 도메인 모델 (순수 Kotlin POJO, 프레임워크 독립)
│   │   └── [User, TrashSpot, Pet, Visit, Attendance, Suggestion, Report]
│   ├── core-application/                # 유스케이스 및 비즈니스 로직
│   │   ├── *Service                     # 도메인 서비스 (비즈니스 로직 오케스트레이션)
│   │   ├── *Facade                      # 파사드 (크로스 도메인 조정)
│   │   ├── *Reader/Appender/Updater     # CQRS-Light 패턴 (읽기/쓰기 분리)
│   │   └── *Validator                   # 비즈니스 규칙 검증
│   ├── core-contract/                   # API 계약 (DTO, Response, Exception)
│   └── core-api/                        # REST API 컨트롤러 (인프라스트럭처 레이어)
│
├── 💾 sseudam-storage/                  # 영속성 레이어 (Ports 구현)
│   ├── db-core/                         # JPA 엔티티 및 리포지토리 구현
│   │   ├── PostgreSQL + PostGIS         # 공간 데이터베이스
│   │   └── Kotlin JDSL                  # 타입 안전 쿼리 DSL
│   └── redis/                           # 캐싱 레이어
│       └── Redisson 기반 분산 캐시
│
├── 🔌 sseudam-clients/                  # 외부 서비스 연동 (Adapters)
│   ├── aws/                             # AWS S3 이미지 스토리지
│   ├── notification/                    # Firebase FCM + Discord 웹훅
│   └── oauth-client/                    # OAuth 2.0 Provider 연동
│
├── 🛠️ sseudam-supports/                 # 횡단 관심사 (Cross-Cutting Concerns)
│   ├── logging/                         # 구조화된 로깅 (Logback)
│   ├── monitoring/                      # 관측성 (Prometheus Metrics, Sentry)
│   └── swagger/                         # API 문서화 (OpenAPI 3.0)
│
├── 🔄 sseudam-batch/                    # 배치 작업
│   └── 월간 펫 시즌 롤오버, 알림 스케줄링
│
├── 👨‍💼 sseudam-admin/                     # 관리자 기능 (별도 Bounded Context)
│   └── 제안/신고 승인, 사용자 관리, 푸시 알림 발송
│
└── 🧪 sseudam-tests/                    # 테스트 인프라
    ├── api-docs/                        # Spring REST Docs 테스트
    ├── test-container/                  # TestContainers (PostgreSQL, LocalStack)
    └── test-helper/                     # 테스트 픽스처 및 유틸리티
```

### 계층별 의존성 흐름

```
Presentation (core-api)
        ↓
Application (core-application)
        ↓
Domain (core-domain)  ← 의존성 방향
        ↑
Infrastructure (storage, clients)
```

- **도메인 레이어**는 어떤 외부 레이어에도 의존하지 않음 (순수 비즈니스 로직)
- **애플리케이션 레이어**는 Repository 인터페이스를 정의 (포트)
- **인프라 레이어**는 이를 구현 (어댑터)

### 모듈 경계 검증

**Spring Modulith**를 활용한 자동화된 아키텍처 규칙 검증:

```shell
# 모듈 경계 검증 테스트
./gradlew :sseudam-core:core-api:test --tests "*ModulithArchitectureTest"
```

```kotlin
// 컴파일 타임 + 테스트 타임 모듈 경계 검증
ApplicationModules.of(SseudamApplication::class.java).verify()
```

**검증 규칙:**
- 모듈 내부 패키지(`internal`)는 외부에서 접근 불가
- 모듈 간 순환 의존성 금지
- 이벤트 리스너는 `@ApplicationModuleListener`로만 등록
- 공개 API는 모듈 루트 패키지에만 배치

**위반 시 빌드 실패:**
- 잘못된 패키지 접근 → `AssertionError`
- 순환 의존성 탐지 → `Cycles detected`
- 이벤트 처리 오류 → 컴파일 에러

이를 통해 **아키텍처 부채를 사전에 방지**하고 팀 전체가 일관된 구조를 유지할 수 있습니다.

<br>

## ⚙️ 기술 스택

### Backend Framework
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

- **Kotlin 1.9+ with JDK 21**: 최신 Kotlin 기능 활용 (데이터 클래스, sealed class, extension)
- **Spring Boot 3.x**: Virtual Threads로 향상된 동시성 처리
- **Spring Modulith**: 이벤트 기반 모듈 간 통신, 모듈 경계 검증
- **Kotlin JDSL**: JPA를 위한 타입 안전 쿼리 DSL

### Database & Persistence
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis%207.2-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Hibernate Spatial](https://img.shields.io/badge/Hibernate%20Spatial-59666C?style=for-the-badge&logo=hibernate&logoColor=white)

- **PostgreSQL + PostGIS**: 공간 데이터베이스, 위치 기반 쿼리 최적화
- **Hibernate Spatial + JTS**: 지리공간 객체 매핑 및 연산
- **Redis with Redisson**: TTL 기반 분산 캐싱 (10초 ~ 6시간)
- **Flyway**: 버전 관리 데이터베이스 마이그레이션

### Cloud & Infrastructure
![AWS S3](https://img.shields.io/badge/AWS%20S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase%20FCM-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)

- **AWS S3**: Presigned URL 기반 이미지 업로드/저장
- **Firebase Cloud Messaging**: 크로스 플랫폼 푸시 알림
- **Docker + Jib**: 멀티 아키텍처 컨테이너 빌드 (amd64, arm64)
- **Docker Compose**: 로컬/개발/프로덕션 환경 오케스트레이션

### Monitoring & Observability
![Sentry](https://img.shields.io/badge/Sentry-362D59?style=for-the-badge&logo=sentry&logoColor=white)
![Prometheus](https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

- **Sentry**: 실시간 에러 추적 및 컨텍스트 기반 알림
- **Prometheus**: 메트릭 수집 (Spring Actuator 연동)
- **Swagger UI + Spring REST Docs**: 자동화된 API 문서 생성
- **Structured Logging**: 프로파일별 로그 레벨 관리

### Testing & Quality
![Kotest](https://img.shields.io/badge/Kotest-34A853?style=for-the-badge&logo=kotlin&logoColor=white)
![TestContainers](https://img.shields.io/badge/TestContainers-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![MockK](https://img.shields.io/badge/MockK-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)

- **Kotest**: BDD 스타일 테스트 프레임워크
- **TestContainers**: 실제 PostgreSQL/LocalStack 기반 통합 테스트
- **MockK**: Kotlin 친화적 모킹 라이브러리
- **Ktlint**: 코드 스타일 통일 (pre-commit hook 연동)

<br>

## 🎯 핵심 설계 패턴

### Application Layer Patterns

**Facade Pattern**
```kotlin
@Service
class TrashSpotFacade(
    private val trashSpotService: TrashSpotService,
    private val suggestionService: SuggestionService,
    private val userService: UserService
) {
    fun findDetails(spotId: Long): TrashSpot.Detail {
        // 여러 도메인 서비스를 조율하여 복잡한 뷰 생성
    }
}
```
- 크로스 도메인 오케스트레이션
- 복잡한 비즈니스 플로우 캡슐화

**Repository Pattern (Ports & Adapters)**
```kotlin
// Application Layer (Port)
interface TrashSpotRepository {
    fun findById(id: Long): TrashSpot?
}

// Storage Layer (Adapter)
class TrashSpotCoreRepository : TrashSpotRepository {
    // JPA/JDSL 구현 세부사항
}
```

**CQRS-Light Pattern**
- `Reader`: 조회 전용 컴포넌트 (캐시 활용)
- `Appender`: 생성 전용 컴포넌트
- `Updater`: 수정 전용 컴포넌트
- 읽기/쓰기 최적화를 위한 책임 분리

**Event-Driven Pattern (Spring Modulith)**
```kotlin
@ApplicationModuleListener
fun handleSpotVisit(event: SpotVisitedEvent) {
    petPointService.addPoints(event.userId, VISIT_POINTS)
    // 비동기 이벤트 처리로 모듈 간 결합도 감소
}
```

### Domain Patterns

**Value Object Pattern**
```kotlin
@Embeddable
data class Address(
    val city: String,
    val district: String,
    val street: String
) // 불변 값 객체로 도메인 개념 표현
```

**Soft Delete Pattern**
```kotlin
abstract class BaseEntity {
    var deletedAt: LocalDateTime? = null
    fun softDelete() { deletedAt = LocalDateTime.now() }
}
```

**Strategy Pattern** (Report Type Handling)
- `UpdateReportEmptySpotProvider`
- `UpdateReportKindProvider`
- `UpdateReportPointProvider`
- 각 신고 타입별 처리 로직 캡슐화

<br>

## 📊 데이터 플로우 예시

### 쓰레기통 방문 처리 플로우

```
1. 클라이언트 → POST /api/v1/visited
         ↓
2. VisitedController.visitSpot()
         ↓
3. SpotVisitedFacade.visitSpot()
         ↓
4. Validation: 금일 이미 방문했는지 확인
         ↓
5. SpotVisitedService.append() → DB INSERT
         ↓
6. Spring Modulith Event 발행: SpotVisitedEvent
         ↓
7. [비동기] PetEventListener.addPetPointHistory()
         ↓
8. 포인트 적립 + 레벨업 임계값 체크
         ↓
9. 레벨업 시: 펫 업데이트 + 히스토리 기록 + 캐시 무효화
         ↓
10. 응답 반환: 방문 확인 + 적립된 포인트
```

### 제안 승인 워크플로우

```
1. 사용자가 신규 쓰레기통 제안 (사진 포함)
         ↓
2. S3 Presigned URL 발급 → 클라이언트 직접 업로드
         ↓
3. 제안 저장 (status: WAITING)
         ↓
4. Discord 웹훅 → 관리자 알림
         ↓
5. 관리자 검토 → 승인/거부
         ↓
6. [승인 시]
   - TrashSpot 생성
   - 제안자에게 포인트 지급
   - 펫 레벨업 체크
   - FCM 푸시 알림
         ↓
7. [거부 시]
   - 거부 사유 저장
   - FCM 알림
```

<br>

## 🚀 주요 기술적 특징

### 1. 공간 데이터 처리

**PostGIS + Hibernate Spatial**을 활용한 효율적인 위치 기반 쿼리:

```kotlin
// Bounding Box 기반 쓰레기통 검색
fun findAllByLocationWithFilters(
    swLat: Double, swLng: Double,  // 남서쪽 좌표
    neLat: Double, neLng: Double,  // 북동쪽 좌표
    region: String?,
    trashType: String?
): List<TrashSpot>
```

- 공간 인덱스를 통한 빠른 검색
- 거리 계산 및 근접성 쿼리 지원

### 2. 캐싱 전략

**커스텀 캐싱 추상화**로 일관된 캐시 관리:

```kotlin
Cache.cache(
    ttl = 360L,  // 6시간
    key = "trash:spot:all",
    typeReference = object : TypeReference<List<TrashSpot.Info>>() {}
) {
    // 캐시 미스 시 실행되는 로직
    trashSpotReader.findAll()
}
```

**캐시 무효화 전략:**
- 데이터 변경 시 즉시 캐시 삭제
- TTL 기반 자동 만료 (데이터 휘발성에 따라 10초 ~ 6시간)
- Jackson TypeReference로 타입 안전 역직렬화

### 3. 보안 구현

**JWT with RSA 비대칭 키:**
```kotlin
@Bean
fun jwtDecoder(): JwtDecoder {
    return NimbusJwtDecoder
        .withPublicKey(rsaPublicKey)
        .build()
}
```

- Public/Private 키 페어로 토큰 서명
- Redis 기반 리프레시 토큰 저장
- 커스텀 JWT Converter로 Role 매핑
- 인증 이력 추적 (로그인 시간, 디바이스 정보)

### 4. 배치 처리

**월간 펫 시즌 자동 롤오버:**
```kotlin
@Scheduled(cron = "0 0 0 1 * *")  // 매월 1일 자정
fun createPetSeason() {
    userPetFacade.createBatchUserPet(year, month)
    notificationFacade.sendNewPetNotifications()
}
```

### 5. 이벤트 기반 아키텍처

**Spring Modulith Event Store**를 활용한 비동기 처리:

```kotlin
// 이벤트 발행
applicationEventPublisher.publishEvent(
    UserPetContextEvent(userId, PetPoint.Action.VISIT)
)

// 이벤트 리스닝 (별도 트랜잭션)
@ApplicationModuleListener
fun addUserPetPoint(event: UserPetContextEvent) {
    // 포인트 적립 로직
}
```

- 모듈 간 느슨한 결합
- 이벤트 영속화로 재처리 가능
- UPDATE 모드로 처리 완료 상태 추적

<br>

## 🧪 테스트 전략

### 테스트 분류

```kotlin
// 단위 테스트 (Spring Context 없이)
@Tag("unitTest")
class TrashSpotServiceTest : BehaviorSpec({
    given("쓰레기통 서비스에서") {
        `when`("특정 ID로 조회하면") {
            then("해당 쓰레기통을 반환한다") {
                // 순수 로직 테스트
            }
        }
    }
})

// 통합 테스트 (TestContainers)
@Tag("contextTest")
@Testcontainers
class TrashSpotRepositoryTest : BehaviorSpec({
    // 실제 PostgreSQL 컨테이너로 테스트
})

// API 문서 테스트 (Spring REST Docs)
@Tag("restDocsTest")
class TrashSpotApiDocTest {
    // API 호출 + 문서 자동 생성
}
```

### Spring Modulith 아키텍처 검증

**모듈 경계 및 의존성 자동 검증:**

```kotlin
@Tag("context")
@SpringBootTest
class ModulithArchitectureTest : BehaviorSpec({
    given("Spring Modulith 모듈 구조") {
        val modules = ApplicationModules.of(SseudamApplication::class.java)

        `when`("모듈 구조를 검증하면") {
            then("순환 의존성이 없어야 한다") {
                modules.verify()  // 자동으로 모듈 경계 검증
            }
        }

        `when`("모듈 문서를 생성하면") {
            then("PlantUML 다이어그램이 생성되어야 한다") {
                Documenter(modules)
                    .writeModulesAsPlantUml()
                    .writeIndividualModulesAsPlantUml()
                // 결과: build/spring-modulith-docs/components.puml
            }
        }
    }
})
```

**검증 항목:**
- ✅ 순환 의존성 자동 탐지 및 차단
- ✅ 모듈 내부 패키지 보호 (공개 API만 접근 가능)
- ✅ 이벤트 발행/리스닝 관계 추적
- ✅ 모듈 다이어그램 자동 생성 (PlantUML)

**테스트 실행:**
```bash
# 모듈 아키텍처 검증
./gradlew :sseudam-core:core-api:test --tests "*ModulithArchitectureTest"

# 생성된 다이어그램 확인
ls build/spring-modulith-docs/
# components.puml - 전체 모듈 관계도
# module-*.puml - 개별 모듈 상세도
```

### TestContainers 구성

- **PostgreSQL Container**: 실제 DB 환경에서 통합 테스트
- **LocalStack**: AWS S3 Mock 환경
- **픽스처 패턴**: 재사용 가능한 테스트 데이터

<br>

## 📝 API 문서화

**Spring REST Docs + Swagger UI**로 이중 문서화:

1. **REST Docs**: 테스트 기반 자동 생성 (정확성 보장)
2. **Swagger**: 런타임 API 탐색 및 테스트

```kotlin
@RestDocsTest
class TrashSpotApiDocTest {
    @Test
    fun `GET 쓰레기통 목록 조회`() {
        mockMvc.get("/api/v1/trash-spots")
            .andExpect { status { isOk() } }
            .andDocument("trash-spots-list") {
                // 자동으로 Asciidoc 생성
            }
    }
}
```

<br>

## 🛠️ 개발 환경 설정

### 필수 요구사항

- **JDK 21** 이상
- **Docker & Docker Compose**
- **PostgreSQL 14+** (PostGIS 확장 포함)
- **Redis 7.2+**

### 로컬 실행

```bash
# 1. 의존성 컨테이너 실행
docker-compose up -d

# 2. 애플리케이션 빌드
./gradlew clean build

# 3. 로컬 프로파일로 실행
./gradlew :sseudam-core:core-api:bootRun --args='--spring.profiles.active=local'
```

### 환경별 프로파일

- `local`: 로컬 개발 환경
- `dev`: 개발 서버
- `prod`: 프로덕션 환경

<br>

## 🌟 아키텍처 강점

1. **명확한 관심사 분리**: 도메인/애플리케이션/인프라 계층 명확히 구분
2. **테스트 용이성**: 인터페이스 기반 설계로 Mocking 간편
3. **확장성 준비**: 모듈형 구조로 향후 마이크로서비스 전환 가능
4. **이벤트 기반 유연성**: 모듈 간 느슨한 결합으로 변경 영향 최소화
5. **성능 최적화**: 캐시 우선 전략으로 읽기 집약적 워크로드 대응
6. **타입 안전성**: Kotlin + JDSL로 컴파일 타임 쿼리 검증
7. **API 문서 동기화**: 테스트와 문서가 항상 일치
8. **보안**: JWT RSA 서명, Role 기반 접근 제어
9. **관측성**: Sentry + Prometheus + 구조화된 로깅
10. **개발자 경험**: Pre-commit hook, 일관된 코드 스타일, 포괄적인 테스트
11. **아키텍처 검증**: Spring Modulith로 모듈 경계 및 의존성 규칙 자동 검증

<br>
