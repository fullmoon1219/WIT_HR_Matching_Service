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

// 팝업 버튼 클릭 시 필터 적용
$(document).on('click', '.filter-popup button', function () {
    const type = $(this).parent().data('type');  // 예: 'verified'
    const value = $(this).data('value');         // 예: 'true' or 'false'
    const label = $(this).text();                // 예: '인증됨'

    currentFilters[type] = value;
    $('.filter-popup').hide();

    const $selected = $(`.filter-selected[data-type="${type}"]`);

    if (value === '') {
        $selected.hide().text('');
    } else {
        const newClass = `filter-selected value ${getFilterClass(type, value)}`;
        $selected
            .attr('class', newClass)
            .text(label)
            .show();

        // ✅ 로그 출력
        console.log(`[필터 선택됨] type: ${type}, value: ${value}, class: ${newClass}, text: ${label}`);
    }

    loadUserList(1, currentFilters); // 필터 반영된 데이터 로딩
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
