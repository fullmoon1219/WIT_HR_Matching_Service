package org.wit.hrmatching.config.auth.oAuth2;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import jakarta.servlet.ServletException;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2FailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        logger.error("OAuth2 로그인 실패: {}", exception.getMessage());

        String errorMessage = "소셜 로그인 중 문제가 발생했습니다.";

        if (exception instanceof OAuth2AuthenticationException oauth2Ex) {
            errorMessage = oauth2Ex.getError().getDescription(); // ex: "이미 이메일로 가입된 계정입니다."
        }

        request.getSession().setAttribute("errorMessage", errorMessage);

        // 로그인 페이지로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, "/users/login");
    }
}


