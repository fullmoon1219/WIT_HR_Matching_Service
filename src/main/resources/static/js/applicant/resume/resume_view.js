// applicant/resume/view.html

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

	const techStackAjax = $.ajax({
		url: '/api/tech-stacks',
		method: 'GET'
	});

	$.when(profileAjax, resumeAjax, techStackAjax).done(function (profileResponse, resumeResponse, techStackResponse) {

		const profileData = profileResponse[0];
		const resumeData = resumeResponse[0];

		const userInfo = profileData.userInfo;
		const profile = profileData.profile;

		const techStacks = techStackResponse[0];

		const skillMap = {};
		if (techStacks) {
			techStacks.forEach(stack => {
				skillMap[stack.id] = stack.name;
			});
		}

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

		$('#title').text(resumeData.title);
		$('#education').text(resumeData.education);
		$('#experience').text(resumeData.experience);
		$('#preferredLocation').text(resumeData.preferredLocation);
		$('#salaryExpectation').text(resumeData.salaryExpectation);
		$('#coreCompetency').text(resumeData.coreCompetency);
		$('#desiredPosition').text(resumeData.desiredPosition);
		$('#motivation').text(resumeData.motivation);
		$('#createAt').text(resumeData.createAt);

		if (resumeData.skills) {
			const skillNames = resumeData.skills.split(',')
				.map(id => skillMap[id.trim()] || id.trim())
				.join(', ');
			$('#skills').text(skillNames);
		} else {
			$('#skills').text('-');
		}

		if (resumeData.updatedAt && resumeData.updatedAt !== resumeData.createAt) {
			$('#updatedAt').text(resumeData.updatedAt);
			$('#updatedAtRow').show();
		}

		if (resumeData.isPublic === true) {
			$('#editBtn').hide();
			$('#deleteBtn').hide();
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

	$('#editBtn').on('click', function () {
		location.href = `/applicant/resume/edit/${resumeId}`;
	});

	$('#deleteBtn').on('click', function () {

		if (confirm('정말 삭제하시겠습니까?')) {

			$.ajax({
				url: `/api/resumes/${resumeId}`,
				method: 'DELETE',
				success: function () {

					sessionStorage.setItem('toastMessage', '이력서가 삭제되었습니다.');
					location.href = '/applicant/resume/list';

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
	})
});
