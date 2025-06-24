// 등록 버튼 클릭 시
$('#btnRegister').click(function () {
	if (!validateResumeForm()) return;

	// input 추가
	$('<input>').attr({
		type: 'hidden',
		name: 'action',
		value: 'REGISTER'
	}).appendTo('form');

	$('form').submit();
});

// 임시 저장 버튼 클릭 시
$('#btnDraft').click(function () {

	const title = $.trim($('input[name="title"]').val());

	// 제목만 유효성 검사
	if (title === '') {
		alert('제목을 입력해주세요.');
		$('input[name="title"]').focus();
		return;
	}

	$('<input>').attr({
		type: 'hidden',
		name: 'action',
		value: 'DRAFT'
	}).appendTo('form');

	$('form').submit();
});