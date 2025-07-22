// /js/employer/post-write.js

var editor;
const uploadedTempImages = new Set();
let isClosing = false;

$(document).ready(function () {
    // ✅ 기술 스택 선택
    const selectedStacks = new Set();

    // 에디터 내부 이미지 복사 방지 (dragstart 이벤트 차단)
    $(document).on("dragstart", ".toastui-editor-contents img", function (e) {
        e.preventDefault();
    });

    $(document).on("click", ".toastui-editor-contents img", function () {
        if (confirm("이미지를 삭제하시겠습니까?")) {
            this.remove();
        }
    });


    $(document).on('click', '.stack-tag', function () {
        const value = $(this).data('id');

        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            selectedStacks.delete(value);
        } else {
            $(this).addClass('selected');
            selectedStacks.add(value);
        }

        $('#techStacksInput').val(Array.from(selectedStacks).join(','));
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
                        uploadedTempImages.add(result.url);
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

    // ✅ 등록 버튼 클릭 시 에디터 내용 반영
    $("form").on("submit", function () {
        $("#description").val(editor.getHTML());
        uploadedTempImages.clear(); // 제출 성공 시 이미지 삭제 대상에서 제거
    });

    // ✅ 페이지 이탈 시 임시 이미지 삭제
    $(window).on("beforeunload", function () {
        if (uploadedTempImages.size > 0) {
            const payload = JSON.stringify({ images: Array.from(uploadedTempImages) });
            const blob = new Blob([payload], { type: "application/json" });
            navigator.sendBeacon("/api/community/delete-temp-images", blob);
        }
    });

    // ✅ 취소 버튼 클릭 또는 ESC, 오버레이 더블클릭 시 플로팅바 닫기
    function isFormModified() {
        const inputChanged = $('#floatingSidebarContent')
            .find('input[type="text"], input[type="date"]')
            .toArray()
            .some(el => el.value.trim() !== (el.defaultValue?.trim?.() || ''));

        const textareaChanged = editor.getMarkdown().trim() !== "";

        const selectChanged = $('#floatingSidebarContent')
            .find('select')
            .toArray()
            .some(select => {
                let initialValue = '';
                $(select).find('option').each(function () {
                    if (this.defaultSelected) {
                        initialValue = this.value;
                        return false;
                    }
                });
                return $(select).val() !== initialValue;
            });

        return inputChanged || textareaChanged || selectChanged;
    }

    function attemptCloseFloatingSidebar(e) {
        if (isClosing) return;
        isClosing = true;

        if (e) {
            e.preventDefault();
            e.stopPropagation();
        }

        if (isFormModified()) {
            if (!confirm('작성 중인 내용을 취소하시겠습니까?')) {
                isClosing = false;
                return;
            }
        }

        if (uploadedTempImages.size > 0) {
            const payload = JSON.stringify({ images: Array.from(uploadedTempImages) });
            const blob = new Blob([payload], { type: "application/json" });
            navigator.sendBeacon("/api/community/delete-temp-images", blob);
        }

        $('#floatingOverlay').removeClass('show');
        $('#floatingSidebar').removeClass('show');
        $('#floatingSidebarContent').empty();

        setTimeout(() => {
            isClosing = false;
        }, 200);
    }

    $(document).on('click', '.cancel-button', attemptCloseFloatingSidebar);
    $(document).on('keyup', function (e) {
        if (e.key === "Escape") attemptCloseFloatingSidebar(e);
    });
    $(document).on('dblclick', '#floatingOverlay', attemptCloseFloatingSidebar);
});
