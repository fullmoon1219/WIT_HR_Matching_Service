// /js/interview/interview-feedback.js

$(document).ready(function () {
    const raw = sessionStorage.getItem("interviewEvaluations");

    if (!raw) {
        $("#feedbackContainer").html("<p class='error'>면접 피드백 데이터를 찾을 수 없습니다.</p>");
        return;
    }

    const evaluations = JSON.parse(raw);
    const $container = $("#feedbackContainer");
    $container.empty();

    // 우선 "분석 중" 메시지 표시
    $container.html(`<p class='loading-message'>답변을 분석하는 중입니다...</p>`);

    // 모든 평가 요청을 비동기로 수행
    const tasks = evaluations.map(({ question, answer }) =>
        evaluateAnswer(question, answer).then((feedback) => ({
            question,
            answer,
            feedback
        }))
    );

    Promise.all(tasks).then((results) => {
        sessionStorage.setItem("interviewEvaluations", JSON.stringify(results));
        renderFeedback(results);
    }).catch(() => {
        $container.html("<p class='error'>AI 분석 중 오류가 발생했습니다.</p>");
    });
});

/**
 * 서버에 AI 평가 요청
 */
function evaluateAnswer(question, answer) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: "/api/ai/interview/evaluate",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({ question, answer }),
            success: function (response) {
                resolve(response);
            },
            error: function () {
                reject();
            }
        });
    });
}

/**
 * 결과 렌더링
 */
function renderFeedback(evaluations) {
    const $container = $("#feedbackContainer");
    $container.empty();

    evaluations.forEach((item, index) => {
        const html = `
            <div class="interview-card">
                <div class="question-label">Q${index + 1}. ${escapeHtml(item.question)}</div>
                <div class="answer-text">${escapeHtml(item.answer)}</div>
                <div class="ai-feedback">→ ${escapeHtml(item.feedback)}</div>
            </div>
        `;
        $container.append(html);
    });
    $("#backToListBtn").show();
}

function escapeHtml(str) {
    return str.replace(/[&<>"']/g, (m) => ({
        '&': '&amp;', '<': '&lt;', '>': '&gt;',
        '"': '&quot;', "'": '&#039;'
    }[m]));
}
