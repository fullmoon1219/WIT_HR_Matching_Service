// /js/employer/profile.js

$(document).ready(function () {
    let isEditing = false;
    const $fields = $('td[data-field]');
    const originalValues = {};

    $('#editButton').on('click', function () {
        const $button = $(this);

        if (!isEditing) {
            $fields.each(function () {
                const $td = $(this);
                const field = $td.data('field');
                const value = $td.find('a').length ? $td.find('a').text().trim() : $td.text().trim();
                originalValues[field] = value;

                $td
                    .addClass('editing')
                    .html(`<input type="text" value="${value}" class="inline-input">`);
            });

            $('#confirmPassword').show();
            $('#cancelButton').show();
            $button.text('저장');
            isEditing = true;

        } else {
            const password = $('#confirmPassword').val().trim();
            if (!password) {
                alert('비밀번호를 입력해주세요.');
                return;
            }

            const updatedData = {};
            $fields.each(function () {
                const $td = $(this);
                const field = $td.data('field');
                const value = $td.find('input').val().trim();
                updatedData[field] = value;
            });
            updatedData.password = password;

            $.ajax({
                type: 'POST',
                url: '/employer/profile/edit',
                contentType: 'application/json',
                data: JSON.stringify(updatedData),
                success: function () {
                    alert('기업 정보가 성공적으로 수정되었습니다.');

                    $fields.each(function () {
                        const $td = $(this);
                        const field = $td.data('field');
                        const value = updatedData[field];

                        if (field === 'homepage') {
                            $td.html(`<a href="https://${value}" target="_blank">${value}</a>`);
                        } else {
                            $td.text(value);
                        }
                        $td.removeClass('editing');
                    });

                    $('#confirmPassword').val('').hide();
                    $('#cancelButton').hide();
                    $button.text('정보 수정');
                    isEditing = false;
                },
                error: function (xhr) {
                    const message = xhr.responseText || '수정 중 오류가 발생했습니다.';
                    alert(message);
                }
            });
        }
    });

    $('#cancelButton').on('click', function () {
        $fields.each(function () {
            const $td = $(this);
            const field = $td.data('field');
            const original = originalValues[field];

            if (field === 'homepage') {
                $td.html(`<a href="https://${original}" target="_blank">${original}</a>`);
            } else {
                $td.text(original);
            }
            $td.removeClass('editing');
        });

        $('#confirmPassword').val('').hide();
        $('#editButton').text('정보 수정');
        $(this).hide();
        isEditing = false;
    });

    // ✅ 비밀번호 변경 모달 처리
    $('#password-edit-button').on('click', function () {
        $('#passwordModal').fadeIn();
    });

    $('.close-button').on('click', function () {
        $('#passwordModal').fadeOut();
        resetModal();
    });

    $('#verifyPasswordBtn').on('click', function () {
        const currentPassword = $('#currentPassword').val().trim();
        if (!currentPassword) {
            alert('현재 비밀번호를 입력해주세요.');
            return;
        }

        $.ajax({
            url: '/employer/profile/verify_password',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ password: currentPassword }),
            success: function () {
                $('#step1').hide();
                $('#step2').show();
            },
            error: function () {
                alert('비밀번호가 일치하지 않습니다.');
            }
        });
    });

    $('#changePasswordBtn').on('click', function () {
        const newPassword = $('#newPassword').val().trim();
        const confirmPassword = $('#confirmedPassword').val().trim();

        if (newPassword.length < 6) {
            alert('새 비밀번호는 6자 이상이어야 합니다.');
            return;
        }

        if (newPassword !== confirmPassword) {
            alert('새 비밀번호가 일치하지 않습니다.');
            return;
        }

        $.ajax({
            url: '/employer/profile/update_password',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ newPassword }),
            success: function () {
                alert('비밀번호가 성공적으로 변경되었습니다.');
                $('#passwordModal').fadeOut();
                resetModal();
            },
            error: function () {
                alert('비밀번호 변경에 실패했습니다.');
            }
        });
    });

    function resetModal() {
        $('#currentPassword, #newPassword, #confirmPassword').val('');
        $('#step1').show();
        $('#step2').hide();
    }

    // ✅ 프로필 이미지 업로드 기능
    $('#uploadImageButton').on('click', function () {
        $('#profileImageInput').click();
    });

    $('#profileImageInput').on('change', function () {
        const formData = new FormData();
        formData.append("profileImage", this.files[0]);

        // CSRF 토큰 가져오기
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        fetch("/employer/profile/image-upload", {
            method: "POST",
            headers: {
                [csrfHeader]: csrfToken
            },
            body: formData
        })
            .then(response => {
                if (!response.ok) throw new Error("이미지 업로드 실패");
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    $('#profileImagePreview').attr('src', data.imageUrl);
                    alert("이미지 업로드가 완료되었습니다!");
                } else {
                    alert("업로드 실패: " + data.message);
                    console.log(data.message);
                }
            })
            .catch(err => {
                console.error(err);
                alert("이미지 업로드 중 오류가 발생했습니다.");
            });
    });
});
