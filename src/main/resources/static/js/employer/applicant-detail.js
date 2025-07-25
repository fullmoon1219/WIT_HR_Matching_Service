// /js/employer/applicant-detail.js

$(document).ready(function () {
    // [🔒 닫기] 버튼 클릭
    $(document).on('click', '.action-button.close', function () {
        closeFloatingSidebar();
    });

    $(document).on('click', '.action-button.pass, .action-button.reject, .action-button.wait', function () {
        const status = $(this).data('status');       // "합격", "불합격", "대기"
        const applicationId = $(this).data('id');    // 지원서 ID를 버튼에 설정해줘야 함

        if (!applicationId) {
            alert("지원서 ID가 없습니다.");
            return;
        }

        $.ajax({
            url: '/employer/application/update_status',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ applicationId, status }),
            success: function () {
                alert(`상태가 '${status}'(으)로 변경되었습니다.`);
                location.reload(); // 또는 해당 행만 업데이트
            },
            error: function () {
                alert("상태 변경 중 오류가 발생했습니다.");
            }
        });
    });
    // ESC 키 누르면 닫기
    $(document).on('keydown', function (e) {
        if (e.key === "Escape") {
            closeFloatingSidebar();
        }
    });

    // 오버레이 더블 클릭 시 닫기
    $(document).on('dblclick', '#floatingOverlay', function () {
        closeFloatingSidebar();
    });

    // 플로팅 사이드바 닫기 함수
    function closeFloatingSidebar() {
        $('#floatingOverlay').removeClass('show');
        $('#floatingSidebar').removeClass('show');
        $('#floatingSidebarContent').empty();
        // 🔄 페이지 전체 새로고침
        location.reload();
    }

    // [🧠 AI 정보 보기] 버튼 클릭 시
    $(document).on('click', '.ai-info-button', function () {
        const resumeId = $(this).data('resume-id');
        console.log(resumeId);

        $(".ai-info-text").text("이력서를 분석하는 중입니다...").show();
        $(this).hide();

        $.ajax({
            url: "/api/ai/resumes/summary",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({ resumeId: resumeId }),
            success: function (response) {
                $("#ai-review").text(response.summary);
                $(".ai-info-text").hide(); // 완료 시 텍스트 숨김
            },
            error: function (xhr) {
                $("#ai-review").text("요약에 실패했습니다.");
                $(".ai-info-text").hide(); // 실패 시도 숨김
            }
        });
    });

});

