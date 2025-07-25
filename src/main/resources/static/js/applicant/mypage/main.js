// applicant/mypage/main.html

$(document).ready(function () {

	let recentApplicationList;

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

	$.ajax({
		url: '/api/profile/dashboard',
		method: 'GET',
		success: function (data) {

			const userProfile = data.userProfile;
			const applicationCount = data.applicationCount;
			const resumeCount = data.resumeCount;
			const bookmarkCount = data.bookmarkCount;
			recentApplicationList = data.recentApplicationList;

			const profileImage = userProfile.profileImage;

			if (userProfile.profileImage && userProfile.profileImage.trim() !== '') {
				$('#profile-image').attr('src', '/uploads/users/profile/' + profileImage);
			}

			$('.userprofile-name strong').text(userProfile.name + '님');
			$('.userprofile-name p').text('(' + translateGender2(userProfile.gender)
				+ ', ' + userProfile.age + '세)');

			$('#applicationCount').text(applicationCount);
			$('#resumeCount').text(resumeCount);
			$('#bookmarkCount').text(bookmarkCount);

			const resumeCard = $('.resume-type-card');
			const resumeTitle = resumeCard.find('.resume-title');
			const resumeDesc = resumeCard.find('.resume-desc');

			if (resumeCount === 0) {
				// 이력서가 하나도 없을 때
				resumeTitle.text('새 이력서 작성하기');
				resumeDesc.html('첫 이력서를 완성하고<br>구직 활동을 시작해보세요!');
				resumeCard.attr('href', '/applicant/resume/register');
			} else {
				// 이력서가 하나 이상 있을 때
				resumeTitle.text('내 이력서 관리');
				resumeDesc.html(`총 ${resumeCount}개의 이력서가 있습니다.<br>관리하거나 새 이력서를 작성하세요.`);
				resumeCard.attr('href', '/applicant/resume/list');
			}


			const tbody = $('#applicationListBody');

			if (!recentApplicationList || recentApplicationList.length === 0) {
				$(tbody).hide();
				$('.no-data').show();
			} else {
				$(tbody).show();
				$('.no-data').hide();
				tbody.empty();

				// 목록 렌더링
				recentApplicationList.forEach(function(application) {
					const row = makeRow(application);
					tbody.append(row);
				});
			}

			if (recentApplicationList.length <= 2) {
				$('.more-btn-container').hide();
			} else {
				$('.more-btn-container').show();
			}

		},
		error: function (xhr) {

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

	$('#applicationListBody').on('click', '.application-title-text', function() {

		const applicationId = $(this).closest('.recommend-list-row').data('application-id');
		const applicationData = recentApplicationList.find(app => app.id === applicationId);

		if (!applicationData) {
			alert("상세 정보를 불러오는 데 실패했습니다.");
			return;
		}

		$('#application-modal').dialog('option', 'title', applicationData.jobPostTitle);
		let modalContentHtml = '';

		// 헤더 부분
		modalContentHtml += '<h3>' + applicationData.employerCompanyName + '</h3>';
		modalContentHtml += '<h2>' + applicationData.jobPostTitle + '</h2>';
		modalContentHtml += '<h4>지원 상태: ' + translateApplicationStatus(applicationData.status) + '</h4>';
		modalContentHtml += '<hr />';

		// 본문 1: 지원 정보
		if (applicationData.resumeDeletedAt) {
			modalContentHtml += '<div class="info-card deleted">';
			modalContentHtml += `
            <div class="card-header">
                <h4 class="card-title">📄 내가 제출한 이력서</h4>
            </div>
            <div class="card-body">
                <div class="info-row">
                    <span class="info-label">제출 이력서</span>
                    <span class="info-value">${applicationData.resumeTitle}</span>
                </div>
                <div class="info-row">
                    <span class="info-label">지원 일시</span>
                    <span class="info-value">${applicationData.appliedAt}</span>
                </div>
            </div>
            <div class="deleted-message">삭제된 이력서입니다.</div>
        `;
			modalContentHtml += '</div>';
		} else {
			modalContentHtml += `<div id="resumeInfo" class="info-card linkable" data-resume-id="${applicationData.resumeId}">`;
			modalContentHtml += `
            <div class="card-header">
                <h4 class="card-title">📄 내가 제출한 이력서</h4>
                <span class="card-arrow">→</span>
            </div>
            <div class="card-body">
                <div class="info-row">
                    <span class="info-label">제출 이력서</span>
                    <span class="info-value">${applicationData.resumeTitle}</span>
                </div>
                <div class="info-row">
                    <span class="info-label">지원 일시</span>
                    <span class="info-value">${applicationData.appliedAt}</span>
                </div>
            </div>
        `;
			modalContentHtml += '</div>';
		}

		// 본문 2: 공고 정보
		if (applicationData.jobPostDeletedAt) {
			modalContentHtml += '<div class="info-card deleted">';
			modalContentHtml += `
            <div class="card-header">
                <h4 class="card-title">🏢 지원한 기업 정보</h4>
            </div>
            <div class="card-body">
                 <div class="info-row">
                    <span class="info-label">지원한 기업</span>
                    <span class="info-value">${applicationData.employerCompanyName}</span>
                </div>
            </div>
            <div class="deleted-message">마감되었거나 더 이상 확인할 수 없는 공고입니다.</div>
        `;
			modalContentHtml += '</div>';
		} else {
			modalContentHtml += `<div id="jobPostInfo" class="info-card linkable" data-job-post-id="${applicationData.jobPostId}">`;
			modalContentHtml += `
            <div class="card-header">
                <h4 class="card-title">🏢 지원한 기업 정보</h4>
                <span class="card-arrow">→</span>
            </div>
            <div class="card-body">
                <div class="info-row">
                    <span class="info-label">지원한 기업</span>
                    <span class="info-value">${applicationData.employerCompanyName}</span>
                </div>
                <div class="info-row">
                    <span class="info-label">근무 지역</span>
                    <span class="info-value">${applicationData.jobPostLocation}</span>
                </div>
                <div class="info-row">
                    <span class="info-label">고용 형태</span>
                    <span class="info-value">${translateEmploymentType(applicationData.jobPostJobCategory)}</span>
                </div>
                <div class="info-row">
                    <span class="info-label">마감일</span>
                    <span class="info-value">${applicationData.jobPostDeadline}</span>
                </div>
            </div>
        `;
			modalContentHtml += '</div>';
		}

		modalContentHtml += '<hr />';

		// 본문 3: 전형 진행 과정
		modalContentHtml += '<div>';
		modalContentHtml += '<h3 class="modal-section-title">전형 진행 과정</h3>';
		modalContentHtml += '<div class="progress-timeline">';

		// 지원 완료
		modalContentHtml += '<div class="progress-step active">';
		modalContentHtml += '<p class="progress-date">😎 지원 완료: ' + applicationData.appliedAt + '</p>';
		modalContentHtml += '<p class="progress-desc">회원님의 이력서가 기업에 안전하게 전달되었어요.</p>';
		modalContentHtml += '</div>';

		// 기업 열람
		if (applicationData.viewedAt) {
			modalContentHtml += '<div class="progress-step active">';
			modalContentHtml += '<p class="progress-date">🖥️ 기업 열람: ' + applicationData.viewedAt + '</p>';
			modalContentHtml += '<p class="progress-desc">인사담당자가 회원님의 이력서를 확인했어요. 좋은 소식을 기다려보세요!</p>';
			modalContentHtml += '</div>';
		}

		// 최종 결과
		if (applicationData.status === 'ACCEPTED') {
			modalContentHtml += '<div class="progress-step active">';
			modalContentHtml += '<p class="progress-date">🎉 최종 합격: ' + applicationData.updatedAt + '</p>';
			modalContentHtml += '<p class="progress-desc">축하합니다! 서류 전형에 최종 합격하셨습니다.</p>';
			modalContentHtml += '</div>';
		} else if (applicationData.status === 'REJECTED') {
			modalContentHtml += '<div class="progress-step active">';
			modalContentHtml += '<p class="progress-date">✉️ 최종 결과: ' + applicationData.updatedAt + '</p>';
			modalContentHtml += '<p class="progress-desc">안타깝게도 이번 채용과는 인연이 닿지 않았습니다. 더 좋은 기회가 회원님을 기다리고 있을 거예요.</p>';
			modalContentHtml += '</div>';
		}

		modalContentHtml += '</div>';
		modalContentHtml += '</div>';

		$('#application-modal').html(modalContentHtml).dialog('open');
	});

	// 모달 내: 이력서 보기 클릭
	$(document).on('click', '#resumeInfo', function() {
		const resumeId = $(this).data('resume-id');
		if (resumeId) window.open(`/applicant/resume/view/${resumeId}`, '_blank');
	});

	// 모달 내: 공고 보기 클릭
	$(document).on('click', '#jobPostInfo', function() {
		const jobPostId = $(this).data('job-post-id');
		if (jobPostId) window.open(`/recruit/view/${jobPostId}`, '_blank');
	});

	$('#application-stat').on('click', function () {
		location.href = '/applicant/applications'
	});

	$('#resume-stat').on('click', function () {
		location.href = '/applicant/resume/list';
	});

	$('#bookmark-stat').on('click', function () {
		location.href = '/applicant/bookmarks'
	});

	$('.more-btn').on('click', function () {
		location.href = '/applicant/applications';
	});
});

function makeRow(application) {
	let statusText = '';

	if (application.status === 'ACCEPTED' || application.status === 'REJECTED') {
		statusText = translateApplicationStatus(application.status);
	} else if (application.viewedAt != null) {
		statusText = '기업 열람';
	} else {
		statusText = translateApplicationStatus(application.status);
	}

	return `
		<div class="recommend-list-row" data-application-id="${application.id}">
			<div class="cell">
				<span class="application-title-text">${application.jobPostTitle}</span>
			</div>
			<div class="cell">${application.employerCompanyName}</div>
			<div class="cell">${application.jobPostLocation}</div>
			<div class="cell">${application.jobPostDeadline}</div>
			<div class="cell">${statusText}</div>
		</div>
    `;
}