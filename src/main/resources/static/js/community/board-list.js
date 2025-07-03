// /js/community/board-list.js

// 조회수 증가 함수
function incrementViewCount(postId) {
    // 서버에 조회수 증가 요청
    $.ajax({
        url: `/community/posts/${postId}/view-count`,
        method: "POST",
        success: function(response) {
            console.log('조회수 증가 완료');
        },
        error: function(xhr) {
            console.error('조회수 증가 실패:', xhr);
        }
    });
}

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
        console.log('게시글 ID:', postId, '클릭됨');

        // 조회수 증가 처리
        incrementViewCount(postId);

        $.ajax({
            url: `/community/posts/view/${postId}`,
            method: "GET",
            success: function (html) {
                $("#floatingSidebarContent").html(html);
                $("#floatingSidebar").addClass("show");
                $("#floatingOverlay").addClass("show"); // 오버레이 표시
            },
            error: function (xhr) {
                console.error('게시글 조회 실패:', xhr);
                alert("게시글을 불러오지 못했습니다.");
            }
        });
    });
});