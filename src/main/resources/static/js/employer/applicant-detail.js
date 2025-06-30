// /js/employer/applicant-detail.js

$(document).ready(function () {
    // [🔒 닫기] 버튼 클릭
    $(document).on('click', '.action-button.close', function () {
        closeFloatingSidebar();
    });

    // ESC 키 누르면 닫기
    $(document).on('keydown', function (e) {
        if (e.key === "Escape") {
            closeFloatingSidebar();
        }
    });

    // 오버레이 더블 클릭 시 닫기
    $(document).on('dblclick', '#floatingOverlay', function () {
        closeFloatingSidebar();
    });

    // 플로팅 사이드바 닫기 함수
    function closeFloatingSidebar() {
        $('#floatingOverlay').removeClass('show');
        $('#floatingSidebar').removeClass('show');
        $('#floatingSidebarContent').empty();
    }
});
