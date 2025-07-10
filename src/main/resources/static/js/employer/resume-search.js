$(document).ready(function () {
    // 드롭다운 열기
    $('.dropdown-toggle').on('click', function (e) {
        e.stopPropagation();

        const current = $(this).next('.dropdown-content');

        // 다른 드롭다운 닫기
        $('.dropdown-content').not(current).slideUp();

        current.slideToggle();
    });

    // ✅ 드롭다운 내부 클릭 시 닫히지 않도록
    $('.dropdown-content').on('click', function (e) {
        e.stopPropagation();
    });

    // 외부 클릭 시 닫기
    $(document).on('click', function () {
        $('.dropdown-content').slideUp();
    });

    // 체크 시 미리보기 태그 추가
    $('input[type="checkbox"]').on('change', function () {
        updateSelectedTags();
    });

    // 태그 삭제
    $(document).on('click', '.remove-tag', function () {
        const value = $(this).parent().data('value');
        const name = $(this).parent().data('name');

        $(`input[name="${name}"][value="${value}"]`).prop('checked', false);
        updateSelectedTags();
    });

    function updateSelectedTags() {
        const $container = $('#selectedTags');
        $container.empty();

        $('input[type="checkbox"]:checked').each(function () {
            const name = $(this).attr('name');
            const value = $(this).val();

            const tag = `
                <span class="tag" data-name="${name}" data-value="${value}">
                    ${value}
                    <span class="remove-tag">×</span>
                </span>
            `;
            $container.append(tag);
        });
    }
});
