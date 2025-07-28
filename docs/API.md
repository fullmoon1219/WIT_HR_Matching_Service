# 📡 HR Matching Service API 명세서

API Base URL: `/api`

---

## 1. ✅ 사용자(User) API

| Method | Endpoint | 설명 |
| ------ | -------- | ---- |
| POST | `/users` | 회원가입 처리 |
| DELETE | `/users/me` | 내 계정 탈퇴 |
| GET | `/users/verify-email` | 이메일 인증 처리 |
| GET | `/users/email-exists` | 이메일 중복 확인 |
| POST | `/users/resend-verification` | 인증 메일 재발송 |
| GET | `/profile` | 내 프로필 조회 |
| PUT | `/profile` | 내 프로필 수정 |
| POST | `/profile/image` | 프로필 이미지 업로드 |
| POST | `/profile/verify-password` | 현재 비밀번호 검증 |
| PUT | `/profile/password` | 비밀번호 변경 |
| GET | `/profile/dashboard` | 마이페이지 요약정보 조회 |

---

## 2. 📄 이력서(Resume) API

| Method | Endpoint | 설명 |
| ------ | -------- | ---- |
| GET | `/resumes` | 내 이력서 전체 조회 |
| POST | `/resumes` | 이력서 등록 |
| POST | `/resumes/draft` | 이력서 임시 저장 |
| GET | `/resumes/completed` | 작성 완료 이력서 조회 |
| GET | `/resumes/draft` | 임시 저장 이력서 조회 |
| GET | `/resumes/{id}` | 특정 이력서 상세 조회 |
| PUT | `/resumes/{id}` | 이력서 수정 |
| PUT | `/resumes/draft/{id}` | 임시 이력서 수정 |
| DELETE | `/resumes/{id}` | 이력서 삭제 |
| PUT | `/resumes/{id}/public` | 대표 이력서 설정 |
| PUT | `/resumes/{id}/private` | 대표 이력서 해제 |

---

## 3. 📢 채용공고(JobPost) API

| Method | Endpoint | 설명 |
| ------ | -------- | ---- |
| GET | `/jobs/random` | 랜덤 공고 조회 |
| GET | `/jobs/urgent` | 마감 임박 공고 조회 |
| GET | `/recruit` | 조건 기반 공고 목록 조회 |
| GET | `/recruit/{id}` | 공고 상세 조회 |
| GET | `/recruit/{id}/summary` | 요약 정보 조회 |
| POST | `/recruit/{id}` | 공고 지원 |

---

## 4. 📬 지원(Application) API

| Method | Endpoint | 설명 |
| ------ | -------- | ---- |
| GET | `/applications` | 내 지원 내역 목록 |
| GET | `/applications/{id}` | 특정 지원 내역 상세 |

---

## 5. 💌 문의 / 신고 API

### 문의

| Method | Endpoint | 설명 |
| ------ | -------- | ---- |
| GET | `/users/inquiries/reasons` | 문의 사유 조회 |
| POST | `/users/inquiries` | 문의 등록 |
| GET | `/users/inquiries` | 내 문의 목록 조회 |
| GET | `/users/inquiries/{id}` | 문의 상세 조회 |

### 신고

| Method | Endpoint | 설명 |
| ------ | -------- | ---- |
| POST | `/users/reports` | 사용자 신고 등록 |

---

## 6. 🏘️ 커뮤니티 API

| Method | Endpoint | 설명 |
| ------ | -------- | ---- |
| GET | `/community` | 게시판 목록 |
| GET | `/community/posts` | 게시글 목록 (페이징) |
| GET | `/community/posts/{id}` | 게시글 상세 |
| POST | `/community/posts/{id}/view` | 조회수 증가 |
| POST | `/community/{boardCode}/write` | 게시글 작성 |
| POST | `/community/image-upload` | 에디터 이미지 업로드 |
| POST | `/community/delete-temp-images` | 임시 이미지 삭제 |
| POST | `/community/posts/{id}/like` | 게시글 좋아요 |
| DELETE | `/community/posts/{id}` | 게시글 삭제 |
| PUT | `/community/posts/{id}` | 게시글 수정 |

