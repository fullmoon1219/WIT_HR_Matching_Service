CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255),
    name VARCHAR(50) NOT NULL,
    role ENUM('APPLICANT', 'EMPLOYER', 'ADMIN') NOT NULL,
    create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ACTIVE', 'SUSPENDED', 'WITHDRAWN') DEFAULT 'ACTIVE',
		warning_count INT NOT NULL DEFAULT 0,
    last_login DATETIME DEFAULT NULL,
    profile_image VARCHAR(255) DEFAULT NULL,
    email_verified TINYINT(1) DEFAULT 0,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
		login_type ENUM('EMAIL','GOOGLE','NAVER') DEFAULT 'EMAIL',
    verification_token VARCHAR(100) DEFAULT NULL,
    token_expiration DATETIME DEFAULT NULL
);

CREATE TABLE resumes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    education TEXT,
    experience TEXT,
    skills TEXT,
    preferred_location VARCHAR(100),
    salary_expectation INT,
    title VARCHAR(100) NOT NULL,
    is_public BOOLEAN DEFAULT true,
    core_competency TEXT,
    desired_position VARCHAR(50),
    motivation TEXT,
		is_completed BOOLEAN DEFAULT FALSE,
    create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE applicant_profiles (
    user_id BIGINT PRIMARY KEY,
    age INT,
    address VARCHAR(255),
    phone_number VARCHAR(20),
    gender ENUM('MALE', 'FEMALE', 'OTHER') DEFAULT 'OTHER',
    portfolio_url VARCHAR(255),
    self_intro TEXT,
    job_type ENUM('FULLTIME', 'PARTTIME', 'INTERNSHIP') DEFAULT 'FULLTIME',
    experience_years INT DEFAULT 0,
    primary_resume_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (primary_resume_id) REFERENCES resumes(id)
);

CREATE TABLE employer_profiles (
    user_id BIGINT PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    business_number VARCHAR(50) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(20),
    homepage_url VARCHAR(255),
    industry VARCHAR(100),
    founded_year INT,
    company_size VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE job_posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    required_skills TEXT,
    salary INT,
    location VARCHAR(100),
    deadline DATE,
    employment_type ENUM('FULLTIME', 'PARTTIME', 'INTERN', 'FREELANCE') DEFAULT 'FULLTIME',
    job_category VARCHAR(100) DEFAULT '기타',
    create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME,
    view_count BIGINT DEFAULT 0,
    bookmark_count BIGINT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    job_post_id BIGINT NOT NULL,
    resume_id BIGINT NOT NULL,
    status ENUM('APPLIED', 'ACCEPTED', 'REJECTED') DEFAULT 'APPLIED',
    applied_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (job_post_id) REFERENCES job_posts(id),
    FOREIGN KEY (resume_id) REFERENCES resumes(id)
);

CREATE TABLE files (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    related_type ENUM('RESUME', 'CERTIFICATE', 'ETC') NOT NULL,
    related_id BIGINT NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    stored_name VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    upload_path VARCHAR(255),
    uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE login_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    login_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent TEXT
);

CREATE TABLE reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_id BIGINT NOT NULL,            -- 신고한 사용자 ID
    target_type VARCHAR(50) NOT NULL,       -- 대상 유형 (예: USER, RESUME, POST 등)
    target_id BIGINT NOT NULL,              -- 대상 객체 ID (ex. 사용자 ID, 이력서 ID 등)
    reason TEXT NOT NULL,                   -- 신고 사유
    status VARCHAR(20) DEFAULT 'PENDING',   -- 처리 상태 (PENDING, RESOLVED, REJECTED)
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (reporter_id) REFERENCES users(id)
);

CREATE TABLE inquiries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,                  -- 문의한 사용자 ID
    category VARCHAR(50) NOT NULL,            -- 문의 유형 (예: 회원가입, 이력서, 공고 등)
    title VARCHAR(255) NOT NULL,              -- 문의 제목
    content TEXT NOT NULL,                    -- 문의 내용
    answer TEXT,                              -- 관리자 답변
    status VARCHAR(20) DEFAULT 'UNANSWERED',  -- 상태: UNANSWERED / ANSWERED
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    answered_at DATETIME,

    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE community_board (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,           -- 게시판 식별자 (PK)
    code        VARCHAR(50) UNIQUE NOT NULL,                 -- 게시판 코드 (예: free, qna 등) → URL 매핑용
    name        VARCHAR(100) NOT NULL,                       -- 게시판 이름 (예: 자유게시판)
    description TEXT                                         -- 게시판 설명 (선택)
);


