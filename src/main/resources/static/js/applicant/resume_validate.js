// applicant/resume/register.html
// applicant/resume/edit.html

$(document).ready(function() {

    function validateResumeForm() {
        const requiredFields = [
            { selector: 'input[name="title"]', label: '제목' },
            { selector: 'input[name="education"]', label: '학력' },
            { selector: 'input[name="experience"]', label: '경력' },
            { selector: 'input[name="skills"]', label: '기술 스택' },
            { selector: 'input[name="preferredLocation"]', label: '선호 지역' },
            { selector: 'input[name="salaryExpectation"]', label: '희망 연봉' },
            { selector: 'input[name="desiredPosition"]', label: '희망 직무' },
            { selector: 'input[name="motivation"]', label: '지원 동기' },
            { selector: 'input[name="coreCompetency"]', label: '핵심 역량' }
        ];

        for (let field of requiredFields) {
            const value = $.trim($(field.selector).val());
            if (!value) {
                alert(`${field.label}은(는) 필수 입력 항목입니다.`);
                $(field.selector).focus();
                return false;
            }
        }

        return true;
    }

});