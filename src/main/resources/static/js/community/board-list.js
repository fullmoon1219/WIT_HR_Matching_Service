// /js/community/board-list.js

$(document).ready(function () {
    // 글쓰기 버튼 클릭 시 플로팅 바 열기 (헤더에 추가한 버튼)
    $("#write-post-btn").on("click", function () {
        const boardCode = $("#board-category").val() || "free"; // 현재 선택된 게시판 코드 또는 기본값 "free"
        
        $.ajax({
            url: `/community/${boardCode}/write`,
            method: "GET",
            success: function (html) {
                $("#floatingSidebarContent").html(html);
                $("#floatingSidebar").addClass("show");
                $("#floatingOverlay").addClass("show"); // 오버레이 표시
            },
            error: function () {
                alert("글쓰기 화면을 불러오지 못했습니다.");
            }
        });
    });
    
    // 기존 글쓰기 버튼 클릭 시 플로팅 바 열기
    $(".write-button").on("click", function () {
        const boardCode = $(this).data("board-code");

        $.ajax({
            url: `/community/${boardCode}/write`,
            method: "GET",
            success: function (html) {
                $("#floatingSidebarContent").html(html);
                $("#floatingSidebar").addClass("show");
                $("#floatingOverlay").addClass("show"); // 오버레이 표시
            },
            error: function () {
                alert("글쓰기 화면을 불러오지 못했습니다.");
            }
        });
    });

    // 제목 클릭 시 뷰 페이지 플로팅 바 열기
    $(document).on("click", ".post-link", function (e) {
        e.preventDefault();

        const postId = $(this).data("id");

        $.ajax({
            url: `/community/posts/view/${postId}`,  // 현재는 하드코딩된 뷰
            method: "GET",
            success: function (html) {
                $("#floatingSidebarContent").html(html);
                $("#floatingSidebar").addClass("show");
                $("#floatingOverlay").addClass("show");
            },
            error: function () {
                alert("게시글을 불러오지 못했습니다.");
            }
        });
    });
});