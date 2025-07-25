// board-view.js

$(document).ready(function () {
    const postId = $("#postId").val();
    if (!postId) return;

    loadPostDetail(postId);

    // 목록 버튼 → 이전 페이지로 이동
    $(".back-btn").on("click", function () {
        const boardCode = $("#boardCode").val();
        if (boardCode) {
            location.href = `/community/${boardCode}`;
        } else {
            location.href = "/community/all";
        }
    });


    // 수정 버튼 클릭 → 수정 페이지로 이동
    $(document).on("click", ".edit-btn", function () {
        const boardCode = $(this).data("board-code");
        if (!postId || !boardCode) {
            alert("필요한 정보가 부족합니다.");
            return;
        }

        location.href = `/community/${boardCode}/edit/${postId}`;
    });
});

function loadPostDetail(postId) {
    $.ajax({
        url: `/api/community/posts/${postId}`,
        method: "GET",
        success: function (post) {
            increaseViewCountIfNotViewed(post.id);

            $("#editPostBtn").data("id", post.id);
            $("#editPostBtn").data("board-code", post.boardCode);
            $("#deletePostBtn").data("board-code", post.boardCode);

            $("#viewBoardTitle").text(post.boardName);
            $("#viewBoardDescription").text(post.boardDescription);
            $("#viewPostTitle").text(post.title);
            $("#viewWriterName").text(post.writerName);
            $("#viewCreatedAt").text(post.createdAt);
            $("#viewViewCount").text("조회수 " + post.viewCount);
            $("#viewLikeCount").text(`♥ ${post.likeCount}`)
                .toggleClass("liked", post.liked);
            $("#reportPostBtn").data("target-id", post.id);
            $("#reportPostBtn").data("user-id", post.writerId);
            $("#viewPostContent").html(post.content);

            if (post.attachments?.length > 0) {
                $("#viewAttachments").show();
                $("#attachmentList").empty();
                post.attachments.forEach(file => {
                    $("#attachmentList").append(
                        `<li><a href="/files/download/${file.id}">${file.originalName}</a></li>`
                    );
                });
            } else {
                $("#viewAttachments").hide();
            }

            if (currentUserId === post.writerId) {
                $("#editPostBtn, #deletePostBtn").show();
            } else {
                $("#editPostBtn, #deletePostBtn").hide();
            }

            loadComments(post.id);
        },
        error: function () {
            alert("게시글을 불러오지 못했습니다.");
        }
    });
}

function renderComments(comments) {
    const $list = $("#commentList");
    $list.empty();

    const MAX_VISIBLE = 5;

    comments.forEach((comment, i) => {
        const isCommentWriter = currentUserId === comment.userId;
        const childComments = comment.children || [];

        // 삭제된 댓글이면서 대댓글도 없으면 렌더링하지 않음
        if (comment.deleted && childComments.length === 0) {
            return;
        }

        const repliesHtml = childComments.map((child, j) => {
            const isChildWriter = currentUserId === child.userId;
            const likedClass = child.isLiked ? "liked" : "";

            return `
                <div class="reply" data-id="${child.id}" style="${j < MAX_VISIBLE ? '' : 'display:none'}" data-index="${j}">
                    <div class="comment-with-profile">
                        <img class="comment-profile" src="${child.profileImage ? '/uploads/users/profile/' + child.profileImage : '/images/users/user_small_profile.png'}">
                        <div class="comment-body">
                            <div class="comment-header">
                                <strong>${child.writerName}</strong> <span>(${child.email})</span>
                                ${child.isWriter ? `<span class="comment-writer-badge">작성자</span>` : ""}
                            </div>
                            <div class="comment-content">
                                ${child.deleted ? '<em class="deleted">삭제된 댓글입니다.</em>' : child.content}
                            </div>
                            <div class="comment-actions">
                                <span>${child.createdAt}</span>
                                ${!child.deleted ? `
                                    | <a href="#" class="like-button ${likedClass}">♥ ${child.likeCount}</a>
                                    <a href="#" class="report-button" data-type="USER" data-target-id="${child.id}" data-user-id="${child.userId}">신고</a>
                                    ${isChildWriter ? `<a href="#" class="reply-edit">수정</a><a href="#" class="reply-delete">삭제</a>` : ""}

                                ` : ""}
                            </div>
                        </div>
                    </div>
                </div>
            `;
        }).join("");

        const showMoreReplies = childComments.length > MAX_VISIBLE
            ? `<div class="replies-show-more" data-parent="${i}" data-count="${MAX_VISIBLE}" onclick="showMoreReplies(this)">답글 더보기 ▼</div>`
            : "";

        const toggleButton = childComments.length > 0
            ? `<div class="reply-toggle" onclick="toggleReplies(this, ${childComments.length})">답글 ${childComments.length}개 ▼</div>`
            : "";

        const likedClass = comment.isLiked ? "liked" : "";

        const commentHtml = `
            <div class="comment" data-id="${comment.id}" style="${i < MAX_VISIBLE ? '' : 'display:none'}" data-index="${i}">
                <div class="comment-with-profile">
                    <img class="comment-profile" src="${comment.profileImage ? '/uploads/users/profile/' + comment.profileImage : '/images/users/user_small_profile.png'}">
                    <div class="comment-body">
                        <div class="comment-header">
                            <strong>${comment.writerName}</strong> <span>(${comment.email})</span>
                            ${comment.isWriter ? `<span class="comment-writer-badge">작성자</span>` : ""}
                        </div>
                        <div class="comment-content">
                            ${comment.deleted ? '<em class="deleted">삭제된 댓글입니다.</em>' : comment.content}
                        </div>
                        <div class="comment-actions">
                            <span>${comment.createdAt}</span>
                            ${!comment.deleted ? `
                                | <a href="#" class="like-button ${likedClass}">♥ ${comment.likeCount}</a>
                                <a href="#" class="report-button" data-type="USER" data-target-id="${comment.id}" data-user-id="${comment.userId}">신고</a>
                                <a href="#" class="reply-link">답글</a>
                                ${isCommentWriter ? `<a href="#" class="reply-edit">수정</a><a href="#" class="reply-delete">삭제</a>` : ""}

                            ` : ""}
                        </div>
                    </div>
                </div>
                ${toggleButton}
                <div class="replies" style="display:none;">
                    ${repliesHtml}
                    ${showMoreReplies}
                </div>
            </div>
        `;

        $list.append(commentHtml);
    });

    if (comments.length > MAX_VISIBLE) {
        $list.append(`
            <div class="comments-show-more" data-count="${MAX_VISIBLE}" onclick="showMoreComments(this)">
                댓글 더보기 ▼
            </div>
        `);
    }
}

