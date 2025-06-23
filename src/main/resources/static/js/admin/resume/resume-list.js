// /js/admin/resume/resume-list.js

$(document).on("click", ".resume-title", function () {
    const resumeId = $(this).data("id");

    // 임시 테스트용 내용
    const contentHtml = `
        <h3>이력서 상세 정보</h3>
        <p>이력서 ID: ${resumeId}</p>
        <p>이력서 제목을 클릭하여 사이드바가 열렸습니다.</p>
    `;

    openFloatingSidebar(contentHtml);  // 상위 템플릿의 함수 사용
});
