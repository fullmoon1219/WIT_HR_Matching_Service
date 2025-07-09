$(document).ready(function () {
    // 로그아웃 링크 클릭 시 이벤트 전파 막기
    $('#profileArea a').on('click', function (e) {
        e.stopPropagation();
    });

    // 프로필 영역 클릭 시 역할에 따라 이동
    $('#profileArea').on('click', function () {
        const role = $('#userRole').val();

        if (!role) return;

        if (role === 'ADMIN') {
            window.location.href = '/admin/dashboard';
        } else if (role === 'EMPLOYER') {
            window.location.href = '/employer/profile/view';
        } else if (role === 'APPLICANT') {
            window.location.href = '/applicant/main';
        }
    });
});
