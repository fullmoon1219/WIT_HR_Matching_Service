$(document).ready(function () {
    loadResumes();
});

function loadResumes() {
    $.ajax({
        url: '/api/resumes/completed',
        method: 'GET',
        success: function (resumes) {
            const listContainer = $('#completedResumeList');
            listContainer.empty();

            if (!resumes || resumes.length === 0) {
                listContainer.append('<li class="empty-list">작성된 이력서가 없습니다.</li>');
                return;
            }

            resumes.forEach(resume => {
                const listItem = makeCompletedListItem(resume);
                listContainer.append(listItem);
            });
        },
        error: function (xhr) {
            if (xhr.status === 403) location.href = '/error/access-denied';
            else if (xhr.status === 404) location.href = '/error/not-found';
            else location.href = '/error/db-access-denied';
        }
    });
}

function makeCompletedListItem(resume) {
    const starButton = resume.isPublic
        ? `<span class="star-icon selected" title="대표 이력서">★</span>`
        : `<span class="star-icon" title="대표 이력서 아님">☆</span>`;

    const actionButton = `
        <button class="btn-small mock-btn" onclick="location.href='/interview/answer/${resume.id}'">
            모의면접 이동하기
        </button>
    `;

    return `
        <li>
            <div class="resume-main-select">${starButton}</div>
            <div class="resume-info">
                <a href="/applicant/resume/view/${resume.id}" class="resume-title-link">
                    <div class="resume-title">${resume.title}</div>
                </a>
                <div class="resume-date">${resume.updatedAt}</div>
            </div>
            <div class="resume-actions-inline">${actionButton}</div>
        </li>
    `;
}
