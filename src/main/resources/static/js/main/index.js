

$(document).ready(function () {
    const csrfToken = $('meta[name="_csrf"]').attr('content');
    const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    // 전역 Ajax 요청에 CSRF 헤더 자동 추가
    $(document).ajaxSend(function (event, jqxhr, settings) {
        if (csrfToken && csrfHeader) {
            jqxhr.setRequestHeader(csrfHeader, csrfToken);
        }
    });

    $('#resendEmailBtn').on('click', function () {
        const email = $('#resendEmailBtn').data('email');

        if (!email) {
            alert('이메일 정보가 없습니다.');
            return;
        }

        $.ajax({
            url: '/api/users/resend-verification',
            method: 'POST',
            data: { email: email },
            success: function (message) {
                alert(message);
            },
            error: function (xhr) {
                const errorMessage = xhr.responseText || '메일 재전송에 실패했습니다.';
                alert(errorMessage);
            }
        });
    });
});
