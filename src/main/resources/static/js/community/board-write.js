// /js/community/board-write.js

$(document).ready(function () {
    const $select = $("#board");
    const $desc = $("#board-description");
    const $form = $("form");

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

    // 등록 버튼 클릭 시 확인 대화상자 표시
    $("#submitPost").on("click", function (e) {
        e.preventDefault(); // 폼 제출 방지
        
        // 필드 유효성 검사
        const title = $("#title").val().trim();
        const content = $("#content").val().trim();
        
        if (!title) {
            alert("제목을 입력해주세요.");
            $("#title").focus();
            return;
        }
        
        if (!content) {
            alert("내용을 입력해주세요.");
            $("#content").focus();
            return;
        }
        
        // 확인 대화상자 표시
        if (confirm("게시물을 등록하시겠습니까?")) {
            // 확인을 누르면 폼 제출
            $form.submit();
        }
    });

    // 취소 버튼 클릭 시 확인 대화상자 표시
    $("#cancelWrite").on("click", function () {
        const hasInput = $("#title").val().trim() !== "" || $("#content").val().trim() !== "";
        
        if (hasInput) {
            if (!confirm("게시물 작성을 취소하시겠습니까?")) {
                return;
            }
        }

        // 플로팅 사이드바가 사용된 경우
        if ($("#floatingSidebar").length) {
            $("#floatingSidebar").removeClass("show");
            $("#floatingOverlay").removeClass("show");
            $("#floatingSidebarContent").empty();
        } else {
            // 일반 페이지인 경우 이전 페이지로 돌아가기
            history.back();
        }
    });
});