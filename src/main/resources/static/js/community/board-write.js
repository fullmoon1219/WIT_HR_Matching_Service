// /js/community/board-write.js

var editor;
var uploadedTempImages = [];

$(document).ready(function () {
    const $select = $("#board");
    const $desc = $("#board-description");

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

    // ✅ 취소 버튼 클릭 시 플로팅 바 닫기 및 이미지 삭제
    $("#cancelWrite").on("click", async function () {
        const hasInput =
            $("#title").val().trim() !== "" ||
            editor.getMarkdown().trim() !== "";

        if (hasInput && !confirm("작성 중인 내용이 삭제됩니다. 정말 닫으시겠습니까?")) return;

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

        $("#floatingSidebar").removeClass("show");
        $("#floatingOverlay").removeClass("show");
        $("#floatingSidebarContent").empty();
    });


    $(document).on('keydown', function (e) {
        if (e.key === "Escape") {
            $("#floatingSidebar").removeClass("show");
            $("#floatingOverlay").removeClass("show");
        }
    });

    $(document).on('dblclick', '#floatingOverlay', function () {
        $("#floatingSidebar").removeClass("show");
        $("#floatingOverlay").removeClass("show");
    });
});

// ✅ 게시물 제출 처리
$("form").on("submit", function (e) {
    e.preventDefault();

    $("#content").val(editor.getHTML());

    const boardId = $("#board").val();
    const boardCode = $("#board option:selected").text().trim();
    const formData = new FormData(this);

    $.ajax({
        url: `/api/community/${boardCode}/write`,
        method: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function (postId) {
            uploadedTempImages = [];

            $.ajax({
                url: `/community/posts/view/${postId}`,
                method: "GET",
                success: function (html) {
                    $("#floatingSidebarContent").html(html);
                    $("#floatingSidebar").addClass("show");
                    $("#floatingOverlay").addClass("show");
                },
                error: function () {
                    alert("작성된 게시글을 불러오지 못했습니다.");
                }
            });
        },
        error: function () {
            alert("게시물 등록 중 오류가 발생했습니다.");
        }
    });
});

$(window).on("beforeunload", function () {
    if (uploadedTempImages.length > 0) {
        const payload = JSON.stringify({ images: uploadedTempImages });

        const blob = new Blob([payload], { type: "application/json" });

        navigator.sendBeacon("/api/community/delete-temp-images", blob);
    }
});
