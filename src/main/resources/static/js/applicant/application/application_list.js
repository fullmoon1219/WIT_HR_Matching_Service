// applicant/application/list.html

let currentCriteria = {
	page: 1,
	recordPerPage: 10,
	status: '',
	sortOrder: 'latest',
	keyword: ''
};

$(document).ready(function() {

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

	// 페이지 진입시 전체 지원 목록 가져오기(페이징 적용)
	loadApplicationList();

	$('#filter-all').on('click', function() {
		loadApplicationList({
			status: '',
			keyword: '',
			page: 1,
			sortOrder: 'latest'
		});
	});

	$('#filter-in-progress').on('click', function() {
		let newCriteria = {
			status: 'APPLIED'
		};
		loadApplicationList(newCriteria);
	});

	$('#filter-final').on('click', function() {
		let newCriteria = {
			status: ['ACCEPTED', 'REJECTED']
		};
		loadApplicationList(newCriteria);
	});

	$('#pagination').on('click', 'a', function(e) {
		e.preventDefault();

		const page = $(this).data('page');

		// 해당 페이지의 목록을 불러오도록 loadApplicationList 함수를 호출.
		loadApplicationList({ page: page });
	});

	$('.recommend-header').on('click', '.more-btn', function() {
		$('.sort-options').toggle(); // 정렬 옵션 div를 보여주거나 숨김
	});

	// 정렬 옵션 중 하나를 클릭했을 때의 이벤트
	$('.recommend-header').on('click', '.sort-options a', function(e) {
		e.preventDefault(); // a 태그의 기본 동작(페이지 이동) 방지

		const selectedSortOrder = $(this).data('sort');

		// 정렬순서에 따라 다시 로드
		loadApplicationList({
			sortOrder: selectedSortOrder,
			page: 1
		});

		$('.sort-options').hide(); // 옵션을 선택했으니 다시 숨김
	});

	// 모달창으로 지원 내용 확인
	$('#applicationListBody').on('click', '.application-title-text', function() {

		const applicationId = $(this).closest('.application-row').data('application-id');
		const title = $(this).text();

		$('#application-modal').dialog('option', 'title', title);
		$('#application-modal').html('<p>로딩 중...</p>').dialog('open');

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
				modalContentHtml += '<div id="resumeInfo" class="modal-link-section" data-resume-id="' + resume.resumeId + '">';;
				modalContentHtml += '<h4>[내가 제출한 정보]</h4>';
				modalContentHtml +=   '<p><strong>제출 이력서:</strong> ' + resume.resumeTitle + '</p>';
				modalContentHtml +=   '<p><strong>지원 일시:</strong> ' + resume.appliedAt + '</p>';
				modalContentHtml +=   '<p><em>(클릭 시 제출한 이력서를 확인합니다)</em></p>';
				modalContentHtml += '</div>';

				modalContentHtml += '<hr />';

				// 본문 2: 공고 정보 (공고 삭제 여부에 따라 내용을 다르게 표시)
				if (resume.jobPostDeletedAt) {
					modalContentHtml += '<div id="jobPostInfo">';
					modalContentHtml += 	'<h4>[지원한 기업 정보]</h4>';
					modalContentHtml += 	'<p><strong>지원한 기업: </strong>' + resume.employerCompanyName + '</p>';
					modalContentHtml += 	'<p>이 공고는 마감되었거나, 더 이상 확인할 수 없습니다.</p>';
					modalContentHtml += '</div>';
				} else {
					modalContentHtml += '<div id="jobPostInfo" class="modal-link-section" data-job-post-id="' + resume.jobPostId + '">';
					modalContentHtml += 	'<h4>[지원한 기업 정보]</h4>';
					modalContentHtml += 	'<p><strong>지원한 기업: </strong>' + resume.employerCompanyName + '</p>';
					modalContentHtml += 	'<p><strong>근무 지역: </strong>' + resume.jobPostLocation + '</p>';
					modalContentHtml += 	'<p><strong>고용 형태: </strong>' + resume.jobPostJobCategory + '</p>';
					modalContentHtml += 	'<p><strong>마감일: </strong>' + resume.jobPostDeadline + '</p>';
					modalContentHtml += 	'<p><em>(클릭 시 상세 공고를 확인합니다)</em></p>';
					modalContentHtml += '</div>';
				}

				modalContentHtml += '<hr />';

				// 본문 3: 타임라인
				modalContentHtml += '<div>';
				modalContentHtml += '<h3>[전형 진행 과정]</h3>';

				// 지원 완료
				modalContentHtml += '<div>';
				modalContentHtml += 	'<p><strong>😎 지원 완료:</strong> ' + resume.appliedAt + '</p>';
				modalContentHtml += 	'<p>회원님의 이력서가 기업에 안전하게 전달되었어요.</p>';
				modalContentHtml += '</div>';

				// 기업 열람
				if (resume.viewedAt) {
					modalContentHtml += '<div>';
					modalContentHtml += 	'<p><strong>🖥️ 기업 열람:</strong> ' + resume.viewedAt + '</p>';
					modalContentHtml += 	'<p>인사담당자가 회원님의 이력서를 확인했어요. 좋은 소식을 기다려보세요!</p>';
					modalContentHtml += '</div>';
				}

				// 최종 결과
				if (resume.status === 'ACCEPTED') {
					modalContentHtml += '<div>';
					modalContentHtml += 	'<p><strong>🎉 최종 합격:</strong> ' + resume.updatedAt + '</p>';
					modalContentHtml += 	'<p>축하합니다! 서류 전형에 최종 합격하셨습니다.</p>';
					modalContentHtml += '</div>';
				} else if (resume.status === 'REJECTED') {
					modalContentHtml += '<div>';
					modalContentHtml += 	'<p><strong>✉️ 최종 결과:</strong> ' + resume.updatedAt + '</p>';
					modalContentHtml += 	'<p>안타깝게도 이번 채용과는 인연이 닿지 않았습니다. 더 좋은 기회가 회원님을 기다리고 있을 거예요.</p>';
					modalContentHtml += '</div>';
				}
				modalContentHtml += '</div>';

				$('#application-modal').html(modalContentHtml);
			},
			error: function (xhr) {
				if (xhr.status === 403) {
					// 권한 없는 경우
					location.href = '/error/access-denied';
				} else if (xhr.status === 404) {
					// 없는 이력서
					location.href = '/error/not-found';
				} else {
					alert('지원 내역 불러오기에 실패했습니다. 나중에 다시 시도해주세요.');
					console.error(xhr);
				}
			}
		});
	});

	// 지원 상세보기 내용에서 이력서 확인
	$('#application-modal').on('click', '#resumeInfo', function() {
		const resumeId = $(this).data('resume-id');
		if (resumeId) {
			window.open(`/applicant/resume/view/${resumeId}`, '_blank');
		}
	});

	// 지원 상세보기 내용에서 채용 공고 확인
	$('#application-modal').on('click', '#jobPostInfo', function() {
		const jobPostId = $(this).data('job-post-id');
		if (jobPostId) {
			window.open(`/applicant/recruit/view/${jobPostId}`, '_blank');
		}
	});
});

function loadApplicationList(newCriteria = {}) {

	if (newCriteria.page === undefined) {
		newCriteria.page = 1;
	}
	// currentCriteria 객체에 newCriteria객체 덮어쓰기 (조건 추가시)
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

				applications.forEach(function(application, index) {

					const rowNum = index + 1 + ((pagingInfo.currentPage - 1) * pagingInfo.recordPerPage);

					const row = makeRow(application, rowNum);
					tbody.append(row);
				});
			}

			// 페이징 UI 그리는 함수 호출
			// renderPagination(pagingInfo);

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
				alert('지원 목록 불러오기에 실패했습니다. 나중에 다시 시도해주세요.')
				console.error("지원 목록 로딩 중 오류 발생:", xhr);
			}
		}
	});
}

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

$('#custom-pagination').on('click', 'button', function() {
	const page = $(this).data('page');
	loadApplicationList({ page: page });

	// active 스타일 갱신
	$('#custom-pagination button').removeClass('active');
	$(this).addClass('active');
});
