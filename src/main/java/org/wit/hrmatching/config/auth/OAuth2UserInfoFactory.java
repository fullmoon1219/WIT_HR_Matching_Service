package org.wit.hrmatching.config.auth;

import org.wit.hrmatching.vo.UserVO;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static UserVO extract(String registrationId, Map<String, Object> attributes) {
        if ("google".equals(registrationId)) {
            return parseGoogle(attributes);
        } else if ("naver".equals(registrationId)) {
            return parseNaver(attributes);
        }

        throw new IllegalArgumentException("지원하지 않는 소셜 로그인: " + registrationId);
    }

    private static UserVO parseGoogle(Map<String, Object> attr) {
        return UserVO.builder()
                .email((String) attr.get("email"))
                .name((String) attr.get("name"))
                .role("APPLICANT")
                .status("ACTIVE")
                .isActive(true)
                .emailVerified(true)
                .build();
    }

    private static UserVO parseNaver(Map<String, Object> attr) {
        Object responseObj = attr.get("response");
        if (responseObj instanceof Map<?, ?> map) {
            Map<String, Object> response = castToMap(map);
            return UserVO.builder()
                    .email((String) response.get("email"))
                    .name((String) response.get("name"))
                    .role("APPLICANT")
                    .status("ACTIVE")
                    .isActive(true)
                    .emailVerified(true)
                    .build();
        }
        throw new IllegalArgumentException("Naver 사용자 정보가 올바르지 않습니다.");
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> castToMap(Map<?, ?> rawMap) {
        return (Map<String, Object>) rawMap;
    }
}

