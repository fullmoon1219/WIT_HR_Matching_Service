// applicant/resume/list.html

$(document).ready(function () {
    loadResumes();        // 이력서 목록 로드
    loadDraftResumes();   // 임시 저장 이력서 목록 로드
});

function loadResumes() {

    const tbody = $('#resumeListBody');
    tbody.empty();

    // 대표 이력서부터 조회
    $.ajax({
        url: '/api/resumes/public',
        method: 'GET',
        success: function (publicResume) {

            if (publicResume) {
                const row = makeRow(publicResume, true);
                tbody.append(row);
            }

            // 비공개 이력서 목록 조회
            $.ajax({
                url: '/api/resumes',
                method: 'GET',
                success: function (resumes) {

                    // 대표 이력서 외의 이력서가 없는 경우
                    if (!publicResume && resumes.length === 0) {
                        const emptyRow = `<tr><td colspan="4" style="text-align: center;">이력서가 없습니다</td></tr>`;
                        tbody.append(emptyRow);
                        return;
                    }

                    resumes.forEach(resume => {
                        const row = makeRow(resume, false);
                        tbody.append(row);
                    });
                },
                error: function () {
                    location.href = '/error/db-access-denied';
                }
            });
        },
        error: function () {

            // 대표 이력서가 없는 경우 비공개 이력서 목록만 조회
            $.ajax({
                url: '/api/resumes',
                method: 'GET',
                success: function (resumes) {
                    if (resumes.length === 0) {
                        const emptyRow = `<tr><td colspan="4" style="text-align: center;">이력서가 없습니다</td></tr>`;
                        tbody.append(emptyRow);
                        return;
                    }

                    resumes.forEach(resume => {
                        const row = makeRow(resume, false);
                        tbody.append(row);
                    });
                },
                error: function () {
                    location.href = '/error/db-access-denied';
                }
            });
        }
    });
}

function makeRow(resume, isPublic) {
    return `
        <tr>
            <td onclick="handlePublicClick(${resume.id}, ${isPublic})" style="cursor: pointer;">
                ${isPublic ? '<span>◆</span>' : '<span>◇</span>'}
            </td>
            <td><a href="/applicant/resume/view/${resume.id}">${resume.title}</a></td>
            <td>${resume.updatedAt}</td>
            <td><a href="/applicant/resume/edit/${resume.id}">수정</a></td>
            <td><button onclick="deleteResume(${resume.id})">삭제</button></td>
        </tr>
    `;
}

function loadDraftResumes() {
    $.ajax({
        url: '/api/resumes/draft', // 임시 저장 목록 REST API URL
        method: 'GET',
        success: function (drafts) {
            const tbody = $('#draftListBody');
            tbody.empty();

            if (drafts.length === 0) {
                const emptyRow = `
                    <tr>
                        <td colspan="3" style="text-align: center;">임시 저장된 이력서가 없습니다</td>
                    </tr>
                `;
                tbody.append(emptyRow);
                return; // 데이터 없으면 여기서 끝
            }

            drafts.forEach(draft => {
                const row = `
                    <tr>
                        <td><a href="/applicant/resume/view/${draft.id}">${draft.title}</a></td>
                        <td>${resume.updatedAt}</td>
                        <td><a href="/applicant/resume/edit/${draft.id}">수정</a></td>
                        <td><button onclick="deleteResume(${draft.id})">삭제</button></td>
                    </tr>
                `;
                tbody.append(row);
            });
        },
        error: function () {
            location.href = '/error/db-access-denied';
        }
    });
}

function deleteResume(resumeId) {

    if (!confirm('정말 삭제하시겠습니까?')) return;

    $.ajax({
        url: `/api/resumes/${resumeId}`,
        method: 'DELETE',
        success: function () {
            loadResumes();
            loadDraftResumes();
        },
        error: function () {
            location.href = '/error/db-access-denied';
        }
    });
}

function handlePublicClick(resumeId, isPublic) {

    if (isPublic) {
        alert('이미 대표 이력서입니다.');
        return;
    }

    if (!confirm('이 이력서를 대표 이력서로 설정하시겠습니까?')) return;

    $.ajax({
        url: `/api/resumes/${resumeId}/public`,
        method: 'PUT',
        success: function () {
            loadResumes();
        },
        error: function () {
            alert('대표 공개 설정에 실패했습니다.');
        }
    });
}