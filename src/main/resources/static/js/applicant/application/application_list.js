$(document).ready(function() {

	$("#application-modal").dialog({
		autoOpen: false,
		modal: true,
		width: 700,
		height: 'auto',
		maxHeight: $(window).height() * 0.8,
		buttons: {
			"자세히 보기": function() {
				const resumeId = $('#resume-modal').data('current-resume-id');

				if (resumeId) {
					const url = `/applicant/resume/view/${resumeId}`;
					window.open(url, '_blank');
				}
			},
			"닫기": function() {
				$(this).dialog("close");
			}
		}
	});

	loadApplicationList();

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
				modalContentHtml +=   '<p><em>(클릭 시 제출한 이력서를 새 창에서 확인합니다)</em></p>';
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
					modalContentHtml += 	'<p><em>(클릭 시 상세 공고를 새 창에서 확인합니다)</em></p>';
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

function loadApplicationList() {
	$.ajax({
		url: "/api/applications",
		type: "GET",
		success: function (applications) {

			const tbody = $('#applicationListBody');
			tbody.empty();

			if (!applications || applications.length === 0) {
				const emptyRow = `<tr><td colspan="5" style="text-align: center;">지원 내역이 없습니다.</td></tr>`;
				tbody.append(emptyRow);
			}

			applications.forEach(function(application, index) {
				const row = makeRow(application, index);
				tbody.append(row);
			});

			let countAll = applications.length;

			let countInProgress = 0;
			applications.forEach(function(application) {
				if (application.status === 'APPLIED') {
					countInProgress++;
				}
			});

			let countFinal = countAll - countInProgress;

			$('#count-all').text(countAll);
			$('#count-in-progress').text(countInProgress);
			$('#count-final').text(countFinal);

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

function makeRow(application, index) {

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
            <td>${index + 1}</td>
            <td><span class="application-title-text">${application.jobPostTitle}</span></td>
            <td>${application.employerCompanyName}</td>
            <td>${application.appliedAt}</td>
            <td>${statusText}</td>
        </tr>
    `;
}