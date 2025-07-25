// applicant/recruit/view.html

$(document).ready(function () {

	const jobPostId = getIdFromUrl();
	let userInfo = null;

	const recruitAjax = $.ajax({
		url: `/api/recruit/${jobPostId}`,
		method: 'GET'
	});

	const techStackAjax = $.ajax({
		url: '/api/tech-stacks',
		method: 'GET'
	});

	$.when(recruitAjax, techStackAjax).done(function(recruitResponse, techStackResponse) {

		const data = recruitResponse[0];
		const techStacks = techStackResponse[0];

		const jobPost = data.jobPost;
		const employer = data.employer;
		const isApplied = data.isApplied;
		const isBookmarked = data.isBookmarked;
		userInfo = data.userInfo;

		const skillMap = {};
		if (techStacks) {
			techStacks.forEach(stack => {
				skillMap[stack.id] = stack.name;
			});
		}

		let type = translateExperienceType(jobPost.experienceType);
		let years = jobPost.experienceYears;

		function fallbackText(value, fallback = '-') {
			return value ? value : fallback;
		}

		// 화면 표시 로직
		$('#title').text(fallbackText(jobPost.title));
		$('#description').html(jobPost.description ? jobPost.description : '상세 정보가 없습니다');
		$('#employmentType').text(fallbackText(translateEmploymentType(jobPost.employmentType)));
		$('#jobCategory').text(fallbackText(jobPost.jobCategory));
		$('#salary').text(fallbackText(jobPost.salary));
		$('#location').text(fallbackText(jobPost.location));
		$('#deadline').text(fallbackText(jobPost.deadline));
		$('#createAt').text(fallbackText(jobPost.createAt));
		$('#viewCount').text(fallbackText(jobPost.viewCount));
		$('#bookmarkCount').text(fallbackText(jobPost.bookmarkCount));

		let experienceText = type;
		if (type !== "신입" && years && years > 0) {
			experienceText += ` (${years}년)`;
		}
		$('#experience').text(fallbackText(experienceText));

		$('#workplaceAddress').text(fallbackText(jobPost.workplaceAddress));

		$('#headerCompanyName').text(fallbackText(employer.companyName));
		$('#companyName').text(fallbackText(employer.companyName));
		$('#address').text(fallbackText(employer.address));
		$('#phoneNumber').text(fallbackText(employer.phoneNumber));
		$('#homepageUrl').text(fallbackText(employer.homepageUrl));
		$('#industry').text(fallbackText(employer.industry));
		$('#companySize').text(fallbackText(employer.companySize));
		$('#employerEmail').text(fallbackText(employer.email));

		$('#reportTargetId').val(jobPost.id);
		$('#reportUserId').val(jobPost.userId);

		if (jobPost.requiredSkills) {
			const skillNames = jobPost.requiredSkills.split(',')
				.map(id => skillMap[id.trim()] || id.trim())
				.join(', ');
			$('#requiredSkills').text(skillNames);
		} else {
			$('#requiredSkills').text('-');
		}

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

	}).fail(function (xhr) {
		if (xhr.status === 403) {
			location.href = '/error/access-denied';
		} else if (xhr.status === 404) {
			location.href = '/error/not-found';
		} else {
			alert('공고 정보를 불러오기에 실패했습니다. 나중에 다시 시도해주세요.');
			console.error(xhr);
		}
	});

	$('#applyBtn').on('click', function () {

		// 사용자 권환 확인 후 안내
		if (!userInfo || !userInfo.isLoggedIn) {
			if (confirm('로그인이 필요한 기능입니다. 로그인 페이지로 이동하시겠습니까?')) {
				window.open('/users/login', '_blank');
			}
			return;
		}
		if (userInfo.role !== 'APPLICANT') {
			alert('개인 회원만 사용할 수 있는 기능입니다.');
			return;
		}

		location.href = `/applicant/applications/apply/${jobPostId}`;
	});

	$('#scrapBtn').on('click', function () {

		// 사용자 권환 확인 후 안내
		if (!userInfo || !userInfo.isLoggedIn) {
			if (confirm('로그인이 필요한 기능입니다. 로그인 페이지로 이동하시겠습니까?')) {
				window.open('/users/login', '_blank');
			}
			return;
		}
		if (userInfo.role !== 'APPLICANT') {
			alert('개인 회원만 사용할 수 있는 기능입니다.');
			return;
		}

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
					if (confirm('로그인이 필요한 기능입니다. 로그인 페이지로 이동하시겠습니까?')) {
						window.open('/users/login', '_blank');
					}
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

function initWorkplaceMap() {
	const address = document.getElementById('workplaceAddress')?.textContent?.trim();
	if (!window.kakao || !kakao.maps || !address) return;

	const mapContainer = document.getElementById('map');
	if (!mapContainer) return;

	const mapOption = {
		center: new kakao.maps.LatLng(37.5665, 126.9780), // 서울 기본 위치
		level: 3
	};

	const map = new kakao.maps.Map(mapContainer, mapOption);
	const geocoder = new kakao.maps.services.Geocoder();

	geocoder.addressSearch(address, function (result, status) {
		if (status === kakao.maps.services.Status.OK) {
			const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
			new kakao.maps.Marker({ map, position: coords });
			map.setCenter(coords);
		} else {
			console.warn("카카오 주소 검색 실패:", status);
		}
	});
}
