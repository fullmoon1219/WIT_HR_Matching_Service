// applicant/mypage/profile.html

// 현재 수정 모드인지 상태를 저장하는 변수
let isEditMode = false;
// 사용자가 수정을 취소할 때를 대비해 원래 데이터를 저장해 둘 변수
let originalData = {};

const passwordModal = $('#passwordModal');

$(document).ready(function () {

	$.ajax({
		url: '/api/profile',
		type: 'GET',
		success: function (data) {

			originalData = data;

			populateProfileData(data);

			if (originalData.loginType !== 'EMAIL') {
				$('#password-edit-button').hide();
			}

		},
		error: function (xhr) {

			if (xhr.status === 403) {
				location.href = '/error/access-denied';
			} else if (xhr.status === 404) {
				location.href = '/error/not-found';
			} else {
				alert('프로필 불러오기에 실패했습니다. 나중에 다시 시도해주세요.');
				console.error(xhr);
			}
		}
	});

	$('#editButton').on('click', function() {
		if (isEditMode) {
			saveProfileChanges();
		} else {
			switchToEditMode();
		}
	});

	// 보기 모드로 전환하고, 저장해둔 원본 데이터로 복원
	$('#cancelButton').on('click', function() {
		switchToViewMode();
		populateProfileData(originalData);
	});

	// '사진 등록' 버튼 이벤트 (다음 단계에서 구현)
	$('#uploadImageButton').on('click', function(e) {
		e.preventDefault();
		$('#profileImageInput').click();
	});

	$('#profileImageInput').on('change', function () {

		const file = this.files[0];

		// 파일이 선택되지 않았으면 아무것도 하지 않음
		if (!file) {
			return;
		}

		const formData = new FormData();
		formData.append('file', file);

		$.ajax({
			url: '/api/profile/image',
			type: 'POST',
			data: formData,
			processData: false,
			contentType: false,
			success: function (response) {

				$('#profileImagePreview').attr('src', response.filePath);

				Toastify({
					text: '프로필 이미지가 변경되었습니다.',
					duration: 2000,
					gravity: "top",
					position: "center",
					style: { background: "#6495ED", color: "#ffffff" }
				}).showToast();
			},
			error: function (xhr) {
				let errorMessage = '이미지 업로드에 실패했습니다. 다시 시도해주세요.';

				if (xhr.responseText) {
					errorMessage = xhr.responseText;
				}

				Toastify({
					text: errorMessage,
					duration: 3000,
					gravity: "top",
					position: "center",
					style: { background: "#CD5C5C", color: "#ffffff" }
				}).showToast();
			}
		});
	});

	// '비밀번호 변경' 버튼 클릭 시 모달 열기
	$('#password-edit-button').on('click', function() {
		resetPasswordModal();
		passwordModal.show();
	});

	// 모달의 닫기 버튼 클릭
	passwordModal.on('click', '.close-button', function() {
		passwordModal.hide();
	});

	$('#btnVerifyPassword').on('click', function() {

		const currentPassword = $('#currentPassword').val();
		const errorContainer = $('#passwordError');

		if (!currentPassword) {
			errorContainer.text('현재 비밀번호를 입력해주세요.');
			return;
		}

		$.ajax({
			url: '/api/profile/verify-password',
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({ currentPassword: currentPassword }),
			success: function(response) {
				if (response === 'ok') {

					errorContainer.text('');
					$('#verifyPasswordView').hide();
					$('#updatePasswordView').show();
				}
			},
			error: function(xhr) {
				errorContainer.text(xhr.responseText || '비밀번호가 일치하지 않습니다.');
			}
		});
	});

	$('#btnUpdatePassword').on('click', function() {

		const newPassword = $('#newPassword').val();
		const confirmNewPassword = $('#confirmNewPassword').val();
		const errorContainer = $('#newPasswordError');

		// 클라이언트 단에서 유효성 검사
		if (!newPassword || !confirmNewPassword) {
			errorContainer.text('새 비밀번호를 모두 입력해주세요.');
			return;
		}
		if (newPassword !== confirmNewPassword) {
			errorContainer.text('새 비밀번호가 일치하지 않습니다.');
			return;
		}

		// 새 비밀번호를 서버로 전송
		$.ajax({
			url: '/api/profile/password',
			type: 'PUT',
			contentType: 'application/json',
			data: JSON.stringify({ newPassword: newPassword }),
			success: function(response) {

				passwordModal.hide();

				sessionStorage.setItem('toastMessage', response);
				location.reload();
			},
			error: function(xhr) {
				errorContainer.text(xhr.responseText || '비밀번호 변경에 실패했습니다.');
			}
		});
	});

	$('#currentPassword').on('keyup', function(event) {
		if (event.key === 'Enter') {
			$('#btnVerifyPassword').click();
		}
	});

	$('#newPassword, #confirmNewPassword').on('keyup', function(event) {
		if (event.key === 'Enter') {
			$('#btnUpdatePassword').click();
		}
	});
});

