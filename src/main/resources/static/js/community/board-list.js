// /js/community/board-list.js

// 게시판 카테고리 정의
const BOARD_CATEGORIES = {
    all: { name: '전체 게시판', icon: '📋' },
    free: { name: '자유게시판', icon: '💬' },
    qna: { name: 'Q&A 게시판', icon: '❓' },
    study: { name: '스터디 게시판', icon: '👨‍🎓' },
    feedback: { name: '피드백 게시판', icon: '📝' },
    job: { name: '채용정보 공유', icon: '💼' }
};

// 게시판 분류 코드 가져오기
function getBoardCode() {
    // URL에서 게시판 코드 추출 (/community/free, /community/qna 등)
    const pathParts = window.location.pathname.split('/');
    return pathParts.length >= 3 ? pathParts[2] : 'all';
}

// 조회수 증가 함수
function incrementViewCount(postId) {
    // 서버에 조회수 증가 요청
    $.ajax({
        url: `/community/posts/${postId}/view-count`,
        method: "POST",
        success: function(response) {
            console.log('조회수 증가 완료');
        },
        error: function(xhr) {
            console.error('조회수 증가 실패:', xhr);
        }
    });
}

// 게시물 필터링 함수
function filterPosts() {
    const boardCode = getBoardCode();
    
    // 현재 게시판 타이틀 업데이트
    const category = BOARD_CATEGORIES[boardCode];
    if (category) {
        $('.board-header h2').text(`${category.icon} ${category.name}`);
    }
    
    // 전체 게시판이면 모든 게시물 표시
    if (boardCode === 'all') {
        $('.board-table tbody tr').show();
        return;
    }
    
    // 특정 게시판인 경우 해당 카테고리만 표시
    $('.board-table tbody tr').each(function() {
        const postCategory = $(this).find('td:nth-child(2)').text().trim();
        
        // 카테고리 텍스트를 코드로 변환하여 비교
        let categoryCode = '';
        switch(postCategory) {
            case '자유': categoryCode = 'free'; break;
            case '질문': categoryCode = 'qna'; break;
            case '스터디': categoryCode = 'study'; break;
            case '피드백': categoryCode = 'feedback'; break;
            case '채용': categoryCode = 'job'; break;
            default: categoryCode = 'free';
        }
        
        if (categoryCode === boardCode) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });
}

$(document).ready(function () {
    // 페이지 로드 시 게시물 필터링 적용
    filterPosts();
    
    // 글쓰기 버튼 클릭 시 플로팅 바 열기 (헤더에 추가한 버튼)
    $("#write-post-btn").on("click", function () {
        const boardCode = getBoardCode();
        
        $.ajax({
            url: `/community/${boardCode === 'all' ? 'free' : boardCode}/write`,
            method: "GET",
            success: function (html) {
                $("#floatingSidebarContent").html(html);
                $("#floatingSidebar").addClass("show");
                $("#floatingOverlay").addClass("show"); // 오버레이 표시
            },
            error: function () {
                alert("글쓰기 화면을 불러오지 못했습니다.");
            }
        });
    });
    
    // 사이드바 메뉴 클릭 시 해당 게시판으로 이동 처리
    $(document).on('click', '.sidebar-menu .menu-item a', function() {
        // 기존 클릭 이벤트 처리는 그대로 유지 (링크 이동)
        // 페이지 이동 후 필터링은 페이지 로드 시 filterPosts()가 처리
    });
    
    // 기존 글쓰기 버튼 클릭 시 플로팅 바 열기
    $(".write-button").on("click", function () {
        const boardCode = $(this).data("board-code") || getBoardCode();

        $.ajax({
            url: `/community/${boardCode}/write`,
            method: "GET",
            success: function (html) {
                $("#floatingSidebarContent").html(html);
                $("#floatingSidebar").addClass("show");
                $("#floatingOverlay").addClass("show"); // 오버레이 표시
            },
            error: function () {
                alert("글쓰기 화면을 불러오지 못했습니다.");
            }
        });
    });

    // 제목 클릭 시 뷰 페이지 플로팅 바 열기
    $(document).on("click", ".post-link", function (e) {
        e.preventDefault();

        const postId = $(this).data("id");
        console.log('게시글 ID:', postId, '클릭됨');

        // 조회수 증가 처리
        incrementViewCount(postId);

        $.ajax({
            url: `/community/posts/view/${postId}`,
            method: "GET",
            success: function (html) {
                $("#floatingSidebarContent").html(html);
                $("#floatingSidebar").addClass("show");
                $("#floatingOverlay").addClass("show"); // 오버레이 표시
            },
            error: function (xhr) {
                console.error('게시글 조회 실패:', xhr);
                alert("게시글을 불러오지 못했습니다.");
            }
        });
    });
    
    // URL 변경 감지 (뒤로가기/앞으로가기 등)
    $(window).on('popstate', function() {
        filterPosts();
    });
});