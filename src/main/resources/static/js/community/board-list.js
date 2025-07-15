// /js/community/board-list.js

const boardCode = $(".write-button").data("board-code") || "";
var boardMap = {};

$(document).ready(function () {
    // 1. 전체 게시판 목록 불러와 boardMap 초기화
    $.ajax({
        url: "/api/community",
        method: "GET",
        success: function (boards) {
            boards.forEach(board => {
                boardMap[board.code] = board.name;
            });

            // 2. 게시글 목록 불러오기
            loadPostList(1, boardCode);
        },
        error: function () {
            alert("게시판 목록을 불러오지 못했습니다.");
        }
    });

    // 글쓰기 버튼 클릭 시 플로팅바 열기
    $(".write-button").on("click", function () {
        const boardCode = $(this).data("board-code");

        $.ajax({
            url: `/community/${boardCode}/write`,
            method: "GET",
            success: function (html) {
                $("#floatingSidebarContent").html(html);
                $("#floatingSidebar").addClass("show");
                $("#floatingOverlay").addClass("show");
            },
            error: function () {
                alert("글쓰기 화면을 불러오지 못했습니다.");
            }
        });
    });

    // 게시글 제목 클릭 시 플로팅바 열기
    $(document).on("click", ".post-link", function (e) {
        e.preventDefault();

        const postId = $(this).data("id");

        $.ajax({
            url: `/community/posts/view/${postId}`,
            method: "GET",
            success: function (html) {
                $("#floatingSidebarContent").html(html);
                $("#floatingSidebar").addClass("show");
                $("#floatingOverlay").addClass("show");

                // ✅ 여기서 호출!
                loadPostDetail(postId);
            },
            error: function () {
                alert("게시글을 불러오지 못했습니다.");
            }
        });
    });
});


function loadPostList(page = 1, boardCode = '') {
    const size = 10;
    const query = $.param({ page: page - 1, size: size, boardCode: boardCode });

    $.ajax({
        url: `/api/community/posts?${query}`,
        method: "GET",
        success: function (data) {
            renderPosts(data.content);
            renderPagination(data.totalPages, page, (p) => loadPostList(p, boardCode));

            const boardName = getBoardName(boardCode);
            $("#boardTitle").text(`📋 ${boardName} (${data.totalElements})`);
        },
        error: function () {
            alert("게시물 목록을 불러오지 못했습니다.");
        }
    });
}

function renderPosts(posts) {
    const $body = $("#postListBody");
    $body.empty();

    posts.forEach(post => {
        const commentCountText = post.commentCount > 0 ? `[${post.commentCount}]` : "";
        const row = `
            <tr>
                <td>${post.id}</td>
                <td>${post.boardName}</td>
                <td>
                    <a href="#" class="post-link" data-id="${post.id}">
                        <span class="post-title">${post.title}</span>
                        <span class="comment-count">${commentCountText}</span>
                    </a>
                </td>
                <td>${post.writerName}</td>
                <td>${post.updatedAt}</td>
                <td>${post.viewCount}</td>
                <td>
                    <i class="fa fa-heart ${post.liked ? 'liked' : ''}"></i> ${post.likeCount}
                </td>

            </tr>
        `;
        $body.append(row);
    });
}

function getBoardName(code) {
    if (!code || code === 'all') return '전체 게시판';
    return boardMap[code] || '게시판';
}