// /js/admin/jobPosts/jobPosts.js

// /js/admin/jobPosts/jobPosts.js

$(document).ready(function () {
    loadPostStats();
});

// ✅ 공고 통계 데이터 불러오기
function loadPostStats() {
    $.ajax({
        url: '/api/admin/posts/stats',
        method: 'GET',
        success: function (data) {
            $('#totalPosts').text(data.total ?? 0);
            $('#availablePosts').text(data.available ?? 0);
            $('#closedPosts').text(data.closed ?? 0);
            $('#deletedPosts').text(data.deleted ?? 0);
        },
        error: function () {
            alert("공고 통계를 불러오는 데 실패했습니다.");
        }
    });
}

$(document).on("click", ".post-title", function () {
    const postId = $(this).data("id");
    if (!postId) {
        alert("유효하지 않은 공고 ID입니다.");
        return;
    }

    window.location.href = `/recruit/view/${postId}`;
});


$(document).on("click", ".post-name", function () {
    const userId = $(this).data("userid");

    $.ajax({
        url: `/api/admin/users/${userId}`,
        method: "GET",
        success: function (u) {
            if (!u || !u.id) {
                openFloatingSidebar("<p>사용자 정보를 찾을 수 없습니다.</p>");
                return;
            }

            const employer = u.employerProfile;

            const baseInfo = `
                <h2>사용자 기본 정보</h2>
                <table class="post-detail-table">
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

            if (u.role === 'EMPLOYER' && employer) {
                profileInfo = `
                    <h2>기업 프로필</h2>
                    <table class="post-detail-table">
                        <tbody>
                            <tr><th>회사명</th><td>${employer.companyName || '-'}</td></tr>
                            <tr><th>사업자번호</th><td>${employer.businessNumber || '-'}</td></tr>
                            <tr><th>주소</th><td>${employer.address || '-'}</td></tr>
                            <tr><th>전화번호</th><td>${employer.phoneNumber || '-'}</td></tr>
                            <tr><th>홈페이지</th><td><a href="${employer.homepageUrl || '#'}" target="_blank">${employer.homepageUrl || '-'}</a></td></tr>
                            <tr><th>업종</th><td>${employer.industry || '-'}</td></tr>
                            <tr><th>설립년도</th><td>${employer.foundedYear || '-'}</td></tr>
                            <tr><th>회사 규모</th><td>${employer.companySize || '-'}</td></tr>
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