$(document).on("click", ".report-button", function (e) {
    e.preventDefault();

    const type = $(this).data("type");
    const targetId = $(this).data("target-id");
    const reportedUserId = $(this).data("user-id");

    const csrfToken = $('meta[name="_csrf"]').attr('content');
    const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    const form = $('<form>', {
        method: 'POST',
        action: '/support/report'
    });

    form.append($('<input>', { type: 'hidden', name: 'type', value: type }));
    form.append($('<input>', { type: 'hidden', name: 'targetId', value: targetId }));
    form.append($('<input>', { type: 'hidden', name: 'reportedUserId', value: reportedUserId }));
    form.append($('<input>', { type: 'hidden', name: '_csrf', value: csrfToken }));

    $('body').append(form);
    form.submit();
});

$(document).on("click", "#reportPostBtn", function (e) {
    e.preventDefault();

    const type = $(this).data("type"); // COMMUNITY_POST
    const targetId = $(this).data("target-id");
    const reportedUserId = $(this).data("user-id");

    const csrfToken = $('meta[name="_csrf"]').attr('content');

    const form = $('<form>', {
        method: 'POST',
        action: '/support/report'
    });

    form.append($('<input>', { type: 'hidden', name: 'type', value: type }));
    form.append($('<input>', { type: 'hidden', name: 'targetId', value: targetId }));
    form.append($('<input>', { type: 'hidden', name: 'reportedUserId', value: reportedUserId }));
    form.append($('<input>', { type: 'hidden', name: '_csrf', value: csrfToken }));

    $('body').append(form);
    form.submit();
});


function toggleReplies(button, count) {
    const $replies = $(button).next(".replies");
    const isVisible = $replies.is(":visible");

    if (isVisible) {
        $replies.slideUp(200);
        $(button).text(`답글 ${count}개 ▼`);
    } else {
        $replies.slideDown(200);
        $(button).text(`답글 ${count}개 ▲`);
    }
}

function showMoreComments(button) {
    const $hidden = $("#commentList .comment:hidden");
    const current = parseInt($(button).attr("data-count"));
    const next = current + 5;

    $hidden.slice(0, 5).show();
    $(button).attr("data-count", next);

    if ($("#commentList .comment:hidden").length === 0) {
        $(button).remove();
    }
}

function showMoreReplies(button) {
    const $parent = $(button).closest(".replies");
    const current = parseInt($(button).attr("data-count"));
    const next = current + 5;

    $parent.find(".reply").slice(current, next).show();
    $(button).attr("data-count", next);

    if ($parent.find(".reply:hidden").length === 0) {
        $(button).remove();
    }
}


function increaseViewCountIfNotViewed(postId) {
    const cookieName = "postView";
    const cookie = document.cookie
        .split("; ")
        .find(row => row.startsWith(cookieName + "="));

    let viewedPostIds = [];
    if (cookie) {
        viewedPostIds = cookie.split("=")[1].split(",");
        if (viewedPostIds.includes(postId.toString())) return;
    }

    $.post(`/api/community/posts/${postId}/view`);
    viewedPostIds.push(postId);
    const expires = new Date();
    expires.setHours(expires.getHours() + 24);
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

$(document).on("click", ".submit-comment", function () {
    const content = $(".comment-textarea").val().trim();
    if (!content) {
        alert("내용을 입력해주세요.");
        return;
    }

    const postId = $("#postId").val();

    $.ajax({
        url: "/api/community/comments",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            postId: postId,
            parentId: null,
            content: content
        }),
        success: function () {
            $(".comment-textarea").val("");
            loadComments(postId);
        },
        error: function () {
            alert("댓글 등록 실패");
        }
    });
});

