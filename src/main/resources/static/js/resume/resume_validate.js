// resume/resume_register.html
// resume/resume_edit.html

$(document).ready(function() {

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

        for (let field of requiredFields) {
            const value = $.trim($(`input[name="${field.name}"]`).val());
            if (!value) {
                alert(`${field.label}은(는) 필수 입력 항목입니다.`);
                $(`input[name="${field.name}"]`).focus();
                return false;
            }
        }

        return true;
    }

    // 등록 버튼 클릭 시
    $('#btnRegister').click(function () {
        if (!validateResumeForm()) return;

        // input 추가
        $('<input>').attr({
            type: 'hidden',
            name: 'action',
            value: 'register'
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
            value: 'draft'
        }).appendTo('form');

        $('form').submit();
    });
});