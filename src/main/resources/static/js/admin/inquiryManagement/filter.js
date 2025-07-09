// /js/admin/inquiryManagement/filter.js

const currentFilters = {
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
$(document).on('click', '.filter-popup button', function () {
    const type = $(this).closest('.filter-popup').data('type');
    const value = $(this).data('value');
    const label = $(this).text();

    currentFilters[type] = value;

    const $selected = $(`.filter-selected[data-type="${type}"]`);
    if (value === '') {
        $selected.hide().text('');
    } else {
        $selected
            .attr('class', `filter-selected value ${value.toLowerCase()}`)
            .text(label)
            .show();
    }

    setTimeout(() => {
        $('.filter-popup').hide();
    }, 50);

    loadInquiryList(1, currentFilters);
});

// 검색
$(document).on('click', '#searchButton', function () {
    const keyword = $('#searchKeyword').val().trim();
    currentFilters.keyword = keyword;
    loadInquiryList(1, currentFilters);
});

$(document).on('keydown', '#searchKeyword', function (e) {
    if (e.key === 'Enter') {
        $('#searchButton').click();
    }
});

// 일괄 삭제
$(document).on("click", "#deleteButton", function () {
    if (!confirm("정말로 선택한 문의를 삭제하시겠습니까?")) return;

    const selectedIds = $(".inquiry-checkbox:checked").map(function () {
        return $(this).val();
    }).get();

    if (selectedIds.length === 0) {
        alert("삭제할 문의를 선택해주세요.");
        return;
    }

    $.ajax({
        url: "/api/admin/inquiries",
        method: "DELETE",
        contentType: "application/json",
        data: JSON.stringify(selectedIds),
        success: function () {
            alert("삭제가 완료되었습니다.");
            loadInquiryList();
        },
        error: function () {
            alert("삭제 중 오류가 발생했습니다.");
        }
    });
});

// 외부 클릭 시 닫기
$(document).on('click', function () {
    $('.filter-popup').hide();
});