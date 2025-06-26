// applicant/recruit/apply.html

$(document).ready(function () {

	const jobPostId = getIdFromUrl();

	// 현재 지원하기를 선택한 공고 정보 가져오기
	$.ajax({
		url: `/api/recruit/${jobPostId}/summary`,
		method: 'GET',
		success: function (summary) {

			if (summary.isApplied === true) {
				$('body').empty().html(`
                    <div class="application-status-message" style="text-align: center; padding: 50px;">
                        <h2>이미 지원한 공고입니다.</h2>
                        <button onclick="location.href='/recruit/list'">공고 목록으로</button>
                    </div>`);
				return;
			}

			if (summary.isProfileComplete === false) {

				Toastify({
					text: '프로필을 완성하셔야 지원 가능합니다.',
					duration: 5000,
					gravity: 'top',
					position: 'center',
					stopOnFocus: false,
					style: {
						background: '#CD5C5C',
						color: '#ffffff'
					}
				}).showToast();

				$('#manageProfile').show();
				$('#submitButton').prop('disabled', true);
				$('#submitButton').after('<p>프로필을 완성하셔야 지원 가능합니다.</p>');
			}

			$('#jobPost-title').text(summary.title);
			$('#employer-companyName').text(summary.companyName);
			$('#jobPost-location').text(summary.location);
			$('#jobPost-employmentType').text(translateEmploymentType(summary.employmentType));
			$('#jobPost-deadline').text(summary.deadline);

			// 이력서 목록 불러오기
			loadApplicantResumes();

		},
		error: function (xhr) {
			if (xhr.status === 403) {
				location.href = '/error/access-denied';
			} else if (xhr.status === 404) {
				location.href = '/error/not-found';
			} else {
				location.href = '/error/db-access-denied';
				console.error(xhr);
			}
		}
	});

	// 이력서 관리 페이지로 이동
	$('#manageResumeBtn').on('click', function () {
		location.href = '/applicant/resume/list';
	});

	// 선택한 이력서를 해당 공고에 제출
	$('#submitButton').on('click', function () {
		if (!confirm('선택한 이력서로 지원하시겠습니까?')) return;

		const resumeId = $('input[name="selectedResume"]:checked').val();

		if (!resumeId) {
			alert('이력서를 선택해주세요.');
			return;
		}

		$.ajax({
			url:`/api/recruit/${jobPostId}`,
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({
				resumeId: resumeId
			}),
			success: function () {

				sessionStorage.setItem('toastMessage', '성공적으로 지원되었습니다.');
				location.href = `/applicant/recruit/view/${jobPostId}`;

			},
			error: function (xhr) {
				if (xhr.status === 403) {
					location.href = '/error/access-denied';
				} else if (xhr.status === 404) {
					location.href = '/error/not-found';
				} else if (xhr.status === 409) {
					alert('이미 지원한 공고입니다.');
				} else {
					alert('서버 오류가 발생했습니다. 나중에 다시 시도해주세요.');
					console.error(xhr);
				}
			}
		});
	});
});

function loadApplicantResumes() {
	const tbody = $('#resumeListBody');

	// 대표 이력서부터 조회
	$.ajax({
		url: '/api/resumes/completed',
		method: 'GET',
		success: function (resumes) {

			tbody.empty();

			if (!resumes || resumes.length === 0) {
				const emptyRow = `<tr><td colspan="3" style="text-align: center;">작성된 이력서가 없습니다. <a href="/applicant/resume/list">이력서 관리</a> 페이지에서 이력서를 먼저 작성해주세요.</td></tr>`;
				tbody.append(emptyRow);
				return;
			}

			resumes.forEach(resume => {
				const row = makeRow(resume);
				tbody.append(row);
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

function makeRow(resume) {
	return `
         <tr>
            <td><input type="radio" name="selectedResume" value="${resume.id}"></td>
            <td>${resume.isPublic ? ' <strong>(대표)</strong>' : ''}${resume.title}</td>
            <td>${resume.updatedAt}</td>
        </tr>
    `;
}