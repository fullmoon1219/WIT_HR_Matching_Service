package org.wit.hrmatching.config.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        // 로그아웃 요청이 처리되었는지 확인하기 위한 로그
        logger.info("Logout request received for user: {}", authentication != null ? authentication.getName() : "Anonymous");

        // 세션 초기화
        request.getSession().invalidate();

        // 로그 추가 (세션 초기화 로그)
        logger.info("Session invalidated for user: {}", authentication != null ? authentication.getName() : "Anonymous");

        // 로그아웃 후 리디렉션 경로 설정
        String redirectUrl = "/";

        // 로그 추가 (리디렉션 경로 로그)
        logger.info("Redirecting to: {}", redirectUrl);

        // 리디렉션
        response.sendRedirect(redirectUrl);
    }
}

