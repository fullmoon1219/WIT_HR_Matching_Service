// /js/community/board-modify.js

$(document).ready(function () {
    const $select = $("#board");
    const $desc = $("#board-description");
    const $form = $("form");
    const postId = $("#postId").val();
    
    // 페이지 로딩 시 원본 데이터 저장
    const originalTitle = $("#title").val();
    const originalContent = $("#content").val();
    const originalBoardId = $("#board").val();

    // 페이지 로딩 시 description 초기 설정
    const initialId = $select.val();
    if (boardMap[initialId]) {
        $desc.text(boardMap[initialId].description || "");
    }

    // 게시판 변경 시 description 변경
    $select.on("change", function () {
        const selectedId = $(this).val();
        const selectedBoard = boardMap[selectedId];
        if (selectedBoard) {
            $desc.text(selectedBoard.description || "");
        }
    });
    
    // 데이터 변경 여부 확인
    function isDataChanged() {
        return originalTitle !== $("#title").val().trim() || 
               originalContent !== $("#content").val().trim() ||
               originalBoardId !== $("#board").val();
    }
    
    // 등록 버튼 클릭 시 확인 대화상자 표시
    $form.on("submit", function(e) {
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
        if (confirm("게시물을 수정하시겠습니까?")) {
            // 확인을 누르면 폼 제출
            this.submit();
        }
    });

    // 취소 버튼 클릭 시 확인 대화상자 표시
    $("#cancelModify").on("click", function () {
        // 게시물 데이터가 변경되었는지 확인
        if (isDataChanged()) {
            if (!confirm("게시물 수정을 취소하시겠습니까?")) {
                return;
            }
        }
        
        if (!postId) {
            alert("게시글 정보가 없습니다.");
            return;
        }

        // 플로팅 사이드바가 사용된 경우
        if ($("#floatingSidebar").length) {
            $.ajax({
                url: `/community/posts/view/${postId}`,
                method: "GET",
                success: function (html) {
                    $("#floatingSidebarContent").html(html);
                },
                error: function () {
                    alert("게시글 상세 페이지를 불러오지 못했습니다.");
                }
            });
        } else {
            // 일반 페이지인 경우 이전 페이지로 돌아가기
            history.back();
        }
    });
});