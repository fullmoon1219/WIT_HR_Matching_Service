// /js/admin/memo-management.js

$(document).ready(function () {
    const $memoContainer = $('#memo-container');
    const $memoTextarea = $('.memo-card textarea');

    // Thymeleaf에서 렌더링한 관리자 ID
    const adminId = parseInt($('#admin-id').val());

    // 메모 목록 불러오기
    function loadMemos() {
        $.ajax({
            url: '/api/admin/memos',
            method: 'GET',
            success: function (memos) {
                $memoContainer.empty();
                if (memos.length === 0) {
                    $memoContainer.append('<p style="color: #777;">등록된 메모가 없습니다.</p>');
                    return;
                }
                memos.forEach(memo => {
                    const memoEl = $(`
                        <div class="memo-item" data-id="${memo.id}">
                            <span class="delete-memo" style="cursor: pointer; color: red;">[X]</span>
                            <div class="memo-header">${memo.adminName}</div>
                            <div class="memo-content">${memo.content}</div>
                        </div>
                    `);
                    $memoContainer.append(memoEl);
                });

            },
            error: function () {
                console.error('메모 불러오기 실패');
            }
        });
    }

    // 메모 저장
    $('.memo-btn').on('click', function () {
        const content = $memoTextarea.val().trim();
        if (!content) return;

        $.ajax({
            url: '/api/admin/memos',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                userId: adminId,
                content: content
            }),
            success: function () {
                $memoTextarea.val('');
                loadMemos();
            },
            error: function () {
                alert('메모 저장 실패');
            }
        });
    });

    // 메모 삭제
    $memoContainer.on('click', '.delete-memo', function () {
        const memoId = $(this).closest('.memo-item').data('id');

        $.ajax({
            url: `/api/admin/memos/${memoId}`,
            method: 'DELETE',
            success: function () {
                loadMemos();
            },
            error: function () {
                alert('메모 삭제 실패');
            }
        });
    });

    // 초기 메모 로딩
    loadMemos();
});

