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
            <td>
                ${isPublic
        ? '<button disabled>수정</button>'
        : `<button onclick="location.href='/applicant/resume/edit/${resume.id}'">수정</button>`}
            </td>
            <td>
                ${isPublic
        ? '<button disabled>삭제</button>'
        : `<button onclick="deleteResume(${resume.id})">삭제</button>`}
            </td>
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
                        <td colspan="4" style="text-align: center;">임시 저장된 이력서가 없습니다</td>
                    </tr>
                `;
                tbody.append(emptyRow);
                return; // 데이터 없으면 여기서 끝
            }

            drafts.forEach(draft => {
                const row = `
                    <tr>
                        <td><a href="/applicant/resume/view/${draft.id}">${draft.title}</a></td>
                        <td>${draft.updatedAt}</td>
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

    if (isPublic) {
        if (!confirm('대표 이력서를 해제하시겠습니까?')) return;

        // 공개 상태일 경우 비공개 전환 요청, 아니면 대표 이력서로 설정 요청
        $.ajax({
            url: `/api/resumes/${resumeId}/private`,
            method: 'PUT',
            success: function () {
                loadResumes();
            },
            error: function (xhr) {
                if (xhr.status === 403) {
                    location.href = '/error/access-denied';
                } else if (xhr.status === 404) {
                    location.href = '/error/not-found';
                } else {
                    alert('대표 해제에 실패했습니다. 나중에 다시 시도해주세요.');
                    console.error(xhr);
                }
            }
        });
    } else {
        if (!confirm('이 이력서를 대표 이력서로 설정하시겠습니까?')) return;

        $.ajax({
            url: `/api/resumes/${resumeId}/public`,
            method: 'PUT',
            success: function () {
                loadResumes();
            },
            error: function (xhr) {
                if (xhr.status === 403) {

                    const response = xhr.responseJSON;
                    if (response && response.message === '개인정보 입력이 완료되지 않았습니다.') {
                        if (confirm('개인정보 입력이 완료되지 않아 대표 이력서로 설정이 불가능합니다.\n\n지금 개인정보를 입력하러 가시겠습니까?')) {
                            // TODO: 추후 경로 수정
                            location.href = '/applicant/profile';
                        }

                    } else {
                        location.href = '/error/access-denied';
                    }
                } else if (xhr.status === 404) {
                    location.href = '/error/not-found';
                } else {
                    alert('대표 공개 설정에 실패했습니다. 나중에 다시 시도해주세요.');
                    console.error(xhr);
                    console.log(xhr.responseText);
                }
            }
        });
    }
}
