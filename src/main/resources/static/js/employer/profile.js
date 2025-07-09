// /js/employer/profile.js

$(document).ready(function () {
    let isEditing = false;
    const $fields = $('td[data-field]');
    const originalValues = {};

    $('#editButton').on('click', function () {
        //편집 모드
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
            // 비밀번호 입력하지 않았을때 return
            const password = $('#confirmPassword').val().trim();
            if (!password) {
                alert('비밀번호를 입력해주세요.');
                return;
            }

            //비밀번호 입력했을 때
            const updatedData = {};
            $fields.each(function () {
                const $td = $(this);
                const field = $td.data('field');
                const value = $td.find('input').val().trim();
                updatedData[field] = value;
            });
            updatedData.password = password;


            // 서버로 수정 요청
            $.ajax({
                type: 'POST',
                url: '/employer/profile/edit',
                contentType: 'application/json',
                data: JSON.stringify(updatedData),
                success: function () {
                    alert('기업 정보가 성공적으로 수정되었습니다.');

                    // 화면에 새 데이터 반영
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
        console.log(newPassword +"//"+ confirmPassword)
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

});