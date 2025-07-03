// /js/employer/applicant-list.js




$('.post-title-link').on('click', function (e) {
    e.preventDefault();
    const postId = $(this).data('id');

    $.ajax({
        url: `/employer/application/jobpost_detail`,
        type: 'GET',
        data: { postId: postId },  // ✅ GET 파라미터로 전달
        success: function (html) {
            $('#floatingSidebarContent').html(html);
            $('#floatingOverlay').addClass('show');
            $('#floatingSidebar').addClass('show');
        },
        error: function () {
            alert('공고 정보를 불러오는 데 실패했습니다.');
        }
    });
});

$('.post-resume-link').on('click', function () {
    const userId = $(this).closest('tr').find('td:eq(2)').text().trim();

    $.ajax({
        url: `/employer/application/resume_detail`,
        type: 'GET',
        success: function (html) {
            $('#floatingSidebarContent').html(html);
            $('#floatingOverlay').addClass('show');
            $('#floatingSidebar').addClass('show');
        },
        error: function () {
            alert('지원자 상세 정보를 불러오는 데 실패했습니다.');
        }
    });
});
