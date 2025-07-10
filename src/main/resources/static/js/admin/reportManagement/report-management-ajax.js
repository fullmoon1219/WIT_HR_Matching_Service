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
                <td>${getReportTypeLabel(report.reportType)} #${report.targetId}</td>
                <td class="reporter-user-info" data-userid="${report.reporterUserId}">${report.reporterName || '-'}</td>
                <td class="reporter-user-info" data-userid="${report.reporterUserId}">${report.reporterEmail || '-'}</td>
                <td class="report-reason" data-reportid="${report.id}">${report.reason}</td>
                <td>${formatDate(report.reportDate)}</td>
                <td>${getStatusLabelWithClass(report.status)}</td>
                <td><button class="view-btn" data-id="${report.id}">보기</button></td>
            </tr>
        `;
        $tbody.append(row);
    });
}

function formatDate(dateTimeString) {
    return new Date(dateTimeString).toLocaleDateString();
}

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

