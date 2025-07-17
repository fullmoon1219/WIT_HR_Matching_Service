// /js/support/inquiry.js

$(document).ready(function () {
    const csrfToken = $("meta[name='_csrf']").attr("content");
    const csrfHeader = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: "/api/users/inquiries/reasons",
        method: "GET",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success: function (reasons) {
            const $select = $("#reasonId");
            reasons.forEach(reason => {
                $select.append(`<option value="${reason.id}">${reason.name}</option>`);
            });
        },
        error: function () {
            alert("문의 사유 목록을 불러오지 못했습니다.");
        }
    });

    // 📤 문의 등록 처리
    $("#inquiryForm").on("submit", function (e) {
        e.preventDefault();

        const data = {
            title: $("#title").val(),
            reasonId: $("#reasonId").val(),
            content: $("#content").val()
        };

        $.ajax({
            url: "/api/users/inquiries",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function () {
                alert("문의가 성공적으로 등록되었습니다.");
                location.href = "/";
            },
            error: function () {
                alert("문의 등록에 실패했습니다.");
            }
        });
    });

});
