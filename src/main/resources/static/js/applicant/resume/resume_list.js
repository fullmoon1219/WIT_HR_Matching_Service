// applicant/resume/list.html

$(document).ready(function () {
    loadResumes();        // 이력서 목록 로드
    loadDraftResumes();   // 임시 저장 이력서 목록 로드
});

function loadResumes() {

    // 대표 이력서와 완성된 이력서 조회
    $.ajax({
        url: '/api/resumes/completed',
        method: 'GET',
        success: function (resumes) {

            const listContainer = $('#completedResumeList');
            listContainer.empty();

            if (!resumes || resumes.length === 0) {
                const emptyItem = '<li class="empty-list">작성된 이력서가 없습니다.</li>';
                listContainer.append(emptyItem);
                return;
            }

            resumes.forEach(resume => {
                const listItem = makeCompletedListItem(resume);
                listContainer.append(listItem);
            });
        },
        error: function (xhr) {
            if (xhr.status === 403) {
                location.href = '/error/access-denied';
            } else if (xhr.status === 404) {
                location.href = '/error/not-found';
            } else {
                location.href = '/error/db-access-denied';
                console.error("이력서 로딩 중 오류 발생:", xhr);
            }
        }
    });

}

function makeCompletedListItem(resume) {

    const isPublic = resume.isPublic;
    const starButton = isPublic
        ? `<button class="star-button selected" title="대표 이력서 해제" onclick="handlePublicClick(${resume.id}, true)">★</button>`
        : `<button class="star-button" title="대표 이력서로 설정" onclick="handlePublicClick(${resume.id}, false)">☆</button>`;

    // 대표 이력서(isPublic=true)는 수정/삭제 버튼을 비활성화
    const actionButtons = isPublic
        ? `<button class="btn-small" disabled>수정</button> <button class="btn-small" disabled>삭제</button>`
        : `<button class="btn-small" onclick="location.href='/applicant/resume/edit/${resume.id}'">수정</button>
           <button class="btn-small" onclick="deleteResume(${resume.id})">삭제</button>`;

    return `
        <li>
            <div class="resume-main-select">${starButton}</div>
            <div class="resume-info">
                <a href="/applicant/resume/view/${resume.id}" class="resume-title-link">
                    <div class="resume-title">${resume.title}</div>
                </a>
                <div class="resume-date">${resume.updatedAt}</div>
            </div>
            <div class="resume-actions-inline">${actionButtons}</div>
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
                const emptyItem = '<li class="empty-list">임시 저장된 이력서가 없습니다.</li>';
                listContainer.append(emptyItem);
                return;
            }

            drafts.forEach(draft => {
                const listItem = makeDraftListItem(draft);
                listContainer.append(listItem);
            });
        },
        error: function (xhr) {
            console.log('임시 저장 이력서 로딩 중 오류: ', xhr);
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
                style: {
                    background: "#6495ED",
                    color: "#ffffff"
                }
            }).showToast();

            loadResumes();
            loadDraftResumes();
        },
        error: function (xhr) {
            if (xhr.status === 403) {
                location.href = '/error/access-denied';
            } else if (xhr.status === 404) {
                location.href = '/error/not-found';
            } else {
                alert('이력서 삭제에 실패했습니다. 나중에 다시 시도해주세요.');
                console.error(xhr);
            }
        }
    });
}

function handlePublicClick(resumeId, isPublic) {

    const action = isPublic ? '해제' : '설정';
    const url = isPublic ? `/api/resumes/${resumeId}/private` : `/api/resumes/${resumeId}/public`;

    if (!confirm(`이 이력서를 대표 이력서로 ${action}하시겠습니까?`)) {
        return;
    }

    $.ajax({
        url: url,
        method: 'PUT',
        success: function () {
            Toastify({
                text: `대표 이력서가 성공적으로 ${action}되었습니다.`,
                duration: 2000,
                gravity: "top",
                position: "center",
                stopOnFocus: false,
                style: {
                    background: "#6495ED",
                    color: "#ffffff"
                }
            }).showToast();

            loadResumes();
        },
        error: function (xhr) {
            if (xhr.status === 403) {

                const response = xhr.responseJSON;

                if (!isPublic && response && response.message === '개인정보 입력이 완료되지 않았습니다.') {

                    if (confirm('개인정보 입력이 완료되지 않아 대표 이력서로 설정할 수 없습니다.\n\n지금 개인정보를 입력하러 가시겠습니까?')) {
                        location.href = '/applicant/profile'; // TODO: 추후 경로 수정
                    }
                } else {
                    location.href = '/error/access-denied';
                }

            } else if (xhr.status === 404) {
                location.href = '/error/not-found';

            } else {
                alert(`대표 이력서 ${action}에 실패했습니다. 나중에 다시 시도해주세요.`);
                console.error(xhr);
            }
        }
    });
}
