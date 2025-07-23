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
                    showFlashMessage('success', '프로필이 성공적으로 업데이트되었습니다.');

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
                    showFlashMessage('error', message);
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

    $('#uploadImageButton').on('click', function () {
        $('#profileImageInput').click();
    });

    $('#profileImageInput').on('change', function () {
        const formData = new FormData();
        formData.append("profileImage", this.files[0]);

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
                    const timestamp = new Date().getTime();
                    const newImage = $('<img>', {
                        id: 'profileImagePreview',
                        src: data.imageUrl + '?t=' + timestamp,
                        alt: '기업 프로필 이미지',
                        class: 'profile-image-preview'
                    });
                    $('#profileImagePreview').replaceWith(newImage);
                    alert("이미지 업로드가 완료되었습니다!");
                    location.reload(true); // ✅ 새로고침
                } else {
                    alert("업로드 실패: " + data.message);
                }
            })

            .catch(err => {
                console.error(err);
                alert("이미지 업로드 중 오류가 발생했습니다.");
            });
    });

    // ✅ 메시지 출력 함수 추가
    function showFlashMessage(type, message) {
        const $container = $('.flash-message-container');
        const $success = $container.find('.alert-success');
        const $error = $container.find('.alert-danger');

        $success.hide();
        $error.hide();

        if (type === 'success') {
            $success.find('p').text(message);
            $success.show();
        } else if (type === 'error') {
            $error.find('p').text(message);
            $error.show();
        }

        $container.show();

        setTimeout(() => {
            $container.fadeOut();
        }, 3000);
    }
});
