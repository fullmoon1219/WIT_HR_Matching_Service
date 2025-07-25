// /js/admin/reportManagement/report-management-ajax.js

let currentPage = 1;

$(document).ready(function () {
    loadReportStats();
    loadReportList();
});

function loadReportStats() {
    $.ajax({
        url: '/api/admin/reports/stats',
        method: 'GET',
        success: function (data) {
            $('#reportCount').text(data.totalCount);
            $('#pendingCount').text(data.pendingCount);
            $('#reviewedCount').text(data.reviewedCount);
        }
    });
}

function loadReportList(page = 1, filters = currentFilters) {
    currentPage = page;

    const query = $.param({
        page: page,
        size: 10,
        reportType: filters.reportType,
        status: filters.status,
        keyword: filters.keyword
    });

    $.ajax({
        url: `/api/admin/reports?${query}`,
        method: 'GET',
        success: function (data) {
            renderReportTable(data.content);

            // ✅ 여기에서 공통 renderPagination 호출
            renderPagination(data.totalPages, data.number, (selectedPage) => {
                loadReportList(selectedPage, currentFilters);
            });
        }
    });
}

function renderReportTable(reportList) {
    const $tbody = $('#reportTableBody');
    $tbody.empty();

    if (reportList.length === 0) {
        $tbody.append('<tr><td colspan="9">신고 내역이 없습니다.</td></tr>');
        return;
    }

    reportList.forEach(report => {
        const row = `
            <tr>
                <td><input type="checkbox" data-id="${report.id}"></td>
                <td>${report.id}</td>
                <td>
                  <a href="#" class="target-link"
                     data-type="${report.reportType}"
                     data-id="${report.targetId}">
                     ${getReportTypeLabel(report.reportType)} #${report.targetId}
                  </a>
                </td>

                <td class="reporter-user-info" data-userid="${report.reporterUserId}">${report.reporterName || '-'}</td>
                <td class="reporter-user-info" data-userid="${report.reporterUserId}">${report.reporterEmail || '-'}</td>
                <td class="report-reason"
                    data-reportid="${report.id}"
                    data-reporttype="${report.reportType}"
                    data-targetid="${report.targetId}">
                    ${report.reason}
                </td>

                <td>${formatDate(report.reportDate)}</td>
                <td>${getStatusLabelWithClass(report.status)}</td>
            </tr>
        `;
        $tbody.append(row);
    });
}

function formatDate(dateTimeString) {
    return new Date(dateTimeString).toLocaleDateString();
}

$(document).on("click", ".target-link", function (e) {
    e.preventDefault();

    const type = $(this).data("type");
    const id = $(this).data("id");

    if (type === 'JOB_POST') {
        // 예: /jobs/공고번호
        window.open(`/recruit/view/${id}`, '_blank');
    } else if (type === 'COMMUNITY_POST') {
        // 예: /community/view/게시물번호
        window.open(`/community/all/view/${id}`, '_blank');
    } else if (type === 'USER') {
        // 댓글 ID로 댓글 조회 → 댓글의 postId를 얻고 이동
        $.get(`/api/admin/reports/contents/comments/${id}`, function (res) {
            if (res && res.postId) {
                window.open(`/community/all/view/${res.postId}#comment-${res.id}`, '_blank');
            } else {
                alert("댓글 정보가 존재하지 않습니다.");
            }
        }).fail(() => {
            alert("댓글 정보를 불러오지 못했습니다.");
        });
    }
});


function getStatusLabelWithClass(status) {
    switch (status) {
        case 'PENDING':
            return `<span class="status-label pending">검토 전</span>`;
        case 'REVIEWED':
            return `<span class="status-label reviewed">처리 완료</span>`;
        case 'DISMISSED':
            return `<span class="status-label dismissed">기각됨</span>`;
        default:
            return `<span class="status-label">${status}</span>`;
    }
}

function getReportTypeLabel(type) {
    switch (type) {
        case 'USER':
            return '사용자';
        case 'JOB_POST':
            return '공고';
        case 'COMMUNITY_POST':
            return '게시물';
        default:
            return type;
    }
}

