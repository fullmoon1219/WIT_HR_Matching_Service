// /js/employer/post-modify.js

$(document).ready(function () {
    // 수정 취소 버튼 클릭 시 뷰 페이지 로드
    $(document).on('click', '.cancel-button', function (e) {
        e.preventDefault();

        // 수정 중일 경우 확인
        const hasChanges = $('#floatingSidebarContent')
            .find('input, textarea, select')
            .toArray()
            .some(el => el.value !== el.defaultValue);

        if (hasChanges && !confirm('작성 중인 내용을 취소하시겠습니까?')) {
            return;
        }

        // const postId = $(this).data('id');
        // if (!postId) {
        //     alert("공고 ID가 없습니다.");
        //     return;
        // }

        // 뷰 페이지로 다시 이동
        $.ajax({
            url: `/employer/test/view`,
            method: 'GET',
            success: function (html) {
                $('#floatingSidebarContent').html(html);
            },
            error: function () {
                alert('상세 페이지를 불러오는 데 실패했습니다.');
            }
        });
    });
});
