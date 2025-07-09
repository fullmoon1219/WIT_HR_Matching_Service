// /js/admin/inquiryManagement/inquiry-management.js

$(document).on("click", ".toggle-details-btn", function () {
    const inquiryId = $(this).data("id");
    const $wrapper = $(".row-" + inquiryId).find(".detail-table-wrapper");

    $(".detail-table-wrapper").not($wrapper).slideUp();
    $(".toggle-details-btn").not(this).text("▼");

    if ($wrapper.is(":visible")) {
        $wrapper.slideUp();
        $(this).text("▼");
    } else {
        $wrapper.slideDown();
        $(this).text("▲");
    }
});

$(document).ready(function () {
    // 페이지네이션 클릭
    $(document).on("click", ".pagination-btn", function () {
        const selectedPage = $(this).data("page");
        loadInquiryList(selectedPage);
    });
});

// 답변 등록 버튼 클릭 이벤트
$(document).on("click", ".reply-submit-btn", function () {
    const inquiryId = $(this).data("id");
    const replyContent = $(`#replyInput-${inquiryId}`).val().trim();

    if (!replyContent) {
        alert("답변 내용을 입력해주세요.");
        return;
    }

    $.ajax({
        url: `/api/admin/inquiries/${inquiryId}/reply`,
        method: "PATCH",
        contentType: "application/json",
        data: JSON.stringify({ reply: replyContent }),
        success: function () {
            alert("답변이 등록되었습니다.");
            loadInquiryList(); // 리스트 갱신
        },
        error: function () {
            alert("답변 등록에 실패했습니다.");
        }
    });
});
// 수정 버튼 클릭 → textarea 보여줌
$(document).on("click", ".reply-edit-btn", function () {
    const inquiryId = $(this).data("id");
    const $replyViewBox = $(`.reply-view-box[data-id="${inquiryId}"]`);
    const $editArea = $(`#edit-form-${inquiryId}`);
    const replyContent = $replyViewBox.find(".reply-text").text().trim();

    // 기존 답변 내용을 textarea에 채우기
    $editArea.find("textarea").val(replyContent);

    // 보기 숨기고 수정폼 보이기
    $replyViewBox.hide();
    $editArea.show();

    // 버튼 교체: 수정/삭제 → 저장/취소
    const $actionBtns = $(`.reply-action-btns:has(.reply-edit-btn[data-id="${inquiryId}"])`);
    $actionBtns.html(`
        <button class="reply-save-btn" data-id="${inquiryId}">저장</button>
        <button class="reply-cancel-btn" data-id="${inquiryId}">취소</button>
    `);
});


$(document).on("click", ".reply-cancel-btn", function () {
    const id = $(this).data("id");

    // 수정폼 숨기고 답변 보기 영역 보이기
    $(`#edit-form-${id}`).hide();
    $(`.reply-view-box[data-id="${id}"]`).show();

    // 버튼 복원: 저장/취소 → 수정/삭제
    const $actionBtns = $(`.reply-action-btns:has(.reply-cancel-btn[data-id="${id}"])`);
    $actionBtns.html(`
        <button class="reply-edit-btn" data-id="${id}">수정</button>
        <button class="reply-delete-btn" data-id="${id}">삭제</button>
    `);
});



// 취소 버튼 클릭
$(document).on("click", ".reply-cancel-btn", function () {
    const id = $(this).data("id");
    $(`#edit-form-${id}`).hide();
    $(`.reply-view[data-id="${id}"]`).show();
});

// 저장 버튼 클릭 (수정)
$(document).on("click", ".reply-save-btn", function () {
    const id = $(this).data("id");
    const updatedReply = $(`#edit-reply-input-${id}`).val().trim();

    if (!updatedReply) {
        alert("답변 내용을 입력해주세요.");
        return;
    }

    $.ajax({
        url: `/api/admin/inquiries/${id}/reply`,
        method: "PATCH",
        contentType: "application/json",
        data: JSON.stringify({ reply: updatedReply }),
        success: function () {
            alert("답변이 수정되었습니다.");
            loadInquiryList();
        },
        error: function () {
            alert("수정에 실패했습니다.");
        }
    });
});

// 삭제 버튼 클릭
$(document).on("click", ".reply-delete-btn", function () {
    const id = $(this).data("id");

    if (!confirm("정말로 답변을 삭제하시겠습니까?")) return;

    $.ajax({
        url: `/api/admin/inquiries/${id}/reply`,
        method: "DELETE",
        success: function () {
            alert("답변이 삭제되었습니다.");
            loadInquiryList();
        },
        error: function () {
            alert("삭제에 실패했습니다.");
        }
    });
});
