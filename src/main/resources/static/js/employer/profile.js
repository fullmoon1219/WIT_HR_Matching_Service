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

            $fields.each(function () {
                const $td = $(this);
                const field = $td.data('field');
                const value = $td.find('input').val().trim();

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
});
