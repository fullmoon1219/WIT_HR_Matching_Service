function renderPagination(totalPages, currentPage, onPageClick) {
    let html = '';
    const maxVisiblePages = 5;

    if (totalPages === 0) {
        $('#paginationContainer').html('');
        return;
    }

    const startPage = Math.floor((currentPage - 1) / maxVisiblePages) * maxVisiblePages + 1;
    const endPage = Math.min(startPage + maxVisiblePages - 1, totalPages);

    html += `<button class="pagination-btn" data-page="1">&lt;&lt;</button>`;
    const prevBlockPage = Math.max(startPage - maxVisiblePages, 1);
    html += `<button class="pagination-btn" data-page="${prevBlockPage}">&lt;</button>`;

    for (let i = startPage; i <= endPage; i++) {
        html += `<button class="pagination-btn ${i === currentPage ? 'active' : ''}" data-page="${i}">${i}</button>`;
    }

    const nextBlockPage = Math.min(startPage + maxVisiblePages, totalPages);
    html += `<button class="pagination-btn" data-page="${nextBlockPage}">&gt;</button>`;
    html += `<button class="pagination-btn" data-page="${totalPages}">&gt;&gt;</button>`;

    $('#paginationContainer').html(html);

    // ✅ 등록된 버튼에 이벤트 바인딩
    $('#paginationContainer .pagination-btn').off('click').on('click', function () {
        const page = $(this).data('page');
        if (typeof onPageClick === 'function') {
            onPageClick(page);
        }
    });
}