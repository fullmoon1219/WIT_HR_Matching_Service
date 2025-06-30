// /js/employer/post-list.js

$(document).ready(function () {
    // 개별 체크박스: 셀 클릭 시 토글
    $('.checkbox-cell').on('click', function (e) {
        const checkbox = $(this).find('input[type="checkbox"]');

        // input 직접 클릭한 경우는 무시
        if (e.target.tagName.toLowerCase() !== 'input') {
            checkbox.prop('checked', !checkbox.prop('checked')).trigger('change');
        }
    });

    // 전체 선택 체크박스 → 나머지에 반영
    $('#selectAll').on('change', function () {
        $('.select-post').prop('checked', this.checked);
    });

    // 공고 등록 버튼 클릭시 공고 작성 페이지
    $('#openWriteFormBtn').on('click', function () {
        $.ajax({
            url: '/employer/test/write',
            type: 'GET',
            success: function (html) {
                $('#floatingSidebarContent').html(html);
                $('#floatingOverlay').addClass('show');
                $('#floatingSidebar').addClass('show');
            },
            error: function () {
                alert('공고 작성 페이지를 불러오는데 실패했습니다.');
            }
        });
    });
    // 제목 클릭 시 수정 폼 로드
    $(document).on('click', '.edit-post-link', function (e) {
        e.preventDefault(); // 기본 링크 동작 막기

        const jobPostId = $(this).data('id');

        $.ajax({
            url: `/employer/jobpost_edit?jobPostId=${jobPostId}`,
            type: 'GET',
            success: function (html) {
                $('#floatingSidebarContent').html(html);
                $('#floatingOverlay').addClass('show');
                $('#floatingSidebar').addClass('show');
            },
            error: function () {
                alert('공고 수정 페이지를 불러오는 데 실패했습니다.');
            }
        });
    });

    // 공고 제목 클릭 시 공고 보기 페이지
    $('.post-title-link').on('click', function (e) {
        e.preventDefault();
        const postId = $(this).data('id');

        $.ajax({
            url: `/employer/test/view`, // 공고 작성/수정 화면 URL
            type: 'GET',
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
});