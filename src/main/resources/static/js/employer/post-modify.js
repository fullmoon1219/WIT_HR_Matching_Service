// /js/employer/post-modify.js

$(document).ready(function () {
    // 수정 취소 버튼 클릭 시 뷰 페이지 로드
    $(document).on('click', '.cancel-button', function (e) {
        e.preventDefault();

        const hasChanges = $('#floatingSidebarContent')
            .find('input, textarea, select')
            .toArray()
            .some(el => el.value !== el.defaultValue);

        if (hasChanges && !confirm('작성 중인 내용을 취소하시겠습니까?')) {
            return;
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
            }
        });
    });
});
