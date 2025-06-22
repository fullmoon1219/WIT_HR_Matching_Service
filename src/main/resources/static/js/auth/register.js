// /js/auth/register.js

$(document).ready(function () {
    let emailChecked = false;
    let lastCheckedEmail = "";

    // 이메일 입력 변경 시
    $('#email').on('input', function () {
        const currentEmail = $(this).val();
        if (currentEmail !== lastCheckedEmail) {
            emailChecked = false;
            $('#emailCheckResult').text('이메일 중복 확인이 필요합니다.').css('color', 'red');
            $('#submitBtn').prop('disabled', true);
        }
    });

    // 이메일 중복 확인 버튼
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

    // 비밀번호 일치 검사
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

    // 유효성 확인 후 버튼 상태 설정
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

    $('#submitBtn').prop('disabled', true); // 초기 상태
});
