// /js/admin/jobPosts/filter.js

// 현재 필터 상태 저장용
const currentFilters = {
    id: '',
    isClosed: '',
    isDeleted: '',
    keyword: ''
};

// ▼ 클릭 시 해당 필터 팝업 열기
$(document).on('click', '.filter-toggle', function (e) {
    e.stopPropagation();
    const type = $(this).data('filter');
    const popup = $(`.filter-popup[data-type="${type}"]`);

    $('.filter-popup').not(popup).hide();
    popup.toggle();
});

// 팝업 버튼 클릭 시 필터 적용
$(document).on('click', '.filter-popup button', function () {
    const type = $(this).parent().data('type');  // isClosed / isDeleted
    const value = $(this).data('value');
    const label = $(this).text();

    currentFilters[type] = value;

    const $selected = $(`.filter-selected[data-type="${type}"]`);

    if (value === '') {
        $selected.hide().text('');
    } else {
        const newClass = `filter-selected value ${value}`;
        $selected
            .attr('class', newClass)
            .text(label)
            .show();
    }

    setTimeout(() => {
        $('.filter-popup').hide();
    }, 50);

    currentPage = 1;
    loadJobPosts();
});

// 키워드 검색
$(document).on('click', '#searchButton', function () {
    const keyword = $('#searchKeyword').val().trim();
    currentFilters.keyword = keyword;
    currentPage = 1;
    loadJobPosts();
});

$(document).on('keydown', '#searchKeyword', function (e) {
    if (e.key === 'Enter') {
        $('#searchButton').click();
    }
});

// 외부 클릭 시 모든 팝업 닫기
$(document).on('click', function () {
    $('.filter-popup').hide();
});

// 삭제 버튼 클릭
$(document).on('click', '#deleteButton', function () {
    const selectedPostIds = $(".user-checkbox:checked").map(function () {
        return $(this).val();
    }).get();

    if (selectedPostIds.length === 0) {
        alert("삭제할 공고를 선택해주세요.");
        return;
    }

    if (!confirm("정말로 선택한 공고를 삭제하시겠습니까?")) {
        return;
    }

    $.ajax({
        url: "/api/admin/posts",
        method: "DELETE",
        contentType: "application/json",
        data: JSON.stringify(selectedPostIds),
        success: function () {
            alert("공고가 삭제되었습니다.");
            loadJobPosts();
        },
        error: function () {
            alert("삭제 중 오류가 발생했습니다.");
        }
    });
});
