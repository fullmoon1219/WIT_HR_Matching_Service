// /js/admin/reportManagement/report-management.js

$(document).ready(function () {
    // 검색 버튼 클릭
    $('#searchButton').on('click', function () {
        currentFilters.keyword = $('#searchKeyword').val();
        loadReportList(currentFilters);
    });

    // 전체 선택
    $('#checkAll').on('change', function () {
        const checked = $(this).is(':checked');
        $('#reportTableBody input[type="checkbox"]').prop('checked', checked);
    });

    // 삭제 버튼 클릭 (선택 삭제)
    $('#deleteButton').on('click', function () {
        const selectedIds = $('#reportTableBody input[type="checkbox"]:checked')
            .map(function () { return $(this).data('id'); }).get();

        if (selectedIds.length === 0) {
            alert('삭제할 항목을 선택하세요.');
            return;
        }

        if (!confirm(`${selectedIds.length}건을 삭제하시겠습니까?`)) return;

        $.ajax({
            url: '/api/admin/reports',
            method: 'DELETE',
            contentType: 'application/json',
            data: JSON.stringify(selectedIds),
            success: function () {
                alert('삭제되었습니다.');
                loadReportStats();
                loadReportList(currentFilters);
            }
        });
    });
});

// ✅ 사용자 상세 정보 조회 (RESTful)
$(document).on("click", ".reporter-user-info", function () {
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