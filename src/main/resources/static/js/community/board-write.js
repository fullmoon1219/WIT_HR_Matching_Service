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

    $("#cancelWrite").on("click", function () {
        const hasInput = $("#title").val().trim() !== "" || $("#content").val().trim() !== "";
        if (hasInput) {
            if (!confirm("작성 중인 내용이 삭제됩니다. 정말 닫으시겠습니까?")) return;
        }

        $("#floatingSidebar").removeClass("show");
        $("#floatingOverlay").removeClass("show");
        $("#floatingSidebarContent").empty();
    });
});