### 댓글 API

| Method | Endpoint | 설명 |
| ------ | -------- | ---- |
| GET | `/community/comments/{postId}` | 댓글 목록 조회 |
| POST | `/community/comments` | 댓글 작성 |
| PUT | `/community/comments/{id}` | 댓글 수정 |
| DELETE | `/community/comments/{id}` | 댓글 삭제 |
| POST | `/community/comments/{id}/like` | 댓글 좋아요 |

---

## 7. 🤖 AI API

| Method | Endpoint | 설명 |
| ------ | -------- | ---- |
| POST | `/ai/resumes/analyze` | 이력서 AI 분석 |
| POST | `/ai/resumes/summary` | 이력서 요약 (기업용) |
| POST | `/ai/interview/questions` | 모의면접 질문 생성 |
| POST | `/ai/interview/evaluate` | 면접 답변 피드백 |

---

## 8. 🔒 관리자(Admin) API

### 사용자 관리

| Method | Endpoint | 설명 |
| ------ | -------- | ---- |
| GET | `/admin/users` | 전체 사용자 목록 |
| GET | `/admin/users/{id}` | 사용자 상세 조회 |
| PATCH | `/admin/users/role` | 사용자 역할 변경 |
| PATCH | `/admin/users/status` | 상태 변경 (정지 등) |
| PATCH | `/admin/users/warning` | 경고 횟수 조정 |
| DELETE | `/admin/users` | 사용자 삭제 |

### 대시보드

| Method | Endpoint | 설명 |
| ------ | -------- | ---- |
| GET | `/admin/dashboard/stats` | 전체 통계 조회 |
| GET | `/admin/dashboard/recent-users` | 최근 가입자 조회 |
| GET | `/admin/dashboard/recent-job-posts` | 최근 공고 조회 |
| GET | `/admin/dashboard/daily-user-counts` | 일일 가입자 수 |
| GET | `/admin/dashboard/daily-login-counts` | 일일 로그인 수 |
| GET | `/admin/dashboard/user-role-distribution` | 사용자 역할 분포 |
| GET | `/admin/dashboard/login-type-distribution` | 로그인 유형 비율 |

### 공고 / 이력서 관리

| Method | Endpoint | 설명 |
| ------ | -------- | ---- |
| GET | `/admin/posts` | 공고 목록 |
| DELETE | `/admin/posts` | 공고 삭제 |
| GET | `/admin/resumes` | 이력서 목록 |
| DELETE | `/admin/resumes` | 이력서 삭제 |

### 문의 / 신고 / 메모

| Method | Endpoint | 설명 |
| ------ | -------- | ---- |
| GET | `/admin/inquiries` | 문의 목록 |
| PATCH | `/admin/inquiries/{id}/reply` | 문의 답변 작성 |
| POST | `/admin/reports/{id}/dismiss` | 신고 기각 처리 |
| POST | `/admin/reports/{id}/apply` | 신고 제재 적용 |
| GET | `/admin/memos` | 관리자 메모 목록 |
| POST | `/admin/memos` | 메모 등록 |
| DELETE | `/admin/memos/{id}` | 메모 삭제 |

---

## 📁 기타

| Method | Endpoint | 설명 |
| ------ | -------- | ---- |
| GET | `/api/bookmarks` | 내 스크랩 목록 |
| POST | `/api/bookmarks/{jobPostId}` | 공고 스크랩 등록 |
| DELETE | `/api/bookmarks/{jobPostId}` | 공고 스크랩 해제 |
| GET | `/api/tech-stacks` | 기술스택 목록 조회 |
| GET | `/files/download/{id}` | 파일 다운로드 |

---

> 🔐 모든 API는 Spring Security 기반 인증이 적용되어 있으며, 권한(role)에 따라 접근이 제한됩니다.
