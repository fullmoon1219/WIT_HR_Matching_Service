// board-view.js

$(document).ready(function () {
    // 목록 버튼 클릭 시 플로팅바 닫기
    $(document).on("click", ".back-btn", function () {
        $("#floatingSidebar").removeClass("show");
        $("#floatingOverlay").removeClass("show");
    });
});
