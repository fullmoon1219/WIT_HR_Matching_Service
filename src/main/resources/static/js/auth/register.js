// /js/auth/register.js
$(document).ready(function () {
    const token = $('meta[name="_csrf"]').attr('content');
    const header = $('meta[name="_csrf_header"]').attr('content');

    let emailChecked = false;
    let lastCheckedEmail = "";

    // 탭 클릭 (회원 유형 선택)
    $('.tab').on('click', function () {
        $('.tab').removeClass('active');
        $(this).addClass('active');

        const selectedRole = $(this).data('role');
        $('#role').val(selectedRole);

        if (selectedRole === 'APPLICANT') {
            $('#personal-content').show();
            $('#company-content').hide();
        } else {
            $('#personal-content').hide();
            $('#company-content').show();
        }
    });

    // 초기 상태
    $('#submitBtn').prop('disabled', true);

    // 이메일 입력 시 중복 확인 초기화
    $('#email').on('input', function () {
        const currentEmail = $(this).val();
        if (currentEmail !== lastCheckedEmail) {
            emailChecked = false;
            $('#emailCheckResult')
                .text('')
                .removeClass('success error');
            $('#submitBtn').prop('disabled', true);
        }
    });

    // 이메일 중복 확인
    $('#checkEmailBtn').click(function () {
        const email = $('#email').val();

        if (!email) {
            $('#emailCheckResult')
                .text('이메일을 입력해주세요.')
                .removeClass('success')
                .addClass('error');
            return;
        }

        $.ajax({
            url: '/api/users/check-email',
            method: 'GET',
            data: { email: email },
            success: function (response) {
                if (response.exists) {
                    $('#emailCheckResult')
                        .text('이미 사용 중인 이메일입니다.')
                        .removeClass('success')
                        .addClass('error');
                    emailChecked = false;
                    $('#submitBtn').prop('disabled', true);
                } else {
                    $('#emailCheckResult')
                        .text('사용 가능한 이메일입니다.')
                        .removeClass('error')
                        .addClass('success');
                    emailChecked = true;
                    lastCheckedEmail = email;
                    validateForm();
                }
            },
            error: function () {
                $('#emailCheckResult')
                    .text('이메일 확인 중 오류가 발생했습니다.')
                    .removeClass('success')
                    .addClass('error');
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
            $('#pwMatchResult').text('').removeClass('success error');
            validateForm();
            return;
        }

        if (pw === confirmPw) {
            $('#pwMatchResult')
                .text('비밀번호가 일치합니다.')
                .removeClass('error')
                .addClass('success');
        } else {
            $('#pwMatchResult')
                .text('비밀번호가 일치하지 않습니다.')
                .removeClass('success')
                .addClass('error');
        }

        validateForm();
    });

    // 전체 약관 동의 체크
    $('#agree-all').on('change', function () {
        const checked = $(this).is(':checked');
        $('#agreeTerms, #agreePrivacy, #agreeMarketing').prop('checked', checked);
    });

    // 유효성 검사
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

    // 회원가입 폼 제출
    $('#registerForm').submit(function (e) {
        e.preventDefault();

        if (!emailChecked) {
            $('#emailCheckResult')
                .text('이메일 중복 확인이 필요합니다.')
                .removeClass('success')
                .addClass('error');
            return;
        }

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
            success: function () {
                window.location.href = '/users/register-success';
            },
            error: function (xhr) {
                const message = xhr.responseText || '회원가입 중 오류가 발생했습니다.';
                alert(message);
            }
        });
    });
});
