// /js/admin/reportManagement/report-processing.js

// 신고 사유 클릭 시 플로팅 바 열기
$(document).on("click", ".report-reason", function () {
    const reportId = $(this).data("reportid");
    const reasonText = $(this).text();

    const html = `
        <div class="report-processing">
            <h2>신고 상세</h2>
            <p>${reasonText}</p>
            <div class="report-actions">
                <label><input type="radio" name="action" value="WARNING"> 경고</label>
                <label><input type="radio" name="action" value="SUSPEND"> 계정 정지</label>
            </div>
            <div class="action-buttons">
                <button class="dismiss-btn" data-id="${reportId}">기각</button>
                <button class="apply-btn" data-id="${reportId}">적용</button>
            </div>
        </div>
    `;

    openFloatingSidebar(html);
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
