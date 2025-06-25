// /js/admin/admin-template.js

function openFloatingSidebar(contentHtml) {
    $("#floatingSidebarContent").html(contentHtml);
    $("#floatingSidebar").addClass("show");
}

function closeFloatingSidebar() {
    $("#floatingSidebar").removeClass("show");
}

// 닫기 버튼 클릭 시 닫기
$("#closeFloatingSidebar").on("click", function () {
    closeFloatingSidebar();
});

// 플로팅 바 외부 클릭 시 닫기 (버튼 같은 요소 클릭 시 무시)
$(document).on("mousedown", function (e) {
    const $sidebar = $("#floatingSidebar");
    const $content = $(".floating-sidebar-inner");

    // 플로팅바가 열려있고, 클릭한 위치가 플로팅바 내부가 아니며, 클릭한 요소가 버튼도 아닐 때
    if (
        $sidebar.hasClass("show") &&
        !$content.is(e.target) &&
        $content.has(e.target).length === 0 &&
        !$(e.target).is("button") &&
        !$(e.target).closest("button").length &&
        !$(e.target).is("a") &&
        !$(e.target).closest("a").length &&
        !$(e.target).is("input") &&
        !$(e.target).closest("input").length
    ) {
        closeFloatingSidebar();
    }
});

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