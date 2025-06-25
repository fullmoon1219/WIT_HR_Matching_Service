// applicant/resume/edit.html

$(document).ready(function () {

	const resumeId = getResumeIdFromUrl();

	$.ajax({
		url: `/api/resumes/${resumeId}`,
		method: 'GET',
		success: function (resume) {
			$('#title').val(resume.title);
			$('#education').val(resume.education);
			$('#experience').val(resume.experience);
			$('#skills').val(resume.skills);
			$('#preferredLocation').val(resume.preferredLocation);
			$('#salaryExpectation').val(resume.salaryExpectation);
			$('#coreCompetency').val(resume.coreCompetency);
			$('#desiredPosition').val(resume.desiredPosition);
			$('#motivation').val(resume.motivation);

			$('body').show();

		},
		error: function (xhr) {
			if (xhr.status === 403) {
				// 권한 없는 경우
				location.href = '/error/access-denied';
			} else if (xhr.status === 404) {
				// 없는 이력서
				location.href = '/error/not-found';
			} else {
				// 그 외 에러
				location.href = '/error/db-access-denied';
				console.error(xhr);
			}
		}
	});

	$('#btnRegister').click(function () {

		if (!validateResumeForm()) return;

		const resume = {
			title: $('input[name="title"]').val(),
			education: $('input[name="education"]').val(),
			experience: $('input[name="experience"]').val(),
			skills: $('input[name="skills"]').val(),
			preferredLocation: $('input[name="preferredLocation"]').val(),
			salaryExpectation: Number($('input[name="salaryExpectation"]').val()),
			desiredPosition: $('input[name="desiredPosition"]').val(),
			motivation: $('input[name="motivation"]').val(),
			coreCompetency: $('input[name="coreCompetency"]').val(),
			isCompleted: true // 정식 등록
		};

		$.ajax({
			url: `/api/resumes/${resumeId}`,
			method: 'PUT',
			contentType: 'application/json',
			data: JSON.stringify(resume),
			success: function (response) {
				if (response.success) {
					alert('등록이 완료되었습니다.');
					location.href = `/applicant/resume/view/${response.id}`;
				} else {
					location.href = '/error/db-access-denied';
				}
			},
			error: function (xhr) {

				if (xhr.status === 400 && xhr.responseJSON && xhr.responseJSON.errors) {

					// 유효성 검사 실패
					const errors = xhr.responseJSON.errors;

					$('.error').text('');

					errors.forEach(err => {
						const field = err.field;
						const message = err.defaultMessage;
						$(`#error-${field}`).text(message);
					});

				} else if (xhr.status === 403) {
					// 권한 없음
					location.href = '/error/access-denied';

				} else if (xhr.status === 404) {
					// 없는 이력서
					location.href = '/error/not-found';

				} else {
					alert('서버 오류가 발생했습니다. 나중에 다시 시도해주세요.');
					console.error(xhr);
				}
			}
		});
	});

	$('#btnDraft').click(function () {

		$('#error-title').text('');

		const $titleInput = $('input[name="title"]');
		const title = $.trim($titleInput.val());

		if (title === '') {
			$('#error-title').text('제목을 입력해주세요.');

			// 포커스 이동 + 부드러운 스크롤
			$titleInput[0].scrollIntoView({ behavior: 'smooth', block: 'center' });
			$titleInput.focus();
			return;
		}

		const resume = {
			title: title,
			education: $('input[name="education"]').val(),
			experience: $('input[name="experience"]').val(),
			skills: $('input[name="skills"]').val(),
			preferredLocation: $('input[name="preferredLocation"]').val(),
			salaryExpectation: $('input[name="salaryExpectation"]').val(),
			desiredPosition: $('input[name="desiredPosition"]').val(),
			motivation: $('input[name="motivation"]').val(),
			coreCompetency: $('input[name="coreCompetency"]').val(),
			isCompleted: false // 임시 저장
		};

		$.ajax({
			url: `/api/resumes/draft/${resumeId}`,
			method: 'PUT',
			contentType: 'application/json',
			data: JSON.stringify(resume),
			success: function (response) {
				if (response.success) {
					alert('임시 저장이 완료되었습니다.');
					location.href = '/applicant/resume/list';
				} else {
					location.href = '/error/db-access-denied';
				}
			},
			error: function (xhr) {
				if (xhr.status === 403) {
					location.href = '/error/access-denied';

				} else if (xhr.status === 404) {
					location.href = '/error/not-found';

				} else {
					// 그 외 (서버 오류 등)
					alert('서버 오류가 발생했습니다. 나중에 다시 시도해주세요.');
					console.error(xhr);
				}
			}
		});
	});

});

function getResumeIdFromUrl() {
	const parts = window.location.pathname.split('/');
	return parts[parts.length - 1];
}