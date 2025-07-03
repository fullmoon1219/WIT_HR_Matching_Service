// applicant/recruit/view.html

$(document).ready(function () {

	const jobPostId = getIdFromUrl();

	$.ajax({
		url: `/api/recruit/${jobPostId}`,
		method: 'GET',
		success: function (data) {

			const jobPost = data.jobPost;
			const employer = data.employer;
			const isApplied = data.isApplied;
			const isBookmarked = data.isBookmarked;

			$('#title').text(jobPost.title);
			$('#description').text(jobPost.description);
			$('#requiredSkills').text(jobPost.requiredSkills);
			$('#employmentType').text(translateEmploymentType(jobPost.employmentType));
			$('#jobCategory').text(jobPost.jobCategory);
			$('#salary').text(jobPost.salary);
			$('#location').text(jobPost.location);
			$('#deadline').text(jobPost.deadline);
			$('#createAt').text(jobPost.createAt);
			$('#viewCount').text(jobPost.viewCount);
			$('#bookmarkCount').text(jobPost.bookmarkCount);

			$('#companyName').text(employer.companyName);
			$('#address').text(employer.address);
			$('#phoneNumber').text(employer.phoneNumber);
			$('#homepageUrl').text(employer.homepageUrl);
			$('#industry').text(employer.industry);
			$('#companySize').text(employer.companySize);

			if (isApplied === true) {

				$('#applyBtn')
					.prop('disabled', true)     // 버튼 비활성
					.text('지원 완료')            		// 버튼 내용 수정
					.addClass('applied-style');		// 지원 완료 상태에서 스타일 적용을 위한 클래스
			}

			if (isBookmarked === true) {

				// 스크랩 부분 버튼에서 이모지는 디자인 시 바꿔야함(현재 임시)
				$('#scrapBtn')
					.addClass('active')
					.html('&#9733; 스크랩 완료');
			}

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

	$('#applyBtn').on('click', function () {
		location.href = `/applicant/applications/apply/${jobPostId}`;
	});

	$('#scrapBtn').on('click', function () {

		const button = $(this);
		let method = '';
		let action = '';
		let url = '';

		if(button.hasClass('active')) {
			url = `/api/bookmarks/${jobPostId}`;
			method = 'DELETE';
			action = '해제';
		} else {
			url = `/api/bookmarks`;
			method = 'POST';
			action = '등록';
		}

		$.ajax({
			url: url,
			method: method,
			contentType: method === 'POST' ? 'application/json' : undefined,
			data: method === 'POST' ? JSON.stringify({ jobPostId: jobPostId }) : null,
			success: function (data) {

				button.toggleClass('active');

				if (button.hasClass('active')) {
					button.html('&#9733; 스크랩 완료');
				} else {
					button.html('&#9734; 스크랩');
				}

				Toastify({
					text: '스크랩 ' + action + '되었습니다.',
					duration: 2000,
					gravity: "top",
					position: "center",
					stopOnFocus: false,
					style: {
						background: "#6495ED",
						color: "#ffffff"
					}
				}).showToast();

			},
			error: function (xhr) {
				if (xhr.status === 403) {
					location.href = '/error/access-denied';
				} else if (xhr.status === 404) {
					location.href = '/error/not-found';
				} else {
					alert('스크랩 ' + action + '에 실패했습니다. 나중에 다시 시도해주세요.');
					console.error(xhr);
				}
			}
		});
	});
});
