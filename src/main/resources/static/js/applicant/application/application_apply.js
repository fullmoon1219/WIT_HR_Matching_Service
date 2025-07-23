// applicant/recruit/apply.html

$(document).ready(function () {

	$("#resume-modal").dialog({
		autoOpen: false,
		modal: true,
		width: 700,
		height: 'auto',
		maxHeight: $(window).height() * 0.8,
		buttons: {
			"닫기": function() {
				$(this).dialog("close");
			}
		}
	});

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

				$('#submitButton').prop('disabled', true);
				$('#submitButton').after('<p>프로필을 완성하셔야 지원 가능합니다.</p>');
			}

			$('#jobPost-title').text(summary.title);
			$('#employer-companyName').text(summary.companyName);
			$('#jobPost-location').text(summary.location);
			$('#jobPost-employmentType').text(translateEmploymentType(summary.employmentType));
			$('#jobPost-deadline').text(summary.deadline);
			$('#jobPost-salary').text(formatSalary(summary.salary));

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
				location.href = `/recruit/view/${jobPostId}`;

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

	// 모달창으로 이력서 내용 확인
	$('#resumeListBody').on('click', '.resume-title-text', function() {

		const resumeId = $(this).closest('.resume-row').data('resume-id');
		const resumeTitle = $(this).text();

		$('#resume-modal').data('current-resume-id', resumeId);

		$('#resume-modal').dialog('option', 'title', resumeTitle);
		$('#resume-modal').html('<p>로딩 중...</p>').dialog('open');

		$.ajax({
			url: `/api/resumes/${resumeId}`,
			method: 'GET',
			success: function (resume) {

				function createInfoRow(label, value) {
					if (!value || String(value).trim() === '') {
						return '';
					}
					return `
                        <div class="info-row">
                            <div class="info-label">${label}</div>
                            <div class="info-value">${value}</div>
                        </div>
                    `;
				}

				let modalContentHtml = '<div class="info-container">';

				modalContentHtml += createInfoRow('학력', resume.education);
				modalContentHtml += createInfoRow('경력', resume.experience);
				modalContentHtml += createInfoRow('희망 직무', resume.desiredPosition);
				modalContentHtml += createInfoRow('기술 스택', resume.skills);
				modalContentHtml += createInfoRow('선호 지역', resume.preferredLocation);
				modalContentHtml += createInfoRow('희망 연봉', resume.salaryExpectation);
				modalContentHtml += createInfoRow('핵심 역량', resume.coreCompetency);
				modalContentHtml += createInfoRow('지원 동기', resume.motivation);

				modalContentHtml += '</div>';

				$('#resume-modal').html(modalContentHtml);
			},
			error: function (xhr) {
				if (xhr.status === 403) {
					// 권한 없는 경우
					location.href = '/error/access-denied';
				} else if (xhr.status === 404) {
					// 없는 이력서
					location.href = '/error/not-found';
				} else {
					alert('이력서 불러오기에 실패했습니다. 나중에 다시 시도해주세요.');
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
      <div class="resume-row" data-resume-id="${resume.id}">
        <div class="resume-left">
          <input type="radio" name="selectedResume" value="${resume.id}">
          ${resume.isPublic ? '<span class="resume-represent">(대표)</span>' : ''}
          <span class="resume-title-text">${resume.title}</span>
        </div>
        <div class="resume-updated">${resume.updatedAt}</div>
      </div>
    `;
}
