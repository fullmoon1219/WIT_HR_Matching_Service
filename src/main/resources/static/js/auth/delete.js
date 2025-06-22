// /js/auth/delete.js

$(document).ready(function () {
    const token = $('meta[name="_csrf"]').attr('content');
    const header = $('meta[name="_csrf_header"]').attr('content');

    $('#deleteForm').submit(function (e) {
        e.preventDefault();

        if (!confirm('정말 회원 탈퇴하시겠습니까?')) {
            return;
        }

        const isSocialUser = $('#confirmDelete').length > 0;
        const payload = {};

        if (isSocialUser) {
            if (!$('#confirmDelete').is(':checked')) {
                $('#deleteMessage').text("탈퇴에 동의해주세요.");
                return;
            }
            payload.confirmDelete = true;
        } else {
            const password = $('#password').val();
            if (!password) {
                $('#deleteMessage').text("비밀번호를 입력해주세요.");
                return;
            }
            payload.password = password;
        }

        $.ajax({
            url: '/api/users/delete',
            method: 'POST',
            data: $.param(payload),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function () {
                window.location.href = '/users/delete-success';
            },
            error: function (xhr) {
                $('#deleteMessage').text(xhr.responseText || "회원 탈퇴 실패");
            }
        });
    });
});
