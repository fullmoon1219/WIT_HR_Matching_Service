$(document).ready(function () {
    loadUserStats();
    loadUserList();
});

function loadUserStats() {
    $.ajax({
        url: '/api/admin/dashboard/stats',
        method: 'GET',
        success: function (data) {
            $('#userCount').text(data.userCount);
            $('#applicantCount').text(data.applicantCount);
            $('#companyCount').text(data.companyCount);
            $('#suspendedUserCount').text(data.suspendedUserCount);
            $('#warningCount').text(data.warningCount);
            $('#unverifiedEmailUserCount').text(data.unverifiedEmailUserCount);
        },
        error: function () {
            alert('사용자 통계를 불러오는 데 실패했습니다.');
        }
    });
}


function loadUserList() {
    $.ajax({
        url: '/api/admin/users',  // ✅ 이 URL에서만 response.content가 존재함
        method: 'GET',
        success: function (response) {
            const users = response.content;  // ✅ 여긴 content 있음

            let html = '';
            users.forEach(user => {
                html += `
                    <tr>
                        <td class="selectable">
                            <div class="checkbox-wrapper">
                                <input type="checkbox" class="user-checkbox" value="${user.id}" />
                                <span class="check-icon">✔</span>
                            </div>
                        </td>

                        <td>${user.id}</td>
                        <td>${user.email}</td>
                        <td>${user.name}</td>
                        <!-- 역할 -->
                        <td>
                          <div class="value-wrapper user-specific">
                            <span class="value role ${user.role.toLowerCase()}" data-type="role" data-id="${user.id}">
                              ${user.role}
                            </span>
                            <div class="value-popup" style="display: none;">
                              <button class="applicant" data-value="APPLICANT">APPLICANT</button>
                              <button class="employer" data-value="EMPLOYER">EMPLOYER</button>
                              <button class="admin" data-value="ADMIN">ADMIN</button>
                            </div>
                          </div>
                        </td>
                        
                        <!-- 상태 -->
                        <td>
                          <div class="value-wrapper user-specific">
                            <span class="value status ${user.status.toLowerCase()}" data-type="status" data-id="${user.id}">
                              ${user.status}
                            </span>
                            <div class="value-popup" style="display: none;">
                              <button class="active" data-value="ACTIVE">ACTIVE</button>
                              <button class="suspended" data-value="SUSPENDED">SUSPENDED</button>
                            </div>
                          </div>
                        </td>
                        
                        <!-- 경고 -->
                        <td>
                          <div class="value-wrapper user-specific">
                            <span class="value warning ${getWarningClass(user.warningCount)}" data-type="warning" data-id="${user.id}">
                              ${user.warningCount}
                            </span>
                            <div class="value-popup" style="display: none;">
                              <button class="warning-0" data-value="0">0</button>
                              <button class="warning-1" data-value="1">1</button>
                              <button class="warning-2" data-value="2">2</button>
                              <button class="warning-3" data-value="3">3</button>
                            </div>
                          </div>
                        </td>
                        
                        <td>${user.emailVerified ? '<span class="icon verified">✔</span>' : '<span class="icon unverified">✖</span>'}</td>
                        <td>${user.lastLogin ? formatDate(user.lastLogin) : '-'}</td>
                        <td style="text-align: center;">
                            <button class="toggle-details-btn" data-id="${user.id}" style="width: 30px;">▼</button>
                        </td>
                    </tr>
                    <tr class="detail-row row-${user.id}">
                        <td colspan="10" style="padding: 0; border: none;">
                            <div class="detail-table-wrapper" style="display: none;">
                                ${renderUserDetail(user)}
                            </div>
                        </td>
                    </tr>
                `;
            });

            $('#userTableBody').html(html);
        },
        error: function (xhr, status, error) {
            console.error("❌ AJAX 오류:", status, error);
            alert('사용자 목록을 불러오는 데 실패했습니다.');
        }
    });
}



