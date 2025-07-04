// board-view.js

let currentPostId = null;
let currentPostLiked = false;
let currentUser = '사용자'; // 실제로는 로그인한 사용자 정보를 사용

// URL에서 게시글 ID와 게시판 코드 추출
function getPostIdFromUrl() {
    const pathArray = window.location.pathname.split('/');
    const id = pathArray[pathArray.length - 1];
    return isNaN(id) ? null : parseInt(id);
}

function getBoardCodeFromUrl() {
    const pathArray = window.location.pathname.split('/');
    // /community/{boardCode}/view/{id} 형태 가정
    return pathArray.length >= 3 ? pathArray[2] : 'all';
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
    const currentBoardCode = getBoardCodeFromUrl();
    loadPostLikes();
    
    // 목록 버튼 클릭 시 페이지 이동
    $(document).on("click", ".list-btn", function () {
        // 현재 게시판 코드로 이동
        window.location.href = `/community/${currentBoardCode}`;
    });

    // 수정 버튼 클릭 시 수정 페이지 불러오기
    $(document).on("click", ".edit-btn", function () {
        const postId = $(this).data("id") || currentPostId;
        const boardCode = $(this).data("board-code") || currentBoardCode;

        if (!postId) {
            alert("게시글 정보를 찾을 수 없습니다.");
            return;
        }

        // 팝업형으로 수정 화면을 불러오는 경우
        if ($("#floatingSidebar").length) {
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
        } else {
            // 새 페이지로 수정 화면을 불러오는 경우
            window.location.href = `/community/${boardCode}/edit/${postId}`;
        }
    });
    
    // 삭제 버튼 클릭 시 확인 후 삭제 요청
    $(document).on("click", ".delete-btn", function () {
        const postId = $(this).data("id") || currentPostId;
        const boardCode = $(this).data("board-code") || currentBoardCode;
        
        if (!postId) {
            alert("게시글 정보를 찾을 수 없습니다.");
            return;
        }
        
        if (confirm("게시물을 삭제하시겠습니까?")) {
            // 삭제 요청 보내기
            $.ajax({
                url: `/community/posts/${postId}`,
                method: "DELETE",
                success: function(response) {
                    alert("게시물이 삭제되었습니다.");
                    // 삭제 성공 시 목록으로 이동
                    window.location.href = `/community/${boardCode}`;
                },
                error: function() {
                    alert("게시물 삭제에 실패했습니다.");
                }
            });
        }
    });
    
    // 좋아요 버튼 클릭 시 좋아요 토글
    $(document).on("click", "#like-button", function() {
        if (!currentPostId) return;
        
        $.ajax({
            url: `/community/posts/${currentPostId}/likes`,
            method: "POST",
            success: function(response) {
                updateLikeButton(response.likeCount, response.userLiked);
                currentPostLiked = response.userLiked;
            },
            error: function() {
                alert("좋아요 처리 중 오류가 발생했습니다.");
            }
        });
    });
});