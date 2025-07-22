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
				let modalContentHtml = '';

				// 헤더 부분
				modalContentHtml += '<h3>' + resume.employerCompanyName + '</h3>';
				modalContentHtml += '<h2>' + resume.jobPostTitle + '</h2>';
				modalContentHtml += '<h4>지원 상태: ' + translateApplicationStatus(resume.status) + '</h4>';
				modalContentHtml += '<hr />';

				// 본문 1: 지원 정보
				if (resume.resumeDeletedAt) {
					modalContentHtml += '<div class="resume-info-deleted">';
					modalContentHtml += '<h4>[내가 제출한 이력서]</h4>';
					modalContentHtml += '<p><strong>제출 이력서:</strong> ' + resume.resumeTitle + '</p>';
					modalContentHtml += '<p><strong>지원 일시:</strong> ' + resume.appliedAt + '</p>';
					modalContentHtml += '<p><em>삭제된 이력서입니다.</em></p>';
					modalContentHtml += '</div>';
					modalContentHtml += '<hr />';
				} else {
					modalContentHtml += '<div id="resumeInfo" class="modal-link-section" data-resume-id="' + resume.resumeId + '">';
					modalContentHtml += '<h4>[내가 제출한 이력서]</h4>';
					modalContentHtml += '<p><strong>제출 이력서:</strong> ' + resume.resumeTitle + '</p>';
					modalContentHtml += '<p><strong>지원 일시:</strong> ' + resume.appliedAt + '</p>';
					modalContentHtml += '<p><em>(클릭 시 제출한 이력서를 확인합니다)</em></p>';
					modalContentHtml += '</div>';
					modalContentHtml += '<hr />';
				}

				// 본문 2: 공고 정보
				if (resume.jobPostDeletedAt) {
					modalContentHtml += '<div class="job-info-deleted">';
					modalContentHtml += '<h4>[지원한 기업 정보]</h4>';
					modalContentHtml += '<p><strong>지원한 기업: </strong>' + resume.employerCompanyName + '</p>';
					modalContentHtml += '<p>이 공고는 마감되었거나, 더 이상 확인할 수 없습니다.</p>';
					modalContentHtml += '</div>';
				} else {
					modalContentHtml += '<div id="jobPostInfo" class="modal-link-section" data-job-post-id="' + resume.jobPostId + '">';
					modalContentHtml += '<h4>[지원한 기업 정보]</h4>';
					modalContentHtml += '<p><strong>지원한 기업: </strong>' + resume.employerCompanyName + '</p>';
					modalContentHtml += '<p><strong>근무 지역: </strong>' + resume.jobPostLocation + '</p>';
					modalContentHtml += '<p><strong>고용 형태: </strong>' + resume.jobPostJobCategory + '</p>';
					modalContentHtml += '<p><strong>마감일: </strong>' + resume.jobPostDeadline + '</p>';
					modalContentHtml += '<p><em>(클릭 시 상세 공고를 확인합니다)</em></p>';
					modalContentHtml += '</div>';
				}

				modalContentHtml += '<hr />';

				// 본문 3: 전형 진행 과정
				modalContentHtml += '<div>';
				modalContentHtml += '<h3>[전형 진행 과정]</h3>';

				// 지원 완료
				modalContentHtml += '<div>';
				modalContentHtml += '<p><strong>😎 지원 완료:</strong> ' + resume.appliedAt + '</p>';
				modalContentHtml += '<p>회원님의 이력서가 기업에 안전하게 전달되었어요.</p>';
				modalContentHtml += '</div>';

				// 기업 열람
				if (resume.viewedAt) {
					modalContentHtml += '<div>';
					modalContentHtml += '<p><strong>🖥️ 기업 열람:</strong> ' + resume.viewedAt + '</p>';
					modalContentHtml += '<p>인사담당자가 회원님의 이력서를 확인했어요. 좋은 소식을 기다려보세요!</p>';
					modalContentHtml += '</div>';
				}

				// 최종 결과
				if (resume.status === 'ACCEPTED') {
					modalContentHtml += '<div>';
					modalContentHtml += '<p><strong>🎉 최종 합격:</strong> ' + resume.updatedAt + '</p>';
					modalContentHtml += '<p>축하합니다! 서류 전형에 최종 합격하셨습니다.</p>';
					modalContentHtml += '</div>';
				} else if (resume.status === 'REJECTED') {
					modalContentHtml += '<div>';
					modalContentHtml += '<p><strong>✉️ 최종 결과:</strong> ' + resume.updatedAt + '</p>';
					modalContentHtml += '<p>안타깝게도 이번 채용과는 인연이 닿지 않았습니다. 더 좋은 기회가 회원님을 기다리고 있을 거예요.</p>';
					modalContentHtml += '</div>';
				}

				modalContentHtml += '</div>';

				$('#application-modal').html(modalContentHtml);
			}, error: function (xhr) {

				if (xhr.status === 403) {
					location.href = '/error/access-denied';
				} else if (xhr.status === 404) {
					location.href = '/error/not-found';
				} else {
					alert('데이터를 불러오기에 실패했습니다. 나중에 다시 시도해주세요.');
					console.error(xhr);
				}
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
		if (jobPostId) window.open(`/recruit/view/${jobPostId}`, '_blank');
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
		},
		error: function (xhr) {

			if (xhr.status === 403) {
				location.href = '/error/access-denied';
			} else if (xhr.status === 404) {
				location.href = '/error/not-found';
			} else {
				alert('지원내역을 불러오기에 실패했습니다. 나중에 다시 시도해주세요.');
				console.error(xhr);
			}
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
