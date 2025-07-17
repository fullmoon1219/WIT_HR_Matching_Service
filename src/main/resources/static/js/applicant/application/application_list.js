// applicant/application/list.html

let currentCriteria = {
	page: 1,
	recordPerPage: 10,
	status: '',
	sortOrder: 'latest',
	keyword: ''
};

$(document).ready(function() {

	// 모달 초기화
	$("#application-modal").dialog({
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

	// 최초 목록 로드
	loadApplicationList();

	// 필터: 전체
	$('#filter-all').on('click', function() {
		loadApplicationList({
			status: '',
			keyword: '',
			page: 1,
			sortOrder: 'latest'
		});
	});

	// 필터: 진행중
	$('#filter-in-progress').on('click', function() {
		loadApplicationList({ status: 'APPLIED', page: 1 });
	});

	// 필터: 최종발표
	$('#filter-final').on('click', function() {
		loadApplicationList({ status: ['ACCEPTED', 'REJECTED'], page: 1 });
	});

	// 페이징 버튼 클릭 이벤트
	$('#custom-pagination').on('click', 'button', function() {
		const page = $(this).data('page');
		loadApplicationList({ page: page });
	});

	// 정렬 옵션 표시 토글
	$('.recommend-header').on('click', '.more-btn', function() {
		$('.sort-options').toggle();
	});

	// 정렬 옵션 클릭 이벤트
	$('.recommend-header').on('click', '.sort-options a', function(e) {
		e.preventDefault();
		const selectedSortOrder = $(this).data('sort');
		loadApplicationList({ sortOrder: selectedSortOrder, page: 1 });
		$('.sort-options').hide();
	});

	// 공고 제목 클릭 -> 모달로 상세보기
	$('#applicationListBody').on('click', '.application-title-text', function() {
		const applicationId = $(this).closest('.application-row').data('application-id');
		const title = $(this).text();

		$('#application-modal').dialog('option', 'title', title);
		$('#application-modal').html('<p>로딩 중...</p>').dialog('open');

		// 상세 API 요청
		$.ajax({
			url: `/api/applications/${applicationId}`,
			method: 'GET',
			success: function (resume) {
				// 모달 내용 작성 (생략: 기존 코드 그대로)
				// ...
			}
		});
	});

	// 모달 내: 이력서 보기 클릭
	$('#application-modal').on('click', '#resumeInfo', function() {
		const resumeId = $(this).data('resume-id');
		if (resumeId) window.open(`/applicant/resume/view/${resumeId}`, '_blank');
	});

	// 모달 내: 공고 보기 클릭
	$('#application-modal').on('click', '#jobPostInfo', function() {
		const jobPostId = $(this).data('job-post-id');
		if (jobPostId) window.open(`/applicant/recruit/view/${jobPostId}`, '_blank');
	});
});

// 지원 내역 목록 요청 및 렌더링
function loadApplicationList(newCriteria = {}) {
	if (newCriteria.page === undefined) newCriteria.page = 1;
	Object.assign(currentCriteria, newCriteria);

	$.ajax({
		url: "/api/applications",
		type: "GET",
		data: currentCriteria,
		success: function (response) {
			const tbody = $('#applicationListBody');
			const applications = response.content;
			const pagingInfo = response.pagingInfo;

			if (!applications || applications.length === 0) {
				$(tbody).hide();
				$('.no-data').show();
			} else {
				$(tbody).show();
				$('.no-data').hide();
				tbody.empty();

				// 목록 렌더링
				applications.forEach(function(application, index) {
					const rowNum = index + 1 + ((pagingInfo.currentPage - 1) * pagingInfo.recordPerPage);
					const row = makeRow(application, rowNum);
					tbody.append(row);
				});
			}

			// ⭐ 페이징 UI 그리기
			renderPagination(pagingInfo);

			// 상태별 카운트 표시
			if (!currentCriteria.status || currentCriteria.status.length === 0) {
				$('#count-all').text(pagingInfo.totalRecord);
			}
			$('#count-in-progress').text(response.countInProgress);
			$('#count-final').text(response.countFinal);
		}
	});
}

// 개별 row 생성
function makeRow(application, rowNumber) {
	let statusText = '';

	if (application.status === 'ACCEPTED' || application.status === 'REJECTED') {
		statusText = translateApplicationStatus(application.status);
	} else if (application.viewedAt != null) {
		statusText = '기업 열람';
	} else {
		statusText = translateApplicationStatus(application.status);
	}

	return `
         <tr class="application-row" data-application-id="${application.id}">
            <td>${rowNumber}</td>
            <td><span class="application-title-text">${application.jobPostTitle}</span></td>
            <td>${application.employerCompanyName}</td>
            <td>${application.appliedAt}</td>
            <td>${application.jobPostDeadline}</td>
            <td>${statusText}</td>
        </tr>
    `;
}

// ⭐ 페이징 UI 그리기 함수
function renderPagination(pagingInfo) {
	const pagination = $('#custom-pagination');
	pagination.empty();

	const currentPage = pagingInfo.currentPage || pagingInfo.page;
	const totalPage = pagingInfo.totalPage;

	if (totalPage <= 1) return;

	// 각 페이지 번호 버튼 렌더링
	for (let i = 1; i <= totalPage; i++) {
		const activeClass = (i === currentPage) ? 'active' : '';
		pagination.append(`<button class="page-btn ${activeClass}" data-page="${i}">${i}</button>`);
	}
}
