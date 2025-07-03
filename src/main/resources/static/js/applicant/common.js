function showToastOnLoad() {
	const message = sessionStorage.getItem('toastMessage');
	if (message) {
		Toastify({
			text: message,
			duration: 3000,
			gravity: "top",
			position: "center",
			stopOnFocus: false,
			style: {
				background: "#6495ED",
				color: "#ffffff"
			}
		}).showToast();
		sessionStorage.removeItem('toastMessage');
	}
}

function getIdFromUrl() {
	const parts = window.location.pathname.split('/');
	return parts[parts.length - 1];
}

function translateEmploymentType(code) {
	switch (code) {
		case 'FULLTIME': return '정규직';
		case 'PARTTIME': return '파트타임';
		case 'INTERN': return '인턴';
		case 'FREELANCE': return '프리랜서';
		default: return '기타';
	}
}

function translateExperienceType(code) {
	switch (code) {
		case 'NEWCOMER': return '신입';
		case 'EXPERIENCED': return '경력';
		case 'ANY': return '경력무관';
		default: return '기타';
	}
}

function translateApplicationStatus(status) {
	switch (status) {
		case 'APPLIED':
			return '지원 완료';
		case 'ACCEPTED':
			return '최종 합격';
		case 'REJECTED':
			return '불합격';
		default:
			return '확인중';
	}
}

function validateResumeForm() {
	const requiredFields = [
		{ name: 'title', label: '제목' },
		{ name: 'education', label: '학력' },
		{ name: 'experience', label: '경력' },
		{ name: 'skills', label: '기술 스택' },
		{ name: 'preferredLocation', label: '선호 지역' },
		{ name: 'salaryExpectation', label: '희망 연봉' },
		{ name: 'desiredPosition', label: '희망 직무' },
		{ name: 'motivation', label: '지원 동기' },
		{ name: 'coreCompetency', label: '핵심 역량' }
	];

	let isValid = true;
	$('.error').text('');

	for (const field of requiredFields) {
		const $input = $(`input[name="${field.name}"], textarea[name="${field.name}"]`); // textarea도 포함
		const value = $.trim($input.val());

		if (!value) {
			$(`#error-${field.name}`).text(`${field.label}은(는) 필수 입력 항목입니다.`);
			$input[0].scrollIntoView({ behavior: 'smooth', block: 'center' });
			$input.focus();
			isValid = false;
			break;
		}
	}
	return isValid;
}

function renderPagination(pagingInfo) {

	const paginationContainer = $('#pagination');
	paginationContainer.empty();

	// 보여줄 페이지가 없거나 1페이지만 있을 때
	if (!pagingInfo || pagingInfo.totalPage <= 1) {
		return;
	}

	let paginationHtml = '';

	// '처음 <<' & '이전 <' 버튼
	if (pagingInfo.hasPrevBlock) {
		// 첫 페이지 블록으로 이동
		paginationHtml += `<a href="#" class="arrow" data-page="1">&lt;&lt;</a>`;
	}
	if (pagingInfo.currentPage > 1) {
		paginationHtml += `<a href="#" class="arrow" data-page="${pagingInfo.currentPage - 1}">&lt;</a>`;
	}

	// 페이지 번호 목록
	for (let i = pagingInfo.startPage; i <= pagingInfo.endPage; i++) {
		if (i === pagingInfo.currentPage) {
			paginationHtml += `<span class="current-page">${i}</span>`;
		} else {
			paginationHtml += `<a href="#" data-page="${i}">${i}</a>`;
		}
	}

	// '다음 >' & '마지막 >>' 버튼
	if (pagingInfo.currentPage < pagingInfo.totalPage) {
		paginationHtml += `<a href="#" class="arrow" data-page="${pagingInfo.currentPage + 1}">&gt;</a>`;
	}
	if (pagingInfo.hasNextBlock) {
		paginationHtml += `<a href="#" class="arrow" data-page="${pagingInfo.totalPage}">&gt;&gt;</a>`;
	}

	paginationContainer.html(paginationHtml);

}


$(document).ready(function() {
	showToastOnLoad();
});