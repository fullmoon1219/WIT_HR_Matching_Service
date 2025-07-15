// board-view.js

$(document).ready(function () {

    // 목록 버튼 클릭 시 플로팅바 닫기
    $(document).on("click", ".back-btn", function () {
        $("#floatingSidebar").removeClass("show");
        $("#floatingOverlay").removeClass("show");
    });

    $(document).on('keydown', function (e) {
        if (e.key === "Escape") {
            $("#floatingSidebar").removeClass("show");
            $("#floatingOverlay").removeClass("show");
        }
    });

    $(document).on('dblclick', '#floatingOverlay', function () {
        $("#floatingSidebar").removeClass("show");
        $("#floatingOverlay").removeClass("show");
    });

    $(document).on("click", ".edit-btn", function () {
        const postId = $(this).data("id");
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

function loadPostDetail(postId) {
    $.ajax({
        url: `/api/community/posts/${postId}`,
        method: "GET",
        success: function (post) {
            increaseViewCountIfNotViewed(post.id);

            $("#viewBoardTitle").text(post.boardName);
            $("#viewBoardDescription").text(post.boardDescription);
            $("#viewPostTitle").text(post.title);
            $("#viewWriterName").text(post.writerName);
            $("#viewCreatedAt").text(post.createdAt);
            $("#viewViewCount").text("조회수 " + post.viewCount);

            const $likeEl = $("#viewLikeCount");
            $likeEl.text(`♥ ${post.likeCount}`);

            if (post.liked) {
                $likeEl.attr("class", "heart-icon liked");
            } else {
                $likeEl.attr("class", "heart-icon");
            }

            $("#viewPostContent").html(post.content);

            // 첨부파일
            if (post.attachments && post.attachments.length > 0) {
                $("#viewAttachments").show();
                $("#attachmentList").empty();
                post.attachments.forEach(file => {
                    $("#attachmentList").append(
                        `<li><a href="/files/download/${file.id}">${file.originalName}</a></li>`
                    );
                });
            } else {
                $("#viewAttachments").hide(); // 첨부파일이 없을 경우 숨기기 (옵션)
            }

            const isWriter = (currentUserId === post.writerId);
            if (isWriter) {
                $("#editPostBtn").show();
                $("#deletePostBtn").show();
            } else {
                $("#editPostBtn").hide();
                $("#deletePostBtn").hide();
            }

            // 댓글
            renderComments(post.comments);

            // 수정 버튼 정보 저장
            $("#editBtn").data("id", post.id);
            $("#editBtn").data("board-code", post.boardCode);
        },
        error: function () {
            alert("게시글을 불러오지 못했습니다.");
        }
    });
}

function renderComments(comments) {
    const $list = $("#commentList");
    $list.empty();

    comments.forEach(comment => {
        const isCommentWriter = currentUserId === comment.userId;

        const children = (comment.children || []).map(child => {
            const isChildWriter = currentUserId === child.userId;

            return `
                <div class="child-comments">
                    <div class="comment-with-profile">
                        <img class="comment-profile" src="/images/users/user_small_profile.png">
                        <div class="comment-body">
                            <div class="comment-header">
                                <strong>${child.writerName}</strong> <span>(${child.email})</span>
                                ${child.writer ? `<span class="comment-writer-badge">작성자</span>` : ""}
                            </div>
                            <div class="comment-content">${child.content}</div>
                            <div class="comment-actions">
                                <span>${child.createdAt}</span> | ♥ ${child.likeCount}
                                <a href="#" class="reply-link">답글</a>
                                ${isChildWriter ? `<a href="#" class="reply-edit">수정</a><a href="#" class="reply-delete">삭제</a>` : ""}
                            </div>
                        </div>
                    </div>
                </div>
            `;
        }).join("");

        const html = `
            <div class="comment">
                <div class="comment-with-profile">
                    <img class="comment-profile" src="/images/users/user_small_profile.png">
                    <div class="comment-body">
                        <div class="comment-header">
                            <strong>${comment.writerName}</strong> <span>(${comment.email})</span>
                            ${comment.writer ? `<span class="comment-writer-badge">작성자</span>` : ""}
                        </div>
                        <div class="comment-content">${comment.content}</div>
                        <div class="comment-actions">
                            <span>${comment.createdAt}</span> | ♥ ${comment.likeCount}
                            <a href="#" class="reply-link">답글</a>
                            ${isCommentWriter ? `<a href="#" class="reply-edit">수정</a><a href="#" class="reply-delete">삭제</a>` : ""}
                        </div>
                    </div>
                </div>
                ${children}
            </div>
        `;
        $list.append(html);
    });
}

function increaseViewCountIfNotViewed(postId) {
    const cookieName = "postView";
    const cookie = document.cookie
        .split("; ")
        .find(row => row.startsWith(cookieName + "="));

    let viewedPostIds = [];

    if (cookie) {
        viewedPostIds = cookie.split("=")[1].split(",");
        if (viewedPostIds.includes(postId.toString())) {
            // 이미 본 게시글이므로 조회수 증가 X
            return;
        }
    }

    // 조회수 증가 요청
    $.post(`/api/community/posts/${postId}/view`);

    // 쿠키에 postId 추가
    viewedPostIds.push(postId);
    const expires = new Date();
    expires.setHours(expires.getHours() + 24); // 24시간 유효

    document.cookie = `${cookieName}=${viewedPostIds.join(",")}; path=/; expires=${expires.toUTCString()}`;
}

$(document).on("click", "#viewLikeCount", function () {
    const postId = $("#postId").val();

    $.ajax({
        url: `/api/community/posts/${postId}/like`,
        method: "POST",
        success: function (res) {
            const $likeEl = $("#viewLikeCount");

            $likeEl.text(`♥ ${res.likeCount}`);

            if (res.liked) {
                $likeEl.addClass("liked");
            } else {
                $likeEl.removeClass("liked");
            }
        },
        error: function () {
            alert("좋아요 처리에 실패했습니다.");
        }
    });
});



