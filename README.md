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

# 쓰담
<img width="130" height="130" alt="AppIcon" src="https://github.com/user-attachments/assets/c8d8d6b1-8b0c-4e53-b05e-370eacec0c31" />

## 🐱 Feature
|<img width="300" alt="screenshot3" src="https://github.com/user-attachments/assets/66a52426-1ecc-45e8-8a94-0adf147ab732" />|<img width="300" alt="screenshot4" src="https://github.com/user-attachments/assets/d2dd86ec-b542-4da5-b09f-a9a711ca7c18" />|<img width="300" alt="screenshot5" src="https://github.com/user-attachments/assets/bf42fe23-44a6-41cd-83ee-c96d63fba481" />|
|:----:|:----:|:----:|

<br>

## 🏗️ Architecture

```
📦 Sseudam-Server
├── 🎯 sseudam-core/
│   ├── core-api/          # REST API 엔드포인트
│   ├── core-domain/       # 도메인 모델 및 비즈니스 로직
│   └── core-contract/     # 공통 DTO 및 예외 처리 정의
├── 💾 sseudam-storage/
│   ├── db-core/           # JPA 엔티티 및 리포지토리
│   └── redis/             # Redis 캐시 설정
├── 🔌 sseudam-clients/
│   ├── aws/               # AWS S3 클라이언트
│   ├── notification/      # Firebase 푸시 알림
│   └── oauth-client/      # OAuth 인증 클라이언트
├── 🛠️ sseudam-supports/
│   ├── logging/           # 로깅 설정
│   ├── monitoring/        # 모니터링 및 메트릭
│   └── swagger/           # API 문서화
├── 🔄 sseudam-batch/      # 배치 작업
├── 👨‍💼 sseudam-admin/       # 관리자 기능
└── 🧪 sseudam-tests/      # 테스트 유틸리티
    ├── api-docs/          # RestDocs API 문서 테스트
    ├── test-container/    # TestContainers 설정
    └── test-helper/       # 테스트 헬퍼
```

## ⚙️ Tech Stack

### Backend
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

### Database
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Hibernate Spatial](https://img.shields.io/badge/Hibernate%20Spatial-59666C?style=for-the-badge&logo=hibernate&logoColor=white)

### Cloud & Infrastructure
![AWS S3](https://img.shields.io/badge/AWS%20S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)

### Monitoring & Documentation
![Sentry](https://img.shields.io/badge/Sentry-362D59?style=for-the-badge&logo=sentry&logoColor=white)
![Prometheus](https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

### Testing
![Kotest](https://img.shields.io/badge/Kotest-34A853?style=for-the-badge&logo=kotlin&logoColor=white)
![TestContainers](https://img.shields.io/badge/Test--Containers-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![MockK](https://img.shields.io/badge/MockK-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)

<br>
