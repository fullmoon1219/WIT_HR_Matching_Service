// applicant/resume/register.html

$(document).ready(function () {

	$.ajax({
		url:'/api/resumes',
		method:'GET',
		success: function (data) {
			const userInfo = data.userInfo;
			const profile = data.profile;

			$('#name').text(userInfo.name);
			$('#email').text(userInfo.email);

			if (profile) {
				$('#age').text(profile.age || '-');
				$('#gender').text(translateGender(profile.gender) || '-');
				$('#phoneNumber').text(profile.phoneNumber || '-');
				$('#address').text(profile.address || '-');
				$('#portfolioUrl').text(profile.portfolioUrl || '-');
				$('#selfIntro').text(profile.selfIntro || '-');
				$('#experienceYears').text(profile.experienceYears || '0');
				$('#jobType').text(translateEmploymentType(profile.jobType) || '-');

			} else {
				$('#age, #gender, #phoneNumber, #address, #portfolioUrl, #selfIntro, #jobType').text('-');
				$('#experienceYears').text('0');
			}
		},
		error: function (xhr) {
			console.error('데이터 로딩 중 오류 발생: ', xhr);

			if (xhr.status === 403) {
				location.href = '/error/access-denied';
			} else if (xhr.status === 404) {
				location.href = '/error/not-found';
			} else {
				alert('데이터를 불러오기에 실패했습니다. 나중에 다시 시도해주세요.')
			}
		}
	});

	$('#stack-selection-area').on('click', '.select-btn', function () {
		// 클릭한 버튼의 'active' 클래스를 토글
		$(this).toggleClass('active');

		const selectedValues = $('#stack-selection-area .select-btn.active').map(function() {
			return $(this).data('value');
		}).get();
		const valueString = selectedValues.join(',');

		$('#skills').val(valueString);
	});

	$('#location-selection-area').on('click', '.select-btn', function () {
		const clickedButton = $(this);

		if (clickedButton.hasClass('active')) {
			clickedButton.removeClass('active');
			$('#preferredLocation').val('');

		} else {
			clickedButton.siblings('.select-btn').removeClass('active');
			clickedButton.addClass('active');

			$('#preferredLocation').val(clickedButton.data('value'));
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
			url: '/api/resumes',
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(resume),
			success: function (response) {
				if (response.success) {

					sessionStorage.setItem('toastMessage', '이력서 등록이 완료되었습니다.');
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
			$('#error-title').text('제목은(는) 필수 입력 항목입니다.');

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

					sessionStorage.setItem('toastMessage', '임시저장이 완료되었습니다.');
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