// /js/admin/resume/resume-list.js

$(document).ready(function () {
    loadResumeStats();
});

// ✅ 이력서 통계 로딩
function loadResumeStats() {
    $.ajax({
        url: "/api/admin/resumes/statistics", // 변경된 URI
        method: "GET",
        success: function (data) {
            $("#resumeCount").text(data.total ?? 0);
            $("#isCompleted").text(data.completed ?? 0);
            $("#isIncompleted").text(data.incompleted ?? 0);
            $("#isPublic").text(data.public ?? 0);
            $("#isDeleted").text(data.deleted ?? 0);
        },
        error: function (xhr) {
            console.error("이력서 통계 로딩 실패", xhr);
        }
    });
}

// ✅ 이력서 상세보기
$(document).on("click", ".resume-title", function () {
    const resumeId = $(this).data("id");

    $.ajax({
        url: "/api/admin/resumes", // RESTful URI
        method: "GET",
        data: { id: resumeId, page: 1, size: 1 },
        success: function (response) {
            if (!response.content || response.content.length === 0) {
                openFloatingSidebar("<p>이력서 정보를 찾을 수 없습니다.</p>");
                return;
            }

            const r = response.content[0];

            const isCompleted = r.isCompleted ? '✅ 완료' : '❌ 미완료';
            const isPublic = r.isPublic ? '공개' : '비공개';
            const deleted = r.deletedAt ? '삭제됨' : '-';

            const contentHtml = `
                <div class="resume-detail">
                    <h2>이력서 상세 정보</h2>
                    <table class="resume-detail-table">
                        <tbody>
                            <tr><th>이력서 ID</th><td>${r.id}</td></tr>
                            <tr><th>작성자 ID</th><td>${r.userId}</td></tr>
                            <tr><th>이메일</th><td>${r.email || '-'}</td></tr>
                            <tr><th>제목</th><td>${r.title || '-'}</td></tr>
                            <tr><th>핵심 역량</th><td>${r.coreCompetency || '-'}</td></tr>
                            <tr><th>희망 직무</th><td>${r.desiredPosition || '-'}</td></tr>
                            <tr><th>지원 동기</th><td>${r.motivation || '-'}</td></tr>
                            <tr><th>학력</th><td>${r.education || '-'}</td></tr>
                            <tr><th>경력</th><td>${r.experience || '-'}</td></tr>
                            <tr><th>보유 기술</th><td>${r.skills || '-'}</td></tr>
                            <tr><th>희망 지역</th><td>${r.preferredLocation || '-'}</td></tr>
                            <tr><th>희망 연봉</th><td>${r.salaryExpectation || '-'}</td></tr>
                            <tr><th>완료 여부</th><td>${isCompleted}</td></tr>
                            <tr><th>공개 여부</th><td>${isPublic}</td></tr>
                            <tr><th>작성일</th><td>${r.createAt || '-'}</td></tr>
                            <tr><th>수정일</th><td>${r.updatedAt || '-'}</td></tr>
                            <tr><th>삭제 여부</th><td style="color: red;">${deleted}</td></tr>
                        </tbody>
                    </table>
                </div>
            `;

            openFloatingSidebar(contentHtml);
        },
        error: function () {
            openFloatingSidebar("<p>이력서 상세 정보를 불러오지 못했습니다.</p>");
        }
    });
});

// ✅ 사용자 상세 정보 조회 (RESTful)
$(document).on("click", ".resume-email", function () {
    const userId = $(this).data("userid");

    console.log("유저 ID 확인:", userId);

    $.ajax({
        url: `/api/admin/users/${userId}`, // ✅ RESTful URI
        method: "GET",
        success: function (u) {
            if (!u || !u.id) {
                openFloatingSidebar("<p>사용자 정보를 찾을 수 없습니다.</p>");
                return;
            }

            const applicant = u.applicantProfile;
            const employer = u.employerProfile;

            const baseInfo = `
                <h2>사용자 기본 정보</h2>
                <table class="resume-detail-table">
                    <tbody>
                        <tr><th>사용자 ID</th><td>${u.id}</td></tr>
                        <tr><th>이메일</th><td>${u.email}</td></tr>
                        <tr><th>이름</th><td>${u.name || '-'}</td></tr>
                        <tr><th>역할</th><td>${u.role}</td></tr>
                        <tr><th>가입일</th><td>${u.createAt || '-'}</td></tr>
                        <tr><th>최근 로그인</th><td>${u.lastLogin || '-'}</td></tr>
                        <tr><th>이메일 인증</th><td>${u.emailVerified ? '✅ 인증됨' : '❌ 미인증'}</td></tr>
                        <tr><th>상태</th><td>${u.status || '-'}</td></tr>
                        <tr><th>경고 횟수</th><td>${u.warningCount || 0}</td></tr>
                        <tr><th>로그인 방식</th><td>${u.loginType || 'EMAIL'}</td></tr>
                    </tbody>
                </table>
            `;

            let profileInfo = '';

            if (u.role === 'APPLICANT' && applicant) {
                profileInfo = `
                    <h2>지원자 프로필</h2>
                    <table class="resume-detail-table">
                        <tbody>
                            <tr><th>나이</th><td>${applicant.age || '-'}</td></tr>
                            <tr><th>성별</th><td>${applicant.gender || '-'}</td></tr>
                            <tr><th>주소</th><td>${applicant.address || '-'}</td></tr>
                            <tr><th>전화번호</th><td>${applicant.phoneNumber || '-'}</td></tr>
                            <tr><th>직무 유형</th><td>${applicant.jobType || '-'}</td></tr>
                            <tr><th>경력 년수</th><td>${applicant.experienceYears || '-'}</td></tr>
                            <tr><th>포트폴리오</th><td><a href="${applicant.portfolioUrl || '#'}" target="_blank">${applicant.portfolioUrl || '-'}</a></td></tr>
                            <tr><th>자기소개</th><td>${applicant.selfIntro || '-'}</td></tr>
                            <tr><th>대표 이력서 ID</th><td>${applicant.primaryResumeId || '-'}</td></tr>
                        </tbody>
                    </table>
                `;
            }

            const contentHtml = `
                <div class="user-detail">
                    ${baseInfo}
                    ${profileInfo}
                </div>
            `;

            openFloatingSidebar(contentHtml);
        },
        error: function () {
            openFloatingSidebar("<p>사용자 정보를 불러오지 못했습니다.</p>");
        }
    });
});


// ✅ 페이징 처리
$(document).on('click', '.pagination-btn', function () {
    const selectedPage = $(this).data('page');
    loadUserList(selectedPage);
});
