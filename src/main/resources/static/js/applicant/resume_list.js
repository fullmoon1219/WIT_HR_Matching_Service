// applicant/resume/list.html

$(document).ready(function () {
    loadResumes();        // 이력서 목록 로드
    loadDraftResumes();   // 임시 저장 이력서 목록 로드
});

function loadResumes() {
    $.ajax({
        url: '/api/resumes', // 이력서 목록 REST API URL
        method: 'GET',
        success: function (resumes) {
            const tbody = $('#resumeListBody');
            tbody.empty();

            // 이력서가 없는 경우 문구 출력
            if (resumes.length === 0) {
                const emptyRow = `
                    <tr>
                        <td colspan="3" style="text-align: center;">이력서가 없습니다</td>
                    </tr>
                `;
                tbody.append(emptyRow);
                return; // 데이터 없으면 여기서 끝
            }

            resumes.forEach(resume => {
                const row = `
                    <tr>
                        <td>${resume.title}</td>
                        <td><a href="/applicant/resume/edit/${resume.id}">수정</a></td>
                        <td><button onclick="deleteResume(${resume.id})">삭제</button></td>
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
                        <td>${draft.title}</td>
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