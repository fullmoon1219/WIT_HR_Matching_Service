let isConfirming = false;

function handleCancelClick(e) {
    e.preventDefault();

    if (isConfirming) return;
    isConfirming = true;

    const hasChanges = $('#floatingSidebarContent')
        .find('input, textarea, select')
        .toArray()
        .some(el => el.value !== el.defaultValue);

    if (hasChanges) {
        const confirmed = confirm('작성 중인 내용을 취소하시겠습니까?');
        if (!confirmed) {
            isConfirming = false;
            return;
        }
    }

    $.ajax({
        url: `/employer/jobpost_list`,
        method: 'GET',
        success: function (html) {
            $('#floatingSidebarContent').html(html);
            $('#floatingOverlay').removeClass('show');
            $('#floatingSidebar').removeClass('show');
        },
        error: function () {
            alert('리스트 페이지를 불러오는 데 실패했습니다.');
        },
        complete: function () {
            isConfirming = false;
        }
    });
}

$(document).ready(function () {
    // -------- 이벤트 중복 방지 --------
    $(document).off('click.cancel').on('click.cancel', '.cancel-button', handleCancelClick);

    // -------- 기술 스택 선택 기능 --------
    const selectedStacks = new Set(
        $('#techStacksInput').val().split(',').map(v => v.trim()).filter(v => v !== "")
    );

    // 초기 선택된 버튼에 selected 클래스 부여
    $('.stack-tag').each(function () {
        const value = $(this).data('value')?.toString(); // ← 이 value는 숫자형 ID 문자열
        if (selectedStacks.has(value)) {
            $(this).addClass('selected');
        }
    });

    // 클릭 시 toggle
    $(document).off('click.stack').on('click.stack', '.stack-tag', function () {
        const value = $(this).data('value')?.toString();

        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            selectedStacks.delete(value);
        } else {
            $(this).addClass('selected');
            selectedStacks.add(value);
        }

        $('#techStacksInput').val(Array.from(selectedStacks).join(','));
    });

});