// /js/community/board-write.js

$(document).ready(function () {
    const $select = $("#board");
    const $desc = $("#board-description");

    // ✅ 페이지 로딩 시 description 초기 설정
    const initialId = $select.val();
    if (boardMap[initialId]) {
        $desc.text(boardMap[initialId].description || "");
    }

    // ✅ 게시판 변경 시 description 변경
    $select.on("change", function () {
        const selectedId = $(this).val();
        const selectedBoard = boardMap[selectedId];
        if (selectedBoard) {
            $desc.text(selectedBoard.description || "");
        }
    });

    $("#cancelModify").on("click", function () {
        const postId = $("#postId").val();

        if (!postId) {
            alert("게시글 ID가 없습니다.");
            return;
        }

        $.ajax({
            url: `/community/posts/view/${postId}`,
            method: "GET",
            success: function (html) {
                $("#floatingSidebarContent").html(html);
                $("#floatingSidebar").addClass("show");
                $("#floatingOverlay").addClass("show");
            },
            error: function () {
                alert("게시글 상세 페이지를 불러오지 못했습니다.");
            }
        });
    });
});
