package org.wit.hrmatching.enums;

public enum AiRole {
    HR_REVIEWER("너는 이력서 평가를 전문으로 하는 HR 전문가야.");

    private final String systemMessage;

    AiRole(String systemMessage) {
        this.systemMessage = systemMessage;
    }

    public String message() {
        return systemMessage;
    }
}
