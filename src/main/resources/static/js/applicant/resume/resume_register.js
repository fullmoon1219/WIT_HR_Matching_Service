// applicant/resume/register.html

$(document).ready(function () {

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
			url: '/api/resumes',
			method: 'POST',
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

				if (xhr.responseJSON && xhr.responseJSON.errors) {
					const errors = xhr.responseJSON.errors;

					$('.error').text('');

					errors.forEach(err => {
						const field = err.field;
						const message = err.defaultMessage;

						// 해당 input 아래 에러 div가 있을 경우만 출력
						$(`#error-${field}`).text(message);
					});

				} else {
					alert('서버 오류가 발생했습니다. 나중에 다시 시도해주세요.');
				}

				console.error(xhr);
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
			url: '/api/resumes/draft',
			method: 'POST',
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
			error: function () {
				alert('서버 오류가 발생했습니다. 나중에 다시 시도해주세요.');
				console.error(xhr);
			}
		});
	});

});