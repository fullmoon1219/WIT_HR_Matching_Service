// applicant/resume/edit.html

$(document).ready(function () {

	const resumeId = getIdFromUrl();

	const profileAjax = $.ajax({
		url: '/api/resumes',
		method: 'GET'
	});

	const resumeAjax = $.ajax({
		url: `/api/resumes/${resumeId}`,
		method: 'GET'
	});

	$.when(profileAjax, resumeAjax).done(function (profileResponse, resumeResponse) {

		const profileData = profileResponse[0];
		const resumeData = resumeResponse[0];

		const userInfo = profileData.userInfo;
		const profile = profileData.profile;

		if (userInfo.profileImage) {
			$('#profile-img-view').attr('src', '/uploads/users/profile/' + userInfo.profileImage);
		} else {
			$('#profile-img-view').attr('src', '/images/users/user_big_profile.png');
		}

		$('#name').text(userInfo.name);
		$('#email').text(userInfo.email);

		if (profile) {
			$('#age').text(profile.age || '-');
			$('#gender').text(translateGender(profile.gender) || '-');
			$('#phoneNumber').text(profile.phoneNumber || '-');
			$('#address').text(profile.address || '-');
			$('#portfolioUrl').text(profile.portfolioUrl || '-').attr('href', profile.portfolioUrl || '#');
			$('#selfIntro').text(profile.selfIntro || '-');
			$('#experienceYears').text(profile.experienceYears || '0');
			$('#jobType').text(translateEmploymentType(profile.jobType) || '-');
		} else {
			$('#age, #gender, #phoneNumber, #address, #portfolioUrl, #selfIntro, #jobType').text('-');
			$('#experienceYears').text('0');
		}

		$('#title').val(resumeData.title);
		$('#education').val(resumeData.education);
		$('#experience').val(resumeData.experience);
		$('#skills').val(resumeData.skills);
		$('#preferredLocation').val(resumeData.preferredLocation);
		$('#salaryExpectation').val(resumeData.salaryExpectation);
		$('#coreCompetency').val(resumeData.coreCompetency);
		$('#desiredPosition').val(resumeData.desiredPosition);
		$('#motivation').val(resumeData.motivation);

		if (resumeData.skills) {
			const skillIds = resumeData.skills.split(',');
			skillIds.forEach(id => {
				$(`#stack-selection-area .select-btn[data-value="${id}"]`).addClass('active');
			});
		}

		if (resumeData.preferredLocation) {
			const location = resumeData.preferredLocation;
			$(`#location-selection-area .select-btn[data-value="${location}"]`).addClass('active');
		}

		$('body').show();

	}).fail(function (xhr) {

		console.error("데이터 로딩 중 오류 발생", xhr);

		if (xhr.status === 403) {
			location.href = '/error/access-denied';
		} else if (xhr.status === 404) {
			location.href = '/error/not-found';
		} else {
			alert('데이터를 불러오는 데 실패했습니다. 나중에 다시 시도해주세요.');
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

					sessionStorage.setItem('toastMessage', '수정 사항이 저장되었습니다.');
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

					sessionStorage.setItem('toastMessage', '임시저장이 완료되었습니다.');
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
});