function formatDate(dateStr) {
    if (!dateStr) return '-';
    const d = new Date(dateStr);
    return d.getFullYear() + '-' +
        String(d.getMonth() + 1).padStart(2, '0') + '-' +
        String(d.getDate()).padStart(2, '0') + ' ' +
        String(d.getHours()).padStart(2, '0') + ':' +
        String(d.getMinutes()).padStart(2, '0');
}


function renderUserDetail(user) {
    if (user.role === 'APPLICANT') {
        return `
            <table class="applicant-detail-table">
                <tbody>
                <tr>
                    <th rowspan="7">
                        <img src="${user.profileImage || '/images/users/user_big_profile.png'}" alt="프로필 이미지"
                            style="width: 80px; height: 80px; border-radius: 50%; object-fit: cover;">

                    </th>
                    <td class="label">이름</td>
                    <td>${user.name}</td>
                    <td class="label">주소</td>
                    <td>${user.applicantProfile?.address || '-'}</td>
                </tr>
                <tr>
                    <td class="label">나이</td>
                    <td>${user.applicantProfile?.age || '-'}</td>
                    <td class="label">대표 이력서</td>
                    <td>${user.applicantProfile?.primaryResumeId || '-'}</td>
                </tr>
                <tr>
                    <td class="label">성별</td>
                    <td>${user.applicantProfile?.gender || '-'}</td>
                    <td class="label">포트폴리오</td>
                    <td><a href="${user.applicantProfile?.portfolioUrl}" target="_blank">${user.applicantProfile?.portfolioUrl || '-'}</a></td>
                </tr>
                <tr>
                    <td class="label">전화번호</td>
                    <td>${user.applicantProfile?.phoneNumber || '-'}</td>
                    <td class="label">경력</td>
                    <td>${user.applicantProfile?.experienceYears || '-'}</td>
                </tr>
                <tr>
                    <td class="label">한줄 소개</td>
                    <td colspan="3">${user.applicantProfile?.selfIntro || '-'}</td>
                </tr>
                <tr>
                    <td class="label">계정 생성일</td>
                    <td>${formatDate(user.createAt)}</td>
                    <td class="label">마지막 업데이트</td>
                    <td>${formatDate(user.updatedAt)}</td>
                </tr>
                <tr>
                    <td class="label">마지막 로그인</td>
                    <td>${user.lastLogin ? formatDate(user.lastLogin) : '-'}</td>
                    <td class="label">로그인 타입</td>
                    <td>${renderLoginType(user.loginType)}</td>
                </tr>
                </tbody>
            </table>`;
    } else if (user.role === 'EMPLOYER') {
        return `
            <table class="employer-detail-table">
                <tbody>
                <tr>
                    <td class="label">회사명</td>
                    <td>${user.employerProfile?.companyName || '-'}</td>
                    <td class="label">사업자번호</td>
                    <td>${user.employerProfile?.businessNumber || '-'}</td>
                </tr>
                <tr>
                    <td class="label">회사 주소</td>
                    <td>${user.employerProfile?.address || '-'}</td>
                    <td class="label">회사 전화번호</td>
                    <td>${user.employerProfile?.phoneNumber || '-'}</td>
                </tr>
                <tr>
                    <td class="label">홈페이지</td>
                    <td><a href="${user.employerProfile?.homepageUrl}" target="_blank">${user.employerProfile?.homepageUrl || '-'}</a></td>
                    <td class="label">산업군</td>
                    <td>${user.employerProfile?.industry || '-'}</td>
                </tr>
                <tr>
                    <td class="label">설립연도</td>
                    <td>${user.employerProfile?.foundedYear || '-'}</td>
                    <td class="label">기업 규모</td>
                    <td>${user.employerProfile?.companySize || '-'}</td>
                </tr>
                </tbody>
            </table>`;
    }
    return '';
}
