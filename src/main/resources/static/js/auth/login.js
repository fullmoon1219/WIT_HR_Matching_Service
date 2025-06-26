$(document).ready(function () {
    $('.tab').on('click', function () {
        $('.tab').removeClass('active');
        $(this).addClass('active');

        const tabId = $(this).attr('id');
        let selectedRole = '';

        if (tabId === 'personal-tab') {
            selectedRole = 'APPLICANT';
        } else if (tabId === 'company-tab') {
            selectedRole = 'EMPLOYER';
        } else if (tabId === 'developer-tab') {
            selectedRole = 'ADMIN';
        }

        $('#userType').val(selectedRole);
    });
});
