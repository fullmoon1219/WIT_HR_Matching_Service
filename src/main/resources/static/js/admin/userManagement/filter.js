// /js/admin/userManagement/filter.js

// 현재 필터 상태 저장용
const currentFilters = {
    role: '',
    status: '',
    warning: '',
    verified: '',
    keyword: ''
};

// ▼ 클릭 시 팝업 열기
$(document).on('click', '.filter-toggle', function (e) {
    e.stopPropagation();
    const type = $(this).data('filter');
    const popup = $(`.filter-popup[data-type="${type}"]`);

    $('.filter-popup').not(popup).hide(); // 다른 팝업 닫기
    popup.toggle();
});

$(document).on('click', '.filter-popup button', function () {
    const type = $(this).closest('.filter-popup').data('type');
    const value = $(this).data('value');
    const label = $(this).text();

    currentFilters[type] = value;

    // UI만 먼저 업데이트 (닫는 건 나중에)
    const $selected = $(`.filter-selected[data-type="${type}"]`);
    if (value === '') {
        $selected.hide().text('');
    } else {
        const newClass = `filter-selected value ${getFilterClass(type, value)}`;
        $selected
            .attr('class', newClass)
            .text(label)
            .show();
    }

    // 이걸 loadUserList 다음으로 미룸 (닫힘 유지됨)
    setTimeout(() => {
        $('.filter-popup').hide();
    }, 50);

    loadUserList(1, currentFilters);
});



function getFilterClass(type, value) {
    if (type === 'warning') return `warning-${value}`;
    if (type === 'status') return value.toLowerCase();
    if (type === 'role') return value.toLowerCase();
    if (type === 'verified') return value.toString() === 'true' ? 'true' : 'false';
    return '';
}

// 외부 클릭 시 닫기
$(document).on('click', function () {
    $('.filter-popup').hide();
});

// 검색 버튼 클릭 시 keyword 필터 적용
$(document).on('click', '#searchButton', function () {
    const keyword = $('#searchKeyword').val().trim();
    currentFilters.keyword = keyword;
    loadUserList(1, currentFilters);
});

// 엔터 키 입력 시 검색 실행
$(document).on('keydown', '#searchKeyword', function (e) {
    if (e.key === 'Enter') {
        $('#searchButton').click();
    }
});

$(document).on("click", "#deleteButton", function () {
    if (!confirm("정말로 선택한 사용자를 삭제하시겠습니까?")) {
        return;
    }

    const selectedUserIds = $(".user-checkbox:checked").map(function () {
        return $(this).val();
    }).get();

    if (selectedUserIds.length === 0) {
        alert("삭제할 사용자를 선택해주세요.");
        return;
    }

    $.ajax({
        url: "/api/admin/users",  // ✅ RESTful 경로로 수정
        method: "DELETE",
        contentType: "application/json",
        data: JSON.stringify(selectedUserIds),
        success: function () {
            alert("삭제가 완료되었습니다.");
            loadUserList(); // ✅ 목록 재조회 함수
        },
        error: function () {
            alert("삭제 중 오류가 발생했습니다.");
        }
    });
});
