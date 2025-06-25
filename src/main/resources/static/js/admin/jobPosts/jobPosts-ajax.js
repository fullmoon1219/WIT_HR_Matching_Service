// /js/admin/jobPosts/jobPosts-ajax.js

$(document).ready(function () {
    loadJobPosts(1);
});

function loadJobPosts(page = 1, filters = currentFilters) {
    currentPage = page;

    const keyword = filters.keyword || '';
    const isClosed = filters.isClosed;
    const isDeleted = filters.isDeleted;

    // 문자열 → Boolean 변환
    const statusParam = isClosed === 'closed' ? true : isClosed === 'active' ? false : null;
    const deletedParam = isDeleted === 'deleted' ? true : isDeleted === 'active' ? false : null;

    $.ajax({
        url: '/api/admin/posts/list',
        method: 'GET',
        data: {
            keyword: keyword,
            isClosed: statusParam,
            isDeleted: deletedParam
        },
        success: function (response) {
            renderTable(response.content);
            renderPagination(
                response.totalPages,
                response.number + 1,
                (selectedPage) => {
                    loadJobPosts(selectedPage, currentFilters);
                }
            );
        },
        error: function () {
            alert("공고 목록을 불러오는 데 실패했습니다.");
        }
    });
}

function renderTable(posts) {
    const $tbody = $('#userTableBody');
    $tbody.empty();

    if (posts.length === 0) {
        $tbody.append('<tr><td colspan="9">공고가 없습니다.</td></tr>');
        return;
    }

    posts.forEach(post => {
        const isDeleted = !!post.deletedAt;
        const rowClass = isDeleted ? 'deleted-row' : '';

        const isClosed = post.deadline && isPast(post.deadline);
        const closedLabel = isClosed ? "마감" : "진행중";
        const closedClass = isClosed ? "bg-red" : "bg-green";

        const deletedBadge = isDeleted ? `<span class="value bg-red">삭제됨</span>` : "";

        $tbody.append(`
            <tr class="${rowClass}">
                <td><input type="checkbox" class="user-checkbox" value="${post.id}"></td>
                <td>${post.id}</td>
                <td>${post.userId}</td>
                <td class="post-name" data-id="${post.userId}" style="cursor: pointer; color: black;"><span>${post.companyName}</span></td>
                <td class="post-title" data-id="${post.id}" style="cursor: pointer; color: blue;"><span>${post.title}</span></td>
                <td>${formatDate(post.createAt)}</td>
                <td>${formatDate(post.updatedAt)}</td>
                <td><div class="value isClosed ${closedClass}">${closedLabel}</div></td>
                <td><div class="value isDeleted">${deletedBadge}</div></td>
            </tr>
        `);
    });
}

function formatDate(dateTime) {
    if (!dateTime) return '-';
    return dateTime.replace('T', ' ').split('.')[0]; // ISO 문자열 포맷 처리
}

function isPast(dateStr) {
    return new Date(dateStr) < new Date();
}
