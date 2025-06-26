// /js/admin/resume/resume-list-ajax.js

function convertToServerValue(type, value) {
    if (type === 'isCompleted') {
        return value === 'completed';
    } else if (type === 'isPublic') {
        return value === 'public';
    } else if (type === 'isDeleted') {
        return value === 'deleted';
    }
    return undefined;
}

$(document).ready(function () {
    loadResumeList(1);
});

// 필터 기반 이력서 목록 로드
function loadResumeList(page = 1, filters = currentFilters) {
    const params = { page };

    if (filters.isCompleted !== '') {
        params.isCompleted = convertToServerValue('isCompleted', filters.isCompleted);
    }

    if (filters.isPublic !== '') {
        params.isPublic = convertToServerValue('isPublic', filters.isPublic);
    }

    if (filters.isDeleted === '') {
        delete params.includeDeleted;
    } else if (filters.isDeleted === 'deleted') {
        params.includeDeleted = true;
    } else {
        params.includeDeleted = false;
    }

    if (filters.keyword.trim() !== '') {
        params.keyword = filters.keyword.trim();
    }

    $.ajax({
        url: "/api/admin/resumes", // ✅ RESTful하게 변경됨
        method: "GET",
        data: params,
        success: function (response) {
            const resumes = response.content;
            const tbody = $("#userTableBody");
            tbody.empty();

            resumes.forEach(resume => {
                const deleted = resume.deletedAt !== null;
                const isCompleted = resume.isCompleted === true || resume.isCompleted === 1 || resume.isCompleted === '1' || resume.isCompleted === 'true';
                const isPublic = resume.isPublic;

                const completedLabel = isCompleted ? "✅ 완료" : "❌ 미완료";
                const publicLabel = isPublic ? "공개" : "비공개";
                const publicClass = isPublic ? "bg-green" : "bg-gray";
                const deletedBadge = deleted ? `<span class="value bg-red">삭제됨</span>` : "";

                const row = `
                <tr class="${deleted ? 'deleted-row' : ''}">
                    <td><input type="checkbox" class="user-checkbox" value="${resume.id}"></td>
                    <td>${resume.id}</td>
                    <td>${resume.userId}</td>
                    <td class="resume-email" data-userid="${resume.userId}" style="cursor: pointer; color: black;"><span>${resume.email || '-'}</span></td>
                    <td class="resume-title" data-id="${resume.id}" style="cursor: pointer; color: blue;"><span>${resume.title}</span></td>
                    <td>${resume.createAt || '-'}</td>
                    <td>${resume.updatedAt || '-'}</td>
                    <td>${completedLabel}</td>
                    <td><div class="value isPublic ${publicClass}">${publicLabel}</div></td>
                    <td><div class="value isDeleted">${deletedBadge}</div></td>
                </tr>
                `;
                tbody.append(row);
            });

            renderPagination(response.totalPages, response.number + 1, (page) => {
                loadResumeList(page, filters);
            });
        },
        error: function (xhr) {
            alert("이력서 목록을 불러오는 중 오류가 발생했습니다.");
            console.error(xhr);
        }
    });
}