function populateProfileData(data) {

	const resumeId = data.primaryResumeId;

	if (data.profileImage) {
		$('#profileImagePreview').attr('src', '/uploads/users/profile/' + data.profileImage);
	} else {
		$('#profileImagePreview').attr('src', '/images/users/user_big_profile.png');
	}

	// 개인 정보 테이블
	$('#email').text(data.email);
	$('#name').text(data.name || '-');
	$('#age').text(data.age ? `${data.age}세` : '-');
	$('#gender').text(data.gender ? translateGender(data.gender) : '-');
	$('#address').text(data.address || '-');
	$('#phoneNumber').text(data.phoneNumber || '-');
	$('#jobType').text(data.jobType ? translateEmploymentType(data.jobType) : '-');
	$('#experienceYears').text(data.experienceYears > 0 ? `${data.experienceYears}년` : '-');
	$('#selfIntro').text(data.selfIntro || '등록된 자기소개가 없습니다. 수정 버튼을 눌러 추가할 수 있습니다.');

	// 포트폴리오 링크 처리
	if (data.portfolioUrl) {
		const linkHtml = `<a href="${data.portfolioUrl}" target="_blank">${data.portfolioUrl}</a>`;
		$('#portfolioUrl').html(linkHtml);
	} else {
		$('#portfolioUrl').text('-');
	}

	// 시스템 정보 테이블
	$('#createAt').text(data.createAt);
	$('#lastLogin').text(data.lastLogin);
	$('#loginType').html(getLoginTypeWithIcon(data.loginType));
	$('#emailVerified').text(data.emailVerified ? '인증' : '미인증');
	$('#warningCount').text((data.warningCount || 0) + '회');

	// 대표 이력서 카드
	const cardBody = $('.primary-resume-card .card-body'); // 중복 사용을 위해 변수로 저장

	if (resumeId) {
		cardBody.removeClass('is-empty');

		$('#resumeTitle').text(data.resumeTitle);
		$('#resumeUpdatedAt').text(`최근 수정일: ${data.resumeUpdatedAt}`);
		$('.btn-view-resume').attr('href', `/applicant/resume/view/${resumeId}`).show();

	} else {
		cardBody.addClass('is-empty');

		$('#resumeTitle').text('대표 이력서가 없습니다.');
		$('#resumeUpdatedAt').text('이력서 관리 페이지에서 대표 이력서를 설정해주세요.');
		$('.btn-view-resume').hide();
	}
}

function getLoginTypeWithIcon(loginType) {
	switch (loginType) {
		case 'EMAIL':
			return `
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="login-icon">
                    <path d="M1.5 8.67v8.58a3 3 0 0 0 3 3h15a3 3 0 0 0 3-3V8.67l-8.928 5.493a3 3 0 0 1-3.144 0L1.5 8.67Z" />
                    <path d="M22.5 6.908V6.75a3 3 0 0 0-3-3h-15a3 3 0 0 0-3 3v.158l9.714 5.978a1.5 1.5 0 0 0 1.572 0L22.5 6.908Z" />
                </svg>
                이메일
            `;
		case 'GOOGLE':
			return `
                <img src="/images/icon/icon_google.png" alt="구글 로그인" class="login-icon">
                구글
            `;
		case 'NAVER':
			return `
                <img src="/images/icon/icon_naver.png" alt="네이버 로그인" class="login-icon">
                네이버
            `;
		default:
			return '기타';
	}
}

