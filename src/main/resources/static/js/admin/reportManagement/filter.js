// /js/admin/reportManagement/filter.js

const currentFilters = {
    reportType: '',
    status: '',
    keyword: ''
};

// ▼ 클릭 시 필터 팝업 열기
$(document).on('click', '.filter-toggle', function (e) {
    e.stopPropagation();
    const type = $(this).data('filter');
    const popup = $(`.filter-popup[data-type="${type}"]`);
    $('.filter-popup').not(popup).hide();
    popup.toggle();
});

// 필터 버튼 클릭
$(document).on('click', '.filter-popup button', function (e) {
    e.preventDefault();
    e.stopPropagation();

    const $popup = $(this).closest('.filter-popup');
    const type = $popup.data('type');
    const value = $(this).data('value');
    const label = $(this).text();

    currentFilters[type] = value;

    const $selected = $(`.filter-selected[data-type="${type}"]`);
    if (!value) {
        $selected.hide().text('');
    } else {
        $selected
            .attr('class', `filter-selected value ${value.toLowerCase()}`)
            .text(label)
            .show();
    }

    // ✅ 클릭 즉시 팝업 닫기 (setTimeout 제거)
    $('.filter-popup').hide();

    loadReportList(1, currentFilters);
});



// 검색 버튼
$(document).on('click', '#searchButton', function () {
    const keyword = $('#searchKeyword').val().trim();
    currentFilters.keyword = keyword;
    loadReportList(1, currentFilters);
});

// 엔터 입력 시 검색
$(document).on('keydown', '#searchKeyword', function (e) {
    if (e.key === 'Enter') {
        $('#searchButton').click();
    }
});

// 일괄 삭제
$(document).on("click", "#deleteButton", function () {
    const selectedIds = $("#reportTableBody input[type='checkbox']:checked").map(function () {
        return $(this).data("id");
    }).get();

    if (selectedIds.length === 0) {
        alert("삭제할 신고를 선택해주세요.");
        return;
    }

    if (!confirm(`${selectedIds.length}건의 신고를 삭제하시겠습니까?`)) return;

    $.ajax({
        url: "/api/admin/reports",
        method: "DELETE",
        contentType: "application/json",
        data: JSON.stringify(selectedIds),
        success: function () {
            alert("삭제가 완료되었습니다.");
            loadReportStats();
            loadReportList(1, currentFilters);
        },
        error: function () {
            alert("삭제 중 오류가 발생했습니다.");
        }
    });
});

// 외부 클릭 시 필터 닫기
$(document).on('click', function () {
    $('.filter-popup').hide();
});
