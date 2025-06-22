//  /static/js/admin/dashboard-ajax.js

$(document).ready(function () {
    // 카드 수치 데이터
    $.get("/api/admin/dashboard/stats", function (data) {
        $("#total-user-count").text(data.userCount);
        $("#applicant-count").text(data.applicantCount);
        $("#company-count").text(data.companyCount);
        $("#resume-count").text(data.resumeCount);
        $("#job-post-count").text(data.jobPostCount);
        $("#application-count").text(data.applicationCount);
    });


    // 최근 가입 사용자
    $.get("/api/admin/dashboard/recent-users", function (users) {
        const $tbody = $("#recent-users-table tbody");
        $tbody.empty();

        for (let i = 0; i < 5; i++) {
            const user = users[i];
            const html = `
                <tr>
                    <td>${user?.id ?? '--'}</td>
                    <td>${user?.email ?? '--'}</td>
                    <td>${user?.name ?? '--'}</td>
                    <td>${user?.createAt ?? '--'}</td>
                    <td>${user?.role ?? '--'}</td>
                    <td>${user?.loginType ?? '--'}</td>
                </tr>
            `;
            $tbody.append(html);
        }
    });

    // 최근 등록된 채용공고
    $.get("/api/admin/dashboard/recent-job-posts", function (posts) {
        const $tbody = $("#recent-job-posts-table tbody");
        $tbody.empty();

        for (let i = 0; i < 5; i++) {
            const post = posts[i];
            const html = `
                <tr>
                    <td>${post?.id ?? '--'}</td>
                    <td>${post?.title ?? '--'}</td>
                    <td>${post?.companyName ?? '--'}</td>
                    <td>${post?.createAt ?? '--'}</td>
                </tr>
            `;
            $tbody.append(html);
        }
    });
});
