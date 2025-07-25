// /js/admin/reportManagement/report-processing.js

// 신고 사유 클릭 시 플로팅 바 열기
$(document).on("click", ".report-reason", function () {
    const reportId = $(this).data("reportid");
    const reasonText = $(this).text();
    const reportType = $(this).data("reporttype");
    const targetId = $(this).data("targetid");

    // 신고 상세 정보 불러오기
    $.get(`/api/admin/reports/${reportId}`, function (report) {
        let penaltyHtml = '';
        let actionButtonsHtml = '';

        // 처리되지 않은 상태면 제재 조치 UI 표시
        if (report.status === 'PENDING') {
            penaltyHtml = `
              <div class="penalty-section">
                <h3>제재 조치 선택</h3>
                <div class="penalty-options">
                  <label><input type="radio" name="action" value="WARNING_1"> 경고 1회</label>
                  <label><input type="radio" name="action" value="SUSPEND"> 계정 정지</label>
                </div>
              </div>
            `;

            actionButtonsHtml = `
              <div class="action-buttons">
                <button class="dismiss-btn" data-id="${report.id}">기각</button>
                <button class="apply-btn" data-id="${report.id}">적용</button>
              </div>
            `;
        } else {
            penaltyHtml = `
              <div class="penalty-section">
                <h3>처리 정보</h3>
                <table class="report-table">
                  <tr><th>처리 상태</th><td>${report.status === 'REVIEWED' ? '처리됨' : '기각됨'}</td></tr>
                  <tr><th>제재 조치</th><td>${report.actionTaken}</td></tr>
                  <tr><th>처리자</th><td>${report.reviewedByAdminName} (${report.reviewedByAdminEmail})</td></tr>
                  <tr><th>처리일</th><td>${report.reviewedAt}</td></tr>
                </table>
              </div>
            `;
        }

        const html = `
          <div class="report-processing">
            <h2>신고 상세</h2>
            <div class="table-section">
                <p><strong>신고 사유:</strong> ${reasonText}</p>
                <table class="report-table" id="target-info">
                  <tr><td colspan="2">대상 정보를 불러오는 중...</td></tr>
                </table>
            </div>
            ${penaltyHtml}
            ${actionButtonsHtml}
          </div>
        `;

        openFloatingSidebar(html);

        // 신고 대상별 데이터 비동기 로드
        if (reportType === 'JOB_POST') {
            $.get(`/api/admin/reports/contents/posts/${targetId}`, function (res) {
                const postHtml = `
                  <tr><th>공고 ID</th><td>${res.id}</td></tr>
                  <tr><th>사용자 ID</th><td>${res.userId}</td></tr>
                  <tr><th>회사명</th><td>${res.companyName}</td></tr>
                  <tr><th>제목</th><td>${res.title}</td></tr>
                  <tr><th>설명</th><td>${res.description}</td></tr>
                `;
                $("#target-info").html(postHtml);
            }).fail(() => {
                $("#target-info").html("<tr><td colspan='2'>공고 정보를 불러오지 못했습니다.</td></tr>");
            });

        } else if (reportType === 'COMMUNITY_POST') {
            $.get(`/api/admin/reports/contents/community-posts/${targetId}`, function (res) {
                const postHtml = `
                  <tr><th>게시물 ID</th><td>${res.id}</td></tr>
                  <tr><th>작성일</th><td>${res.createdAt}</td></tr>
                  <tr><th>제목</th><td>${res.title}</td></tr>
                  <tr><th>내용</th><td>${res.content}</td></tr>
                `;
                $("#target-info").html(postHtml);
            }).fail(() => {
                $("#target-info").html("<tr><td colspan='2'>게시글 정보를 불러오지 못했습니다.</td></tr>");
            });

        } else if (reportType === 'USER') {
            $.get(`/api/admin/reports/contents/comments/${targetId}`, function (res) {
                const userHtml = `
                  <tr><th>게시물 번호</th><td>${res.postId}</td></tr>
                  <tr><th>댓글 ID</th><td>${res.id}</td></tr>
                  <tr><th>작성일</th><td>${res.createdAt}</td></tr>
                  <tr><th>댓글 내용</th><td>${res.content}</td></tr>
                `;
                $("#target-info").html(userHtml);
            }).fail(() => {
                $("#target-info").html("<tr><td colspan='2'>신고된 댓글 정보를 불러오지 못했습니다.</td></tr>");
            });
        }
    });
});




// 기각 처리
$(document).on("click", ".dismiss-btn", function () {
    const reportId = $(this).data("id");
    if (!confirm("이 신고를 기각하시겠습니까?")) return;

    $.ajax({
        url: `/api/admin/reports/${reportId}/dismiss`,
        method: "POST",
        success: function () {
            alert("기각 처리 완료");
            closeFloatingSidebar();
            loadReportList(); // 목록 새로고침
        },
        error: function () {
            alert("처리 중 오류 발생");
        }
    });
});

// 적용 처리
$(document).on("click", ".apply-btn", function () {
    const reportId = $(this).data("id");
    const action = $("input[name='action']:checked").val();

    if (!action) {
        alert("제재 방식을 선택해주세요.");
        return;
    }

    $.ajax({
        url: `/api/admin/reports/${reportId}/apply`,
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({ action: action }),
        success: function () {
            alert("적용 완료");
            closeFloatingSidebar();
            loadReportList();
        },
        error: function () {
            alert("처리 중 오류 발생");
        }
    });
});
