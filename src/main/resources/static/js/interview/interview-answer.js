// /js/interview/interview-answer.js

$(document).ready(function () {
    const resumeId = extractResumeIdFromUrl();
    if (resumeId) {
        showLoadingMessage();
        generateInterviewQuestions(resumeId);
    } else {
        $("#questionContainer").html("<p class='error'>이력서 ID를 찾을 수 없습니다.</p>");
    }

    $("#submitInterviewBtn").on("click", function () {
        const questionCards = $(".interview-card");
        const qaList = [];

        for (let i = 0; i < questionCards.length; i++) {
            const $card = $(questionCards[i]);
            const question = $card.find(".question-label").text().replace(/^Q\d+\.\s*/, "").trim();
            const answer = $card.find("textarea").val().trim();

            if (!answer) {
                alert(`Q${i + 1}번에 답변을 작성해 주세요.`);
                return;
            }

            qaList.push({ question, answer });
        }

        // 질문+답변을 우선 저장 (피드백은 아직 없음)
        sessionStorage.setItem("interviewEvaluations", JSON.stringify(qaList));

        // 피드백 분석은 다음 페이지에서 시작
        window.location.href = "/interview/evaluation";
    });

});

/**
 * 현재 URL에서 resumeId 추출
 */
function extractResumeIdFromUrl() {
    const pathSegments = window.location.pathname.split('/');
    const answerIndex = pathSegments.indexOf("answer");
    if (answerIndex !== -1 && pathSegments.length > answerIndex + 1) {
        return pathSegments[answerIndex + 1];
    }
    return null;
}

/**
 * 로딩 메시지 표시
 */
function showLoadingMessage() {
    $("#questionContainer").html("<p class='loading-message'>면접 질문을 생성하는 중입니다...</p>");
}

/**
 * 질문 생성 요청
 */
function generateInterviewQuestions(resumeId) {
    $.ajax({
        url: "/api/ai/interview/questions",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({ resumeId: resumeId }),
        success: function (questions) {
            renderInterviewQuestions(questions);
        },
        error: function () {
            $("#questionContainer").html("<p class='error'>면접 질문 생성에 실패했습니다.</p>");
        }
    });
}

/**
 * 질문 목록 렌더링
 */
function renderInterviewQuestions(questions) {
    const $container = $("#questionContainer");
    $container.empty();

    if (!questions || questions.length === 0) {
        $container.html("<p class='empty'>질문이 생성되지 않았습니다.</p>");
        return;
    }

    questions.forEach((question, index) => {
        const html = `
            <div class="interview-card">
                <div class="question-label">Q${index + 1}. ${question}</div>
                <textarea class="answer-textarea" rows="5" placeholder="여기에 답변을 작성하세요..."></textarea>
            </div>
        `;
        $container.append(html);
    });

    $("#submitInterviewBtn").css("display", "inline-block");
}

function evaluateAnswer(question, answer) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: "/api/ai/interview/evaluate",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({ question: question, answer: answer }),
            success: function (response) {
                resolve(response); // 서버에서 리턴된 피드백 텍스트
            },
            error: function () {
                reject("평가 실패");
            }
        });
    });
}