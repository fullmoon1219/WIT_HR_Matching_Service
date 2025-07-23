// /js/community/board-write.js

var editor;
let selectedFiles = [];

$(document).ready(function () {
    // ✅ Toast UI Editor 인스턴스 초기화
    editor = new toastui.Editor({
        el: document.querySelector('#editor'),
        height: '500px',
        initialEditType: 'wysiwyg',
        previewStyle: 'vertical'
    });


    $(document).on("dragstart", ".toastui-editor-contents img", function (e) {
        e.preventDefault();
    });

    $(document).on("click", ".toastui-editor-contents img", function () {
        if (confirm("이미지를 삭제하시겠습니까?")) {
            this.remove();
        }
    });


    const postId = $("#postId").val();  // 수정 시 사용
    const boardCode = $("#boardCode").val(); // 게시판 코드 (숨겨진 필드)
    const $select = $("#board");
    const $desc = $("#board-description");

    // ✅ 게시판 설명 초기 세팅
    const initialId = $select.val();
    if (boardMap[initialId]) {
        $desc.text(boardMap[initialId].description || "");
    }

    // ✅ 게시판 변경 시 설명 업데이트
    $select.on("change", function () {
        const selectedId = $(this).val();
        const selectedBoard = boardMap[selectedId];
        if (selectedBoard) {
            $desc.text(selectedBoard.description || "");
        }
    });

    // ✅ 파일 선택 시 목록 렌더링
    $("#file").on("change", function (e) {
        const files = Array.from(e.target.files);

        selectedFiles = [];
        $("#selectedFileList").empty();

        files.forEach((file, index) => {
            selectedFiles.push(file);
            const sizeInKB = (file.size / 1024).toFixed(1);
            const item = `
                <li data-index="${index}">
                    ${file.name} (${sizeInKB} KB)
                    <button type="button" class="remove-file-btn">X</button>
                </li>
            `;
            $("#selectedFileList").append(item);
        });
    });

    // ✅ 파일 제거 버튼
    $(document).on("click", ".remove-file-btn", function () {
        const index = $(this).parent().data("index");
        selectedFiles.splice(index, 1);
        $(this).parent().remove();

        const dataTransfer = new DataTransfer();
        selectedFiles.forEach(file => dataTransfer.items.add(file));
        $("#file")[0].files = dataTransfer.files;

        $("#selectedFileList li").each((i, el) => $(el).attr("data-index", i));
    });

    // ✅ 수정 화면일 경우 기존 데이터 로딩
    if (postId) {
        $.ajax({
            url: `/api/community/posts/${postId}`,
            method: "GET",
            success: function (post) {
                $("#title").val(post.title);
                editor.setMarkdown(post.content);

                if (post.attachments && post.attachments.length > 0) {
                    $("#existingFileList").empty();
                    post.attachments.forEach(file => {
                        $("#existingFileList").append(`
                            <li data-id="${file.id}">
                                ${file.originalName}
                                <button type="button" class="remove-existing-file-btn">X</button>
                            </li>
                        `);
                    });
                }
            },
            error: function () {
                alert("게시글 정보를 불러오지 못했습니다.");
            }
        });
    }

    // ✅ 기존 첨부파일 제거
    $(document).on("click", ".remove-existing-file-btn", function () {
        $(this).parent().remove();
    });

    // ✅ 폼 제출
    $("form").on("submit", function (e) {
        e.preventDefault();

        const title = $("#title").val().trim();
        const boardId = $("#board").val();
        const content = editor.getMarkdown();

        if (!title || !content) {
            alert("제목과 내용을 모두 입력해주세요.");
            return;
        }

        const formData = new FormData();
        formData.append("title", title);
        formData.append("boardId", boardId);
        formData.append("content", content);

        selectedFiles.forEach(file => {
            formData.append("files", file);
        });

        $("#existingFileList li").each(function () {
            const fileId = $(this).data("id");
            formData.append("existingFileIds", fileId);
        });

        const method = postId ? "PUT" : "POST";
        const url = postId ? `/api/community/posts/${postId}` : `/api/community/posts`;

        $.ajax({
            url: url,
            method: method,
            processData: false,
            contentType: false,
            data: formData,
            success: function (savedPostId) {
                alert(postId ? "수정이 완료되었습니다." : "등록이 완료되었습니다.");
                // 상세 페이지로 이동
                location.href = `/community/${boardCode}/view/${savedPostId || postId}`;
            },
            error: function () {
                alert("게시글 " + (postId ? "수정" : "등록") + "에 실패했습니다.");
            }
        });
    });

    // ✅ 취소 버튼: 게시판 목록으로 이동
    $("#cancelModify").on("click", function () {
        if (confirm("작성을 취소하고 목록으로 돌아가시겠습니까?")) {
            location.href = `/community/${boardCode}`;
        }
    });
});