$(document).on("click", ".reply-link", function (e) {
    e.preventDefault();
    const $parent = $(this).closest(".comment, .reply");

    // 이미 입력창이 있으면 제거
    $parent.find(".reply-input-box").remove();

    // 새로 입력창 삽입
    const html = `
        <div class="reply-input-box">
            <textarea class="reply-textarea" placeholder="답글을 입력하세요"></textarea>
            <button class="submit-reply">답글 등록</button>
        </div>
    `;
    $(this).closest(".comment-body").append(html);
});


$(document).on("click", ".submit-reply", function () {
    const $box = $(this).closest(".reply-input-box");
    const content = $box.find(".reply-textarea").val().trim();
    if (!content) {
        alert("내용을 입력해주세요.");
        return;
    }

    const postId = $("#postId").val();
    const parentId = $(this).closest(".comment, .reply").data("id");

    $.ajax({
        url: "/api/community/comments",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            postId: postId,
            parentId: parentId,
            content: content
        }),
        success: function () {
            loadComments(postId); // 새로고침
        },
        error: function () {
            alert("답글 등록 실패");
        }
    });
});

function loadComments(postId) {
    $.ajax({
        url: `/api/community/comments/${postId}`,
        method: "GET",
        success: function (comments) {
            renderComments(comments);
        },
        error: function () {
            alert("댓글을 불러오지 못했습니다.");
        }
    });
}

// 댓글 또는 대댓글 좋아요 클릭 처리
$(document).on("click", ".like-button", function (e) {
    e.preventDefault();

    const $likeBtn = $(this);
    const commentId = $likeBtn.closest(".comment, .reply").data("id");

    $.ajax({
        url: `/api/community/comments/${commentId}/like`,
        method: "POST",
        success: function (res) {
            $likeBtn.text(`♥ ${res.likeCount}`);

            if (res.liked) {
                $likeBtn.addClass("liked");
            } else {
                $likeBtn.removeClass("liked");
            }
        },
        error: function () {
            alert("좋아요 처리에 실패했습니다.");
        }
    });
});

// 댓글 수정 버튼 클릭
$(document).on("click", ".reply-edit", function (e) {
    e.preventDefault();

    const $comment = $(this).closest(".comment, .reply");
    const commentId = $comment.data("id");
    const $contentDiv = $comment.find(".comment-content");
    const originalContent = $contentDiv.text().trim();

    // 이미 수정창이 있다면 return
    if ($comment.find(".edit-textarea").length > 0) return;

    // textarea + 저장/취소 버튼 삽입
    const editHtml = `
        <div class="edit-box">
            <textarea class="edit-textarea">${originalContent}</textarea>
            <div class="edit-actions">
                <button class="save-edit" data-id="${commentId}">저장</button>
                <button class="cancel-edit">취소</button>
            </div>
        </div>
    `;
    $contentDiv.hide().after(editHtml);
});

// 댓글 수정 취소
$(document).on("click", ".cancel-edit", function () {
    const $editBox = $(this).closest(".edit-box");
    const $comment = $editBox.closest(".comment, .reply");

    $editBox.remove();
    $comment.find(".comment-content").show();
});

// 댓글 수정 저장
$(document).on("click", ".save-edit", function () {
    const $btn = $(this);
    const commentId = $btn.data("id");
    const $comment = $btn.closest(".comment, .reply");
    const newContent = $comment.find(".edit-textarea").val().trim();
    const postId = $("#postId").val();

    if (!newContent) {
        alert("내용을 입력해주세요.");
        return;
    }

    $.ajax({
        url: `/api/community/comments/${commentId}`,
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify({ content: newContent }),
        success: function () {
            loadComments(postId);
        },
        error: function () {
            alert("댓글 수정 실패");
        }
    });
});

$(document).on("click", ".reply-delete", function (e) {
    e.preventDefault();

    if (!confirm("정말 삭제하시겠습니까?")) return;

    const commentId = $(this).closest(".comment, .reply").data("id");
    const postId = $("#postId").val();

    $.ajax({
        url: `/api/community/comments/${commentId}`,
        method: "DELETE",
        success: function () {
            loadComments(postId);
        },
        error: function () {
            alert("댓글 삭제에 실패했습니다.");
        }
    });
});

$(document).on("click", "#deletePostBtn", function () {
    const postId = $("#postId").val();
    const boardCode = $(this).data("board-code");

    if (!confirm("정말 이 게시글을 삭제하시겠습니까?")) return;

    $.ajax({
        url: `/api/community/posts/${postId}`,
        method: "DELETE",
        success: function () {
            alert("삭제 완료");
            location.href = `/community/${boardCode}`;
        },
        error: function () {
            alert("삭제에 실패했습니다.");
        }
    });
});

