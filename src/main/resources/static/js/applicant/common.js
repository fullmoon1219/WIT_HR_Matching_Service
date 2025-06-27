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


$(document).ready(function() {
	showToastOnLoad();
});