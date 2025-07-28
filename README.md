# 📛 HR Matching Service
![ERD](/docs/images/logo_no_background.png)
> Spring Boot 기반의 지능형 구인/구직 매칭 플랫폼  
> 개발 기간: 2025.06.16 ~ 2025.07.27  

---

## 🧰 기술 스택

### Frontend
- HTML
- Thymeleaf
- Thymeleaf Layout Dialect

### Backend
- Java 17
- Spring Boot
- MyBatis, JPA
- Spring Security
- Spring WebFlux (일부)
- Java Mail Sender

### Database
- MySQL

### 기타
- Lombok, Validation
- OAuth2 Client (소셜 로그인)
- SLF4J + Logback
- Chart.js

---

## 👥 팀 구성 및 역할

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/fullmoon1219">
        <img src="https://avatars.githubusercontent.com/fullmoon1219" width="100px" style="border-radius: 10px;" />
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/kkknature">
        <img src="https://avatars.githubusercontent.com/kkknature" width="100px" style="border-radius: 10px;" />
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/paht9939">
        <img src="https://avatars.githubusercontent.com/paht9939" width="100px" style="border-radius: 10px;" />
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/hanna0526">
        <img src="https://avatars.githubusercontent.com/hanna0526" width="100px" style="border-radius: 10px;" />
      </a>
    </td>
  </tr>
  <tr>
    <td align="center"><b><a href="https://github.com/fullmoon1219">@fullmoon1219</a></b></td>
    <td align="center"><b><a href="https://github.com/kkknature">@kkknature</a></b></td>
    <td align="center"><b><a href="https://github.com/paht9939">@paht9939</a></b></td>
    <td align="center"><b><a href="https://github.com/hanna0526">@hanna0526</a></b></td>
  </tr>
  <tr>
    <td align="center">👑 팀장<br />✔️ 프론트 · 백엔드</td>
    <td align="center">✔️ 백엔드</td>
    <td align="center">✔️ 프론트</td>
    <td align="center">✔️ 백엔드</td>
  </tr>
</table>

---

## 📌 주요 기능 요약

### 🔐 공통 기능
- 회원가입 (지원자/기업)
- 로그인 / 로그아웃
- 이메일 인증 / 비밀번호 변경
- 마이페이지
- 커뮤니티 게시판

### 👤 지원자 기능
- 이력서 등록 / 수정 / 삭제
- 채용 공고 검색 및 상세조회
- 공고 지원 및 이력 관리
- 이력서 분석 (AI), 모의면접 (AI)
- 스크랩 기능

### 🏢 기업 기능
- 채용 공고 등록 / 수정 / 삭제
- 공고별 지원자 목록 / 이력서 열람
- 채용 결과 처리
- 이력서 검색 및 요약 분석 (AI)

### 🛠️ 관리자 기능
- 사용자 관리 (정지/삭제/경고 등)
- 공고/이력서 모니터링
- 통계 대시보드
- 문의/신고 관리
- 관리자 메모 기능

---

## 🧾 ERD (주요 테이블 요약)

- `users`: 공통 사용자 테이블 (지원자/기업/관리자)
- `applicant_profiles`: 지원자 상세 프로필
- `employer_profiles`: 기업 회원 상세 정보
- `job_posts`: 채용 공고
- `resumes`: 이력서
- `applications`: 공고 지원 이력
- `community_*`: 커뮤니티(게시판, 댓글, 좋아요 등)
- `reports`, `inquiries`: 신고 및 문의 처리 테이블
- `files`: 첨부파일 관리
- `admin_memo`: 관리자 메모

> 전체 테이블 및 상세 필드 구조는 [ERD 문서](/docs/ERD.md)에서 확인할 수 있습니다.

---

## 📊 관리자 통계 기능

- 최근 가입자 / 공고 등록 목록
- 사용자 유형 및 로그인 유형 분포
- 이력서 등록/완료율 통계
- 공고 직무 분포, 경고/정지 비율
- Chart.js 기반 대시보드 시각화

---

## 🔒 인증 및 보안

- Spring Security 기반 인증 / 인가
- 비밀번호 암호화 (`BCrypt`)
- 이메일 인증 링크 전송 (`JavaMailSender`)
- 권한별 접근 제어 (지원자 / 기업 / 관리자)

---

## 🤖 AI 연동 기능

- **이력서 분석**: 강점, 약점, 총점, 종합 피드백
- **모의면접**: 이력서 기반 질문 생성 → 답변 → 평가
- OpenAI 기반 비동기 API 연동

---

## 📦 프로젝트 구조

```
src
└── main
    ├── java
    │   └── org.wit.hrmatching
    │       ├── config          # 보안, CORS, OAuth 설정 등
    │       ├── controller      # REST API 컨트롤러 계층
    │       ├── dao             # DAO 클래스 (비즈니스 DB 접근)
    │       ├── dto             # 요청/응답 데이터 전달용 DTO
    │       ├── enums           # Enum 정의 (역할, 상태 등)
    │       ├── exception       # 예외 처리 및 핸들러
    │       ├── mapper          # MyBatis 매퍼 인터페이스
    │       ├── service         # 서비스 계층 (비즈니스 로직)
    │       └── vo              # VO 클래스 (DB 테이블 매핑)
    │
    └── resources
        ├── mappers            # MyBatis XML 매퍼
        ├── static             # 정적 파일 (JS, CSS, 이미지 등)
        ├── templates.hrms     # Thymeleaf HTML 템플릿
        └── application.yml    # 프로젝트 설정 파일
```

---

## 🧪 테스트 및 배포

- 운영체제: AWS Ubuntu Linux
- 서버: Spring Boot 내장 Tomcat
- DB: MySQL 8.x
- 배포 방식: EC2 수동 배포 (보안그룹 설정 포함)

---

## 🗂️ API 명세

> 일부 예시만 작성되었습니다. 전체 API 명세는 [API 문서](docs/API.md)를 참고하세요.

### 회원 API

| Method | URL | 설명 |
| --- | --- | --- |
| POST | /api/users | 회원가입 |
| GET | /api/users/verify-email | 이메일 인증 처리 |
| POST | /api/users/login | 로그인 |
| GET | /api/profile | 프로필 조회 |

### 공고 API

| Method | URL | 설명 |
| --- | --- | --- |
| GET | /api/recruit | 공고 목록 조회 |
| GET | /api/recruit/{id} | 공고 상세 조회 |
| POST | /api/recruit/{id} | 공고 지원 |

---

## 📌 기타 기술 포인트

- 유효성 검증: `javax.validation` 활용
- 예외 처리: `@ControllerAdvice` 기반 글로벌 핸들러
- DTO/VO 분리 설계
- 소프트 삭제 (`is_deleted`, `deleted_at`)
- 페이징 및 필터링 API 전반 적용

---

## 📚 문서 자료

- [ERD 및 테이블 정의](docs/ERD.md)
- [API 명세서](docs/API.md)

---

## 🙌 기여자

본 프로젝트는 WIT Novi 프로젝트 팀에서 진행한 팀 프로젝트입니다.  
