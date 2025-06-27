// /js/employer/post-view.js

$(document).ready(function () {
    // 닫기 버튼 클릭
    $(document).on('click', '.close-button', function () {
        closeFloatingSidebar();
    });

    // ESC 키 누를 때 닫기
    $(document).on('keydown', function (e) {
        if (e.key === "Escape") {
            closeFloatingSidebar();
        }
    });

    // 오버레이 영역 더블 클릭 시 닫기
    $(document).on('dblclick', '#floatingOverlay', function () {
        closeFloatingSidebar();
    });

    // 플로팅바 닫기 함수
    function closeFloatingSidebar() {
        $('#floatingOverlay').removeClass('show');
        $('#floatingSidebar').removeClass('show');
        $('#floatingSidebarContent').empty();
    }

    $(document).on('click', '.edit-button', function () {
        const postId = $(this).data('id'); // 버튼에 data-id 속성이 있어야 함

        // 플로팅바 열기
        $.ajax({
            url: `/employer/test/modify`, // 수정 페이지 URL (컨트롤러에서 매핑 필요)
            method: 'GET',
            success: function (html) {
                $('#floatingSidebarContent').html(html);
                $('#floatingOverlay').addClass('show');
                $('#floatingSidebar').addClass('show');
            },
            error: function () {
                alert('수정 페이지를 불러오지 못했습니다.');
            }
        });
    });
});
