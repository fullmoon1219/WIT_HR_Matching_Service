// /js/support/report.js

$(document).ready(function () {
    const csrfToken = $('meta[name="_csrf"]').attr('content');
    const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    $.ajaxSetup({
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        }
    });


    $('#reportForm').on('submit', function (e) {
        e.preventDefault();

        const data = {
            reportType: $('input[name="reportType"]').val(),
            targetId: $('input[name="targetId"]').val(),
            reportedUserId: $('input[name="reportedUserId"]').val(),
            reason: $('#title').val(),
            description: $('#content').val()
        };

        $.ajax({
            url: '/api/users/reports',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (res) {
                alert(res);  // "신고가 등록되었습니다."
                const targetId = data.targetId;
                location.href = '/recruit/view/' + targetId;
            },
            error: function (xhr) {
                const message = xhr.responseText || '신고 등록에 실패했습니다.';
                alert(message);
            }
        });
    });
});
