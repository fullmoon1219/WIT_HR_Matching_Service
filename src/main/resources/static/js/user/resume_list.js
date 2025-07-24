$(document).ready(function () {
    loadResumes();        // 완료 이력서 로드
    loadDraftResumes();   // 임시 저장 이력서 로드
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
        <button class="btn-small mock-btn" onclick="location.href='/mock-interview/start/${resume.id}'">
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

function loadDraftResumes() {
    $.ajax({
        url: '/api/resumes/draft',
        method: 'GET',
        success: function (drafts) {
            const listContainer = $('#draftResumeList');
            listContainer.empty();

            if (!drafts || drafts.length === 0) {
                listContainer.append('<li class="empty-list">임시 저장된 이력서가 없습니다.</li>');
                return;
            }

            drafts.forEach(draft => {
                const listItem = makeDraftListItem(draft);
                listContainer.append(listItem);
            });
        },
        error: function () {
            location.href = '/error/db-access-denied';
        }
    });
}

function makeDraftListItem(draft) {
    return `
        <li>
            <div class="resume-info">
                 <a href="/applicant/resume/edit/${draft.id}" class="resume-title-link">
                    <div class="resume-title">${draft.title}</div>
                </a>
                <div class="resume-date">${draft.updatedAt}</div>
            </div>
            <div class="resume-actions-inline">
                <button class="btn-small" onclick="location.href='/applicant/resume/edit/${draft.id}'">수정</button>
                <button class="btn-small" onclick="deleteResume(${draft.id})">삭제</button>
            </div>
        </li>
    `;
}

function deleteResume(resumeId) {
    if (!confirm('정말 삭제하시겠습니까?')) return;

    $.ajax({
        url: `/api/resumes/${resumeId}`,
        method: 'DELETE',
        success: function () {
            Toastify({
                text: '이력서가 삭제되었습니다.',
                duration: 2000,
                gravity: "top",
                position: "center",
                stopOnFocus: false,
                style: { background: "#6495ED", color: "#fff" }
            }).showToast();

            loadResumes();
            loadDraftResumes();
        },
        error: function (xhr) {
            if (xhr.status === 403) location.href = '/error/access-denied';
            else if (xhr.status === 404) location.href = '/error/not-found';
            else alert('이력서 삭제에 실패했습니다. 나중에 다시 시도해주세요.');
        }
    });
}