CREATE TABLE community_post (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,          -- 게시글 ID (PK)
    board_id     BIGINT NOT NULL,                            -- 게시판 ID (FK → community_board)
    user_id      BIGINT NOT NULL,                            -- 작성자 ID (FK → users)
    title        VARCHAR(200) NOT NULL,                      -- 게시글 제목
    content      TEXT NOT NULL,                              -- 게시글 본문 내용
    view_count   INT DEFAULT 0,                              -- 조회수
    like_count   INT DEFAULT 0,                              -- 좋아요 수
    is_deleted   BOOLEAN DEFAULT FALSE,                      -- 삭제 여부 (소프트 삭제용)
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,         -- 작성 시각
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP          -- 수정 시각 (자동 갱신)
                    ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (board_id) REFERENCES community_board(id),   -- 게시판 참조
    FOREIGN KEY (user_id) REFERENCES users(id)               -- 작성자 참조
);


CREATE TABLE community_comment (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,           -- 댓글 ID (PK)
    post_id      BIGINT NOT NULL,                             -- 댓글이 속한 게시글 ID (FK → community_post)
    user_id      BIGINT NOT NULL,                             -- 댓글 작성자 ID (FK → users)
    parent_id    BIGINT,                                      -- 대댓글일 경우 부모 댓글 ID (자기 테이블 참조)
    content      TEXT NOT NULL,                               -- 댓글 본문
    like_count   INT DEFAULT 0,                               -- 댓글 좋아요 수
    is_deleted   BOOLEAN DEFAULT FALSE,                       -- 삭제 여부 (소프트 삭제)
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,          -- 댓글 작성 시각

    FOREIGN KEY (post_id) REFERENCES community_post(id),      -- 게시글 참조
    FOREIGN KEY (user_id) REFERENCES users(id),               -- 작성자 참조
    FOREIGN KEY (parent_id) REFERENCES community_comment(id)  -- 부모 댓글 참조 (nullable → 일반 댓글일 경우 NULL)
);


CREATE TABLE community_post_like (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,             -- 좋아요 ID (PK)
    post_id   BIGINT NOT NULL,                               -- 게시글 ID (FK → community_post)
    user_id   BIGINT NOT NULL,                               -- 좋아요 누른 사용자 ID (FK → users)

    UNIQUE (post_id, user_id),                               -- 같은 게시글에 같은 유저가 두 번 좋아요 못하도록
    FOREIGN KEY (post_id) REFERENCES community_post(id),     -- 게시글 참조
    FOREIGN KEY (user_id) REFERENCES users(id)               -- 사용자 참조
);


CREATE TABLE community_attachment (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,        -- 첨부파일 ID (PK)
    post_id         BIGINT NOT NULL,                          -- 소속 게시글 ID (FK → community_post)
    original_name   VARCHAR(255),                             -- 사용자가 업로드한 원본 파일명
    stored_name     VARCHAR(255),                             -- 서버에 저장된 실제 파일명 (UUID 등)
    file_size       BIGINT,                                   -- 파일 크기 (byte)
    uploaded_at     DATETIME DEFAULT CURRENT_TIMESTAMP,       -- 업로드 시각

    FOREIGN KEY (post_id) REFERENCES community_post(id)       -- 게시글 참조
);

INSERT INTO community_board (code, name, description) VALUES
('free', '자유 게시판', '자유로운 소통 공간입니다.'),
('qna', 'Q&A 게시판', '개발 및 취업 관련 질문과 답변을 나누는 공간입니다.'),
('study', '스터디 게시판', '스터디, 프로젝트 팀원을 모집하는 게시판입니다.'),
('feedback', '피드백 게시판', '이력서와 포트폴리오에 대한 피드백을 요청하세요.'),
('jobshare', '채용 정보 공유 게시판', '유익한 채용 공고를 공유하는 공간입니다.');
