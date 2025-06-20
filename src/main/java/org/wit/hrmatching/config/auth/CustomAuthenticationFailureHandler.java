package org.wit.hrmatching.config.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String errorMessage;

        if (exception instanceof DisabledException) {
            errorMessage = "비활성화된 계정입니다. 관리자에게 문의하세요.";

        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "이메일이 존재하지 않습니다.";

        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "비밀번호가 올바르지 않습니다.";

        } else if (exception instanceof OAuth2AuthenticationException oauth2Ex) {
            // CustomOAuth2UserService에서 던진 설명 메시지
            errorMessage = oauth2Ex.getError().getDescription();

        } else {
            errorMessage = "로그인에 실패했습니다. 다시 시도해주세요.";
        }

        // 에러 메시지를 세션에 저장
        request.getSession().setAttribute("errorMessage", errorMessage);

        // 로그인 페이지로 리다이렉트
        response.sendRedirect("/users/login");
    }
}

