// board-view.js

let currentPostId = null;
let currentPostLiked = false;
let currentUser = '사용자'; // 실제로는 로그인한 사용자 정보를 사용

// URL에서 게시글 ID 추출
function getPostIdFromUrl() {
    const pathArray = window.location.pathname.split('/');
    const id = pathArray[pathArray.length - 1];
    return isNaN(id) ? null : parseInt(id);
}

// 게시글 좋아요 상태 불러오기
function loadPostLikes() {
    if (!currentPostId) return;
    
    $.ajax({
        url: `/community/posts/${currentPostId}/likes`,
        method: "GET",
        success: function(response) {
            updateLikeButton(response.likeCount, response.userLiked);
            currentPostLiked = response.userLiked;
        },
        error: function(xhr) {
            console.error('좋아요 정보 불러오기 실패:', xhr);
        }
    });
}

// 좋아요 버튼 상태 업데이트
function updateLikeButton(likeCount, userLiked) {
    const likeButton = $('#like-button');
    likeButton.html(`${userLiked ? '❤️' : '🖤'} 좋아요 (${likeCount})`);
    
    if (userLiked) {
        likeButton.addClass('liked');
    } else {
        likeButton.removeClass('liked');
    }
}

$(document).ready(function () {
    // 초기화
    currentPostId = getPostIdFromUrl();
    loadPostLikes();
    
    // 목록 버튼 클릭 시 페이지 이동
    $(document).on("click", ".list-btn", function () {
        window.location.href = '/community';
    });

    $(document).on("click", ".edit-btn", function () {
        const postId = $(this).data("id");  // 현재는 1로 하드코딩됨
        const boardCode = $(this).data("board-code");

        if (!postId || !boardCode) {
            alert("필요한 정보가 부족합니다.");
            return;
        }

        $.ajax({
            url: `/community/${boardCode}/edit/${postId}`,
            method: "GET",
            success: function (html) {
                $("#floatingSidebarContent").html(html);
                $("#floatingSidebar").addClass("show");
                $("#floatingOverlay").addClass("show");
            },
            error: function () {
                alert("수정 페이지를 불러오지 못했습니다.");
            }
        });
    });

});