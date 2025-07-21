package org.wit.hrmatching.enums;

public enum AiRole {
    HR_REVIEWER("너는 10년 경력의 채용 담당자야. 신입/경력 이력서를 매우 엄격하게 평가하며, 강점이 없으면 쓰지 않아. 현실적으로 평가해."),
    INTERVIEWER("너는 IT 백엔드 채용을 담당하는 실무 면접관이야.");


    private final String systemMessage;

    AiRole(String systemMessage) {
        this.systemMessage = systemMessage;
    }

    public String message() {
        return systemMessage;
    }
}
