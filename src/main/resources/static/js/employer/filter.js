// /js/employer/filter.js

// 필터 상태 저장 객체
const currentFilters = {
    status: ''
};

// ▼ 클릭 시 팝업 열기
$(document).on('click', '.filter-toggle', function (e) {
    e.stopPropagation();
    const type = $(this).data('filter');
    const popup = $(this).closest('th').find(`.filter-popup[data-type="${type}"]`);


    $('.filter-popup').not(popup).hide();
    popup.toggle();
});

// 필터 버튼 클릭 시 필터 적용
$(document).on('click', '.filter-popup button', function () {
    const type = $(this).closest('.filter-popup').data('type');
    const value = $(this).data('value');
    const label = $(this).text();

    currentFilters[type] = value;

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

    setTimeout(() => {
        $('.filter-popup').hide();
    }, 50);

    applyApplicantFilter(); // 필터 적용 함수 호출
});

// 외부 클릭 시 팝업 닫기
$(document).on('click', function () {
    $('.filter-popup').hide();
});

// 상태 필터 클래스 변환 함수
function getFilterClass(type, value) {
    if (type === 'status') return value.toLowerCase();
    return '';
}

// 테이블 필터링 로직 (프론트에서만 동작)
function applyApplicantFilter() {
    const selectedStatus = currentFilters.status;

    $('tbody tr').each(function () {
        const statusClass = $(this).find('.status-label').attr('class') || ''; // ex: "status-label read"
        const statusMatch = statusClass.split(' ').find(cls =>
            ['unread', 'read', 'waiting', 'accepted', 'rejected'].includes(cls)
        );

        if (!selectedStatus || selectedStatus === statusMatch) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });
}