function switchToEditMode() {
	isEditMode = true;

	// 버튼 상태 변경
	$('#editButton').text('수정 완료');
	$('#cancelButton').show();
	if (originalData.loginType === 'EMAIL') {
		$('#confirmPassword').show();
	}

	// 개인 정보 테이블의 텍스트를 입력 필드로 변경
	$('#name').html(`<input type="text" class="edit-input" id="editName" value="${originalData.name || ''}">`);
	$('#age').html(`<input type="number" class="edit-input" id="editAge" value="${originalData.age || ''}">`);
	$('#gender').html(`
        <select class="edit-select" id="editGender">
            <option value="MALE" ${originalData.gender === 'MALE' ? 'selected' : ''}>남성</option>
            <option value="FEMALE" ${originalData.gender === 'FEMALE' ? 'selected' : ''}>여성</option>
            <option value="OTHER" ${originalData.gender === 'OTHER' ? 'selected' : ''}>기타</option>
        </select>
    `);
	$('#portfolioUrl').html(`<input type="text" class="edit-input" id="editPortfolioUrl" value="${originalData.portfolioUrl || ''}">`);
	$('#phoneNumber').html(`<input type="text" class="edit-input" id="editPhoneNumber" value="${originalData.phoneNumber || ''}">`);
	$('#address').html(`<input type="text" class="edit-input" id="editAddress" value="${originalData.address || ''}">`);
	$('#experienceYears').html(`<input type="number" class="edit-input" id="editExperienceYears" value="${originalData.experienceYears || 0}">`);
	$('#jobType').html(`
        <select class="edit-select" id="editJobType">
            <option value="FULLTIME" ${originalData.jobType === 'FULLTIME' ? 'selected' : ''}>정규직</option>
            <option value="PARTTIME" ${originalData.jobType === 'PARTTIME' ? 'selected' : ''}>파트타임</option>
            <option value="INTERNSHIP" ${originalData.jobType === 'INTERNSHIP' ? 'selected' : ''}>인턴</option>
            <option value="FREELANCE" ${originalData.jobType === 'FREELANCE' ? 'selected' : ''}>프리랜서</option>
        </select>
    `);

	// 자기소개 섹션
	$('#selfIntro').html(`<textarea class="edit-textarea" id="editSelfIntro">${originalData.selfIntro || ''}</textarea>`);
}

function switchToViewMode() {
	isEditMode = false;

	// 버튼 상태 원래대로 변경
	$('#editButton').text('개인 정보 수정');
	$('#cancelButton').hide();
	$('#confirmPassword').hide().val('');

	populateProfileData(originalData);
}

function saveProfileChanges() {

	const ageValue = parseInt($('#editAge').val(), 10);
	const experienceYearsValue = parseInt($('#editExperienceYears').val(), 10);

	// 수정된 데이터를 DTO 형식에 맞는 객체로 만들기
	const updateData = {
		name: $('#editName').val(),
		age: isNaN(ageValue) ? null : ageValue,
		gender: $('#editGender').val(),
		portfolioUrl: $('#editPortfolioUrl').val(),
		phoneNumber: $('#editPhoneNumber').val(),
		address: $('#editAddress').val(),
		experienceYears: isNaN(experienceYearsValue) ? null : experienceYearsValue,
		jobType: $('#editJobType').val(),
		selfIntro: $('#editSelfIntro').val(),
	};

	// 이메일 로그인 시에만 비밀번호 확인
	if (originalData.loginType === 'EMAIL') {
		const password = $('#confirmPassword').val();
		if (!password) {
			alert('프로필을 수정하려면 비밀번호를 입력해야 합니다.');
			$('#confirmPassword').focus();
			return;
		}
		updateData.password = password;
	}

	// 서버에 데이터 전송
	$.ajax({
		url: '/api/profile',
		type: 'PUT',
		contentType: 'application/json',
		data: JSON.stringify(updateData),
		success: function () {

			sessionStorage.setItem('toastMessage', '프로필이 성공적으로 수정되었습니다.');
			location.reload();
		},
		error: function (xhr) {
			// 실패 시
			if (xhr.status === 401) {

				alert('비밀번호가 일치하지 않습니다. 다시 확인해주세요.');
				$('#confirmPassword').focus();
			} else {

				alert('프로필 수정 중 오류가 발생했습니다. 나중에 다시 시도해주세요.');
				console.error(xhr);
			}
		}
	});
}

// 모달 초기화 함수
function resetPasswordModal() {

	$('#verifyPasswordView').show();
	$('#updatePasswordView').hide();

	$('#currentPassword, #newPassword, #confirmNewPassword').val('');
	$('#passwordError, #newPasswordError').text('');
}