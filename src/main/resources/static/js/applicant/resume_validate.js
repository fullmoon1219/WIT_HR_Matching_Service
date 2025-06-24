// applicant/resume/register.html
// applicant/resume/edit.html

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

    // 기존 에러 메시지 제거
    $('.error').text('');

    for (let field of requiredFields) {
        const $input = $(`input[name="${field.name}"]`);
        const value = $.trim($input.val());

        if (!value) {
            $(`#error-${field.name}`).text(`${field.label}은(는) 필수 입력 항목입니다.`);

            // 스크롤 이동 + 커서 포커스
            $input[0].scrollIntoView({ behavior: 'smooth', block: 'center' });
            $input.focus();

            isValid = false;
            break; // 가장 먼저 발견된 에러 하나만 표시
        }
    }

    return isValid;
}