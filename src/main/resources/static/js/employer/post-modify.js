// /js/employer/post-modify.js

let isConfirming = false;
var editor;
const uploadedTempImages = new Set();

function handleCancelClick(e) {
    e.preventDefault();

    if (isConfirming) return;
    isConfirming = true;

    const hasChanges = $('#floatingSidebarContent')
        .find('input, textarea, select')
        .toArray()
        .some(el => el.value !== el.defaultValue);

    if (hasChanges) {
        const confirmed = confirm('작성 중인 내용을 취소하시겠습니까?');
        if (!confirmed) {
            isConfirming = false;
            return;
        }
    }

    $.ajax({
        url: `/employer/jobpost_list`,
        method: 'GET',
        success: function (html) {
            $('#floatingSidebarContent').html(html);
            $('#floatingOverlay').removeClass('show');
            $('#floatingSidebar').removeClass('show');
        },
        error: function () {
            alert('리스트 페이지를 불러오는 데 실패했습니다.');
        },
        complete: function () {
            isConfirming = false;
        }
    });
}

$(document).ready(function () {
    // -------- 이벤트 중복 방지 --------
    $(document).off('click.cancel').on('click.cancel', '.cancel-button', handleCancelClick);

    // -------- 기술 스택 선택 기능 --------
    const selectedStacks = new Set(
        $('#techStacksInput').val().split(',').map(v => v.trim()).filter(v => v !== "")
    );

    // 초기 선택된 버튼에 selected 클래스 부여
    $('.stack-tag').each(function () {
        const value = $(this).data('value')?.toString();
        if (selectedStacks.has(value)) {
            $(this).addClass('selected');
        }
    });

    // 클릭 시 toggle
    $(document).off('click.stack').on('click.stack', '.stack-tag', function () {
        const value = $(this).data('value')?.toString();

        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            selectedStacks.delete(value);
        } else {
            $(this).addClass('selected');
            selectedStacks.add(value);
        }

        $('#techStacksInput').val(Array.from(selectedStacks).join(','));
    });

    // -------- Toast UI Editor 초기화 --------
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

    // -------- 기존 설명 바인딩 --------
    const existingHtml = $("#description").val();
    if (existingHtml && existingHtml.trim() !== "") {
        editor.setHTML(existingHtml);
    }

    // -------- 제출 시 에디터 내용 반영 --------
    $("form").on("submit", function () {
        $("#description").val(editor.getHTML());
        uploadedTempImages.clear(); // 전송 시에는 이미지 삭제 대상에서 제외
    });

    // -------- 페이지 이탈 시 임시 이미지 삭제 --------
    $(window).on("beforeunload", function () {
        if (uploadedTempImages.size > 0) {
            const payload = JSON.stringify({ images: Array.from(uploadedTempImages) });
            const blob = new Blob([payload], { type: "application/json" });
            navigator.sendBeacon("/api/community/delete-temp-images", blob);
        }
    });

    $(document).on("dragstart", ".toastui-editor-contents img", function (e) {
        e.preventDefault();
    });

    $(document).on("click", ".toastui-editor-contents img", function () {
        if (confirm("이미지를 삭제하시겠습니까?")) {
            this.remove();
        }
    });

});
