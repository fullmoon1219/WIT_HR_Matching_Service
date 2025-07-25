// /js/community/board-write.js

var editor;
var uploadedTempImages = [];

$(document).ready(function () {
    const $select = $("#board");
    const $desc = $("#board-description");

    $(document).on("dragstart", ".toastui-editor-contents img", function (e) {
        e.preventDefault();
    });

    $(document).on("click", ".toastui-editor-contents img", function () {
        if (confirm("이미지를 삭제하시겠습니까?")) {
            this.remove();
        }
    });


    // ✅ 초기 게시판 설명 표시
    const initialId = $select.val();
    if (boardMap[initialId]) {
        $desc.text(boardMap[initialId].description || "");
    }

    // ✅ 게시판 변경 시 설명 동기화
    $select.on("change", function () {
        const selectedId = $(this).val();
        const selectedBoard = boardMap[selectedId];
        if (selectedBoard) {
            $desc.text(selectedBoard.description || "");
        }
    });

    // ✅ Toast UI Editor 초기화
    editor = new toastui.Editor({
        el: document.querySelector("#editor"),
        height: "400px",
        initialEditType: "wysiwyg",
        previewStyle: "vertical",
        hooks: {
            async addImageBlobHook(blob, callback) {
                const formData = new FormData();
                formData.append("image", blob);

                try {
                    const response = await fetch("/api/community/image-upload", {
                        method: "POST",
                        body: formData
                    });
                    const result = await response.json();

                    if (result.success) {
                        uploadedTempImages.push(result.url); // ✅ 이미지 URL 저장
                        callback(result.url, "업로드 이미지");
                    } else {
                        alert("이미지 업로드 실패: " + result.message);
                    }
                } catch (e) {
                    console.error("이미지 업로드 에러:", e);
                    alert("이미지 업로드 중 오류가 발생했습니다.");
                }
            }
        }
    });

    // ✅ 취소 버튼 클릭 시 뒤로가기 또는 목록 이동
    $("#cancelWrite").on("click", async function () {
        const hasInput =
            $("#title").val().trim() !== "" ||
            editor.getMarkdown().trim() !== "";

        if (hasInput && !confirm("작성 중인 내용이 삭제됩니다. 정말 취소하시겠습니까?")) return;

        if (uploadedTempImages.length > 0) {
            try {
                await fetch("/api/community/delete-temp-images", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({ images: uploadedTempImages })
                });
                console.log("🗑️ 임시 업로드 이미지 삭제 완료");
            } catch (e) {
                console.error("⚠️ 이미지 삭제 실패:", e);
            }
        }

        // 글쓰기 취소 후 이전 페이지로 이동
        history.back();
        // 또는 location.href = "/community/해당게시판코드"; 로 명시 이동
    });

    // ✅ 게시물 제출 처리
    $("form").on("submit", function (e) {
        e.preventDefault();

        $("#content").val(editor.getHTML());

        const boardId = $("#board").val();
        const boardCode = $("#board option:selected").data("code"); // <option data-code="free">

        const formData = new FormData(this);

        $.ajax({
            url: `/api/community/${boardCode}/write`,
            method: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function (postId) {
                uploadedTempImages = [];

                // ✅ 작성 성공 시 상세 페이지로 이동
                location.href = `/community/${boardCode}/view/${postId}`;
            },
            error: function () {
                alert("게시물 등록 중 오류가 발생했습니다.");
            }
        });
    });

    // ✅ 페이지 이탈 시 임시 이미지 삭제 (브라우저 종료, 새로고침 등)
    $(window).on("beforeunload", function () {
        if (uploadedTempImages.length > 0) {
            const payload = JSON.stringify({ images: uploadedTempImages });
            const blob = new Blob([payload], { type: "application/json" });
            navigator.sendBeacon("/api/community/delete-temp-images", blob);
        }
    });
});
