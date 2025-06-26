// board-view.js

$(document).ready(function () {
    // 목록 버튼 클릭 시 플로팅바 닫기
    $(document).on("click", ".back-btn", function () {
        $("#floatingSidebar").removeClass("show");
        $("#floatingOverlay").removeClass("show");
    });

    $(document).on("click", ".edit-btn", function () {
        const postId = $(this).data("id");  // 현재는 1로 하드코딩됨
        const boardCode = $(this).data("board-code");

        if (!postId || !boardCode) {
            alert("필요한 정보가 부족합니다.");
            return;
        }

        $.ajax({
            url: `/community/${boardCode}/edit/${postId}`,
            method: "GET",
            success: function (html) {
                $("#floatingSidebarContent").html(html);
                $("#floatingSidebar").addClass("show");
                $("#floatingOverlay").addClass("show");
            },
            error: function () {
                alert("수정 페이지를 불러오지 못했습니다.");
            }
        });
    });

});
