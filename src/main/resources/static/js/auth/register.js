// /js/auth/register.js

$(document).ready(function () {
    const token = $('meta[name="_csrf"]').attr('content');
    const header = $('meta[name="_csrf_header"]').attr('content');

    let emailChecked = false;
    let lastCheckedEmail = "";

    // 초기 상태
    $('#submitBtn').prop('disabled', true);

    // 이메일 입력 변경 시
    $('#email').on('input', function () {
        const currentEmail = $(this).val();
        if (currentEmail !== lastCheckedEmail) {
            emailChecked = false;
            $('#emailCheckResult').text('이메일 중복 확인이 필요합니다.').css('color', 'red');
            $('#submitBtn').prop('disabled', true);
        }
    });

    // 이메일 중복 확인
    $('#checkEmailBtn').click(function () {
        const email = $('#email').val();

        if (!email) {
            $('#emailCheckResult').text('이메일을 입력해주세요.').css('color', 'red');
            return;
        }

        $.ajax({
            url: '/api/users/check-email',
            method: 'GET',
            data: { email: email },
            success: function (response) {
                if (response.exists) {
                    $('#emailCheckResult').text('이미 사용 중인 이메일입니다.').css('color', 'red');
                    emailChecked = false;
                    $('#submitBtn').prop('disabled', true);
                } else {
                    $('#emailCheckResult').text('사용 가능한 이메일입니다.').css('color', 'green');
                    emailChecked = true;
                    lastCheckedEmail = email;
                    validateForm();
                }
            },
            error: function () {
                $('#emailCheckResult').text('이메일 확인 중 오류가 발생했습니다.').css('color', 'red');
                emailChecked = false;
                $('#submitBtn').prop('disabled', true);
            }
        });
    });

    // 비밀번호 일치 확인
    $('#password, #confirmPassword').on('input', function () {
        const pw = $('#password').val();
        const confirmPw = $('#confirmPassword').val();

        if (!confirmPw) {
            $('#pwMatchResult').text('');
            validateForm();
            return;
        }

        if (pw === confirmPw) {
            $('#pwMatchResult').text('비밀번호가 일치합니다.').css('color', 'green');
        } else {
            $('#pwMatchResult').text('비밀번호가 일치하지 않습니다.').css('color', 'red');
        }

        validateForm();
    });

    // 폼 유효성 검사 후 버튼 제어
    function validateForm() {
        const pw = $('#password').val();
        const confirmPw = $('#confirmPassword').val();
        const passwordsMatch = pw && confirmPw && pw === confirmPw;

        if (emailChecked && passwordsMatch) {
            $('#submitBtn').prop('disabled', false);
        } else {
            $('#submitBtn').prop('disabled', true);
        }
    }

    // 폼 제출 시 AJAX 처리
    $('#registerForm').submit(function (e) {
        e.preventDefault();

        const formData = {
            email: $('#email').val(),
            password: $('#password').val(),
            confirmPassword: $('#confirmPassword').val(),
            name: $('#name').val(),
            role: $('#role').val()
        };

        $.ajax({
            url: '/api/users/register',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (res) {
                window.location.href = '/users/register-success';
            },
            error: function (xhr) {
                const message = xhr.responseText || '회원가입 중 오류가 발생했습니다.';
            }
        });
    });
});
