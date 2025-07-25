// /js/admin/inquiryManagement/inquiry-management-ajax.js

$(document).ready(function () {
    loadInquiryStats();
    loadInquiryList(1);
    loadReasonFilters();
});

function loadReasonFilters() {
    $.ajax({
        url: '/api/admin/inquiries/reasons',
        method: 'GET',
        success: function (reasons) {
            const container = $('[data-type="reasonId"]');
            container.empty();

            container.append(`<button class="default" data-value="">전체</button>`);

            reasons.forEach(reason => {
                container.append(`
                    <button class="reason-${reason.id}" data-value="${reason.id}">
                        ${reason.name}
                    </button>
                `);
            });

            const selectedValue = currentFilters.reasonId;
            if (selectedValue) {
                container.find(`button[data-value="${selectedValue}"]`).addClass('selected');
            }
        }
    });
}



function loadInquiryStats() {
    $.ajax({
        url: '/api/admin/inquiries/stats',
        method: 'GET',
        success: function (data) {
            $('#inquiryCount').text(data.totalCount);
            $('#unansweredCount').text(data.unansweredCount);
            $('#answeredCount').text(data.answeredCount);
        }
    });
}

function getStatusLabel(status) {
    switch (status) {
        case 'ANSWERED':
            return '답변 완료';
        case 'WAITING':
            return '답변 대기';
        default:
            return status;
    }
}

function loadInquiryList(page = 1, filters = currentFilters) {
    const query = $.param({
        page: page,
        status: filters.status,
        keyword: filters.keyword,
        reasonId: filters.reasonId
    });

    $.ajax({
        url: `/api/admin/inquiries?${query}`,
        method: 'GET',
        success: function (response) {
            const inquiries = response.content;
            const totalPages = response.totalPages;
            const currentPage = response.number;

            let html = '';
            inquiries.forEach(inquiry => {
                html += `
                    <tr>
                        <td><input type="checkbox" class="inquiry-checkbox" value="${inquiry.id}"></td>
                        <td>${inquiry.id}</td>
                        <td>${inquiry.reasonName ?? '-'}</td>
                        <td>${inquiry.title}</td>
                        <td>${inquiry.name}</td>
                        <td>${inquiry.email}</td>
                        <td>${formatDate(inquiry.createdAt)}</td>
                        <td>${inquiry.status === 'ANSWERED' ? formatDate(inquiry.repliedAt) : '-'}</td>
                        <td>
                            <div class="value-wrapper status-wrapper">
                                <span class="value status ${inquiry.status.toLowerCase()}" data-id="${inquiry.id}" data-type="status">
                                    ${getStatusLabel(inquiry.status)}
                                </span>
                            </div>
                        </td>
                        <td style="text-align: center;">
                            <button class="toggle-details-btn" data-id="${inquiry.id}" style="width: 30px;">▼</button>
                        </td>
                    </tr>
                    <tr class="detail-row row-${inquiry.id}">
                        <td colspan="9" style="padding: 0; border: none;">
                            <div class="detail-table-wrapper" style="display: none;">
                                <table class="inquiry-detail-table">
                                    <tr>
                                        <th>문의 내용</th>
                                        <th>답변</th>
                                    </tr>
                                    <tr>
                                        <td class="inquiry-content">
                                            <p style="white-space: pre-wrap;">${escapeHtml(inquiry.content)}</p>
                                        </td>
                                        <td class="inquiry-reply">
                                            ${inquiry.status === 'ANSWERED' ? `
                                                <div class="reply-card">
                                                    <div class="reply-view-box" data-id="${inquiry.id}">
                                                        <p class="reply-text">${escapeHtml(inquiry.reply)}</p>
                                                        <div class="reply-action-btns">
                                                            <button class="reply-edit-btn" data-id="${inquiry.id}">수정</button>
                                                            <button class="reply-delete-btn" data-id="${inquiry.id}">삭제</button>
                                                        </div>
                                                    </div>
                                                    <div class="reply-edit-area" id="edit-form-${inquiry.id}" style="display: none;">
                                                        <textarea id="edit-reply-input-${inquiry.id}" class="reply-textarea">${escapeHtml(inquiry.reply)}</textarea>
                                                        <div class="reply-submit-btns">
                                                            <button class="reply-save-btn" data-id="${inquiry.id}">저장</button>
                                                            <button class="reply-cancel-btn" data-id="${inquiry.id}">취소</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            ` : `
                                                <div class="reply-card">
                                                    <textarea id="replyInput-${inquiry.id}" class="reply-textarea" placeholder="답변을 입력하세요"></textarea>
                                                    <div class="reply-submit-btns">
                                                        <button class="reply-submit-btn" data-id="${inquiry.id}">등록</button>
                                                    </div>
                                                </div>
                                            `}
                                        </td>

                                    </tr>
                                </table>
                            </div>
                        </td>
                    </tr>

                `;
            });

            $('#inquiryTableBody').html(html);
            renderPagination(totalPages, currentPage, function (page) {
                loadInquiryList(page, filters);
            });
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

function truncate(str, maxLength) {
    return str.length > maxLength ? str.slice(0, maxLength) + '...' : str;
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}
