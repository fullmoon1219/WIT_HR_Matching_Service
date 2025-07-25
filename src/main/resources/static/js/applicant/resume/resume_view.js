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
		$("#resumeId").attr("data-resume-id", resumeData.id);

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

	$(document).on('click', '.ai-info-button', function () {
		const resumeId = $(this).data('resume-id');

		// 상태 출력 및 버튼 숨김
		$(".ai-info-text").text("이력서를 분석하는 중입니다...").show();
		$(this).hide();

		$.ajax({
			url: "/api/ai/resumes/analyze",
			method: "POST",
			contentType: "application/json",
			data: JSON.stringify({ resumeId: resumeId }),
			success: function (response) {
				console.log("AI 분석 결과:", response);

				// 각 항목 추출
				const score = response.score ?? 'N/A';
				const strengths = response.strengths || '강점 없음';
				const weaknesses = Array.isArray(response.weaknesses)
					? response.weaknesses.map((w, i) => `<li>${i + 1}. ${w}</li>`).join('')
					: '<li>보완점 없음</li>';
				const comment = response.comment || '분석 코멘트가 없습니다.';

				// HTML 구성
				const resultHtml = `
                <div class="ai-review-result">
                    <p><strong>💯 종합 점수:</strong> ${score}점</p>
                    <p><strong>💪 강점:</strong> ${strengths}</p>
                    <p><strong>🛠 보완점:</strong></p>
                    <ul>${weaknesses}</ul>
                    <p><strong>📝 총평:</strong> ${comment}</p>
                </div>
            `;

				// 결과 출력
				$("#ai-review").html(resultHtml);
				$(".ai-info-text").hide();
			},
			error: function (xhr) {
				$("#ai-review").text("AI 분석에 실패했습니다.");
				$(".ai-info-text").hide();
			}
		});
	});



});
