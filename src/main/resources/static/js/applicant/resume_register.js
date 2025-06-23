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
			salaryExpectation: $('input[name="salaryExpectation"]').val(),
			isPublic: $('input[name="isPublic"]:checked').val() === 'true',  // boolean 처리
			desiredPosition: $('input[name="desiredPosition"]').val(),
			motivation: $('input[name="motivation"]').val(),
			coreCompetency: $('input[name="coreCompetency"]').val(),
			completed: true // 정식 등록
		};

		$.ajax({
			url: 'api/resume',
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(resume),
			success: function (response) {
				if (response.success) {
					alert('등록이 완료되었습니다.');
					location.href = '/applicant/resume/list';
				} else {
					location.href = '/error/db-access-denied';
				}
			},
			error: function (xhr) {

				if (xhr.responseJSON && xhr.responseJSON.errors) {
					const errors = xhr.responseJSON.errors;

					console.log(errors);

					alert(errors[0].defaultMessage);
				} else {
					alert('서버 오류가 발생했습니다. 나중에 다시 시도해주세요.');
				}

				console.error(xhr);
			}
		});
	});

	$('#btnDraft').click(function () {

		const title = $.trim($('input[name="title"]').val());

		if (title === '') {
			alert('제목을 입력해주세요.');
			$('input[name="title"]').focus();
			return;
		}

		const resume = {
			title: title,
			education: $('input[name="education"]').val(),
			experience: $('input[name="experience"]').val(),
			skills: $('input[name="skills"]').val(),
			preferredLocation: $('input[name="preferredLocation"]').val(),
			salaryExpectation: $('input[name="salaryExpectation"]').val(),
			isPublic: $('input[name="isPublic"]:checked').val() === 'true',  // boolean 처리
			desiredPosition: $('input[name="desiredPosition"]').val(),
			motivation: $('input[name="motivation"]').val(),
			coreCompetency: $('input[name="coreCompetency"]').val(),
			completed: false // 임시 저장
		};

		$.ajax({
			url: '/api/resume',
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(resume),
			success: function (response) {
				if (response.success) {
					alert('임시 저장이 완료되었습니다.');
					location.href = '/applicant/resume/list';
				} else {
					alert('임시 저장 실패');
				}
			},
			error: function () {
				alert('서버 오류로 저장에 실패했습니다.');
			}
		});
	});

});