$(document).ready(function () {
    const $personalTab = $('#personal-tab');
    const $companyTab = $('#company-tab');
    const $personalContent = $('#personal-content');
    const $companyContent = $('#company-content');

    $personalTab.on('click', function () {
        $personalTab.addClass('active');
        $companyTab.removeClass('active');
        $personalContent.addClass('active');
        $companyContent.removeClass('active');
    });

    $companyTab.on('click', function () {
        $companyTab.addClass('active');
        $personalTab.removeClass('active');
        $companyContent.addClass('active');
        $personalContent.removeClass('active');
    });
});
