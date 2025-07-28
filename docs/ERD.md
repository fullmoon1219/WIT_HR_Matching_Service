# 🗂️ HR Matching Service - 데이터베이스 설계서

---

## ✅ 주요 개념

본 프로젝트는 구직자와 채용기업 간의 매칭을 지원하는 시스템으로, 사용자 정보, 이력서, 공고, 지원, 커뮤니티, 신고/문의, AI 분석 기능 등을 중심으로 데이터 모델링 되었습니다.

ERD는 다음과 같은 도메인으로 구성됩니다:

- **회원 도메인**: users, applicant_profiles, employer_profiles, login_history
- **이력서 도메인**: resumes, files
- **공고 도메인**: job_posts, applications, application_views, bookmarks
- **커뮤니티 도메인**: community_board, community_post, comment, like, attachment
- **고객 지원 도메인**: inquiries, inquiry_reasons, reports, admin_memo
- **기타**: tech_stacks, AI 연동용 필드 등

---

## 🧩 주요 테이블 요약

| 테이블 | 설명 |
|--------|------|
| `users` | 회원 공통 테이블 (지원자/기업 공용) |
| `applicant_profiles` | 지원자 상세 프로필 |
| `employer_profiles` | 기업 회원 정보 |
| `job_posts` | 채용 공고 |
| `resumes` | 지원자 이력서 |
| `applications` | 공고에 대한 지원 이력 |
| `bookmarks` | 공고 스크랩 |
| `inquiries` | 사용자 문의 내역 |
| `reports` | 신고 내역 |
| `community_*` | 커뮤니티 기능(게시글, 댓글, 좋아요 등) |
| `admin_memo` | 관리자 메모 |
| `login_history` | 로그인 기록 |
| `files` | 파일 업로드 관리 |
| `tech_stacks` | 기술 스택 목록 |

---

## 👤 `users` - 사용자 테이블

| 필드명 | 타입 | 설명 |
|--------|------|------|
| id | BIGINT (PK) | 사용자 ID |
| email | VARCHAR | 로그인 이메일 |
| password | VARCHAR | 암호화된 비밀번호 |
| name | VARCHAR | 이름 또는 기업명 |
| role | ENUM(APPLICANT, EMPLOYER) | 사용자 유형 |
| status | ENUM | ACTIVE, SUSPENDED 등 |
| profile_image | VARCHAR | 프로필 이미지 경로 |
| email_verified | BOOLEAN | 이메일 인증 여부 |
| login_type | ENUM | EMAIL, GOOGLE, NAVER |
| is_deleted | BOOLEAN | 탈퇴 여부 (소프트 삭제) |

---

## 📑 `resumes` - 이력서

| 필드명 | 타입 | 설명 |
|--------|------|------|
| id | BIGINT (PK) | 이력서 ID |
| user_id | FK → users | 작성자 ID |
| title | VARCHAR | 이력서 제목 |
| education / experience / skills | TEXT | 각 항목 상세내용 |
| is_public | BOOLEAN | 공개 여부 |
| is_completed | BOOLEAN | 작성 완료 여부 |
| deleted_at | DATETIME | 삭제 시각(소프트 삭제) |

---

## 💼 `job_posts` - 채용공고

| 필드명 | 타입 | 설명 |
|--------|------|------|
| id | BIGINT (PK) | 공고 ID |
| user_id | FK → users | 공고 등록한 기업 |
| title | VARCHAR | 공고 제목 |
| job_category | TEXT | 직무 카테고리 |
| required_skills | TEXT | 요구 기술 |
| location | VARCHAR | 근무지 |
| employment_type | ENUM | FULLTIME 등 |
| experience_type | ENUM | 경력/신입 |
| view_count | INT | 조회수 |
| bookmark_count | INT | 스크랩 수 |

---

## 📝 `applications` - 지원 이력

| 필드명 | 타입 | 설명 |
|--------|------|------|
| id | BIGINT (PK) | 지원 ID |
| user_id | FK → users | 지원자 |
| job_post_id | FK → job_posts | 공고 |
| resume_id | FK → resumes | 제출한 이력서 |
| status | ENUM | APPLIED, ACCEPTED, REJECTED |
| applied_at | DATETIME | 지원 시점 |

---

## 📚 `community_post` - 커뮤니티 게시글

| 필드명 | 타입 | 설명 |
|--------|------|------|
| id | BIGINT | 게시글 ID |
| board_id | FK → community_board | 게시판 종류 |
| user_id | FK → users | 작성자 |
| title / content | TEXT | 제목 / 본문 |
| view_count / like_count | INT | 조회수 / 좋아요 |
| is_deleted | BOOLEAN | 삭제 여부 |

---

## 📢 `reports` - 신고 내역

| 필드명 | 타입 | 설명 |
|--------|------|------|
| id | BIGINT | 신고 ID |
| reporter_user_id | FK → users | 신고자 |
| report_type | ENUM | USER / JOB_POST / COMMUNITY_POST |
| reason / description | TEXT | 신고 사유 / 상세 설명 |
| action_taken | ENUM | WARNING, SUSPEND 등 |
| reviewed_by_admin_id | FK → users | 처리 관리자 |

---

## 📋 `inquiries` - 문의

| 필드명 | 타입 | 설명 |
|--------|------|------|
| id | BIGINT | 문의 ID |
| user_id | FK → users | 작성자 |
| reason_id | FK → inquiry_reasons | 사유 분류 |
| title / content | TEXT | 제목 / 내용 |
| reply | TEXT | 관리자 답변 |
| status | ENUM | WAITING, ANSWERED |

---

## 💬 기타 테이블

- `login_history`: 사용자 로그인 시간, IP, 에이전트 기록
- `files`: 파일 업로드 이력 (이력서, 커뮤니티 등과 연관)
- `admin_memo`: 관리자 전용 메모
- `tech_stacks`: 기술 스택 이름 목록
- `community_comment`, `community_comment_like`: 댓글/대댓글 + 좋아요
- `application_views`: 기업이 열람한 지원 이력

---

## 📌 참고

- 모든 테이블은 기본적으로 `created_at`, `updated_at`, `deleted_at` 등의 시간 필드를 포함합니다.
- 주요 관계형 구조는 MyBatis + MySQL 외래키 기준으로 매핑됩니다.
- ENUM 타입은 MySQL ENUM 또는 VARCHAR 기반으로 관리되었습니다.

---

## 📌 ERD

![ERD](/docs/images/db_erd.png)