// /js/admin/resume/resume-list-ajax.js

$(document).ready(function () {
    loadResumeList();
});

function loadResumeList() {
    $.ajax({
        url: "/api/admin/resumes/list",
        method: "GET",
        data: {
            includeDeleted: true, // 삭제된 이력서 포함
            page: 0,
            size: 20
        },
        success: function (response) {
            const resumes = response.content;
            const tbody = $("#userTableBody");
            tbody.empty();

            resumes.forEach(resume => {
                const deleted = resume.deletedAt !== null;
                const isCompleted = resume.isCompleted;
                const isPublic = resume.isPublic;

                const completedLabel = isCompleted ? "✅ 완료" : "❌ 미완료";
                const publicLabel = isPublic ? "공개" : "비공개";

                const publicClass = isPublic ? "bg-green" : "bg-gray";
                const deletedBadge = deleted
                    ? `<span class="value bg-red">삭제됨</span>`
                    : "";

                const row = `
                <tr class="${deleted ? 'deleted-row' : ''}">
                    <td><input type="checkbox" value="${resume.id}"></td>
                    <td>${resume.id}</td>
                    <td>${resume.userId}</td>
                    <td>${resume.email || '-'}</td>
                    <td><span class="resume-title" data-id="1" style="cursor: pointer; color: blue;">${resume.title}</span></td>
                    <td>${resume.createAt || '-'}</td>
                    <td>${resume.updatedAt || '-'}</td>
                    <td>${completedLabel}</td>
                    <td>
                        <div class="value isPublic ${publicClass}">
                            ${publicLabel}
                        </div>
                    </td>
                    <td>
                        <div class="value isDeleted">
                            ${deletedBadge}
                        </div>
                    </td>
                </tr>
                `;
                tbody.append(row);
            });
        },
        error: function (xhr) {
            alert("이력서 목록을 불러오는 중 오류가 발생했습니다.");
            console.error(xhr);
        }
    });
}

