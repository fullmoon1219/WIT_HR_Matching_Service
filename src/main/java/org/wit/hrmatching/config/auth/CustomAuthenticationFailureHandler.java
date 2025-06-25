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
            errorMessage = "존재하지 않는 이메일입니다.";

        } else if (exception instanceof BadCredentialsException) {
            String exMessage = exception.getMessage();
            if ("회원 유형이 일치하지 않습니다.".equals(exMessage)) {
                errorMessage = "선택한 회원 유형과 계정 정보가 일치하지 않습니다.";
            } else {
                errorMessage = "비밀번호가 올바르지 않습니다.";
            }

        } else if (exception instanceof OAuth2AuthenticationException oauth2Ex) {
            errorMessage = oauth2Ex.getError().getDescription();

        } else {
            errorMessage = "로그인에 실패했습니다. 다시 시도해주세요.";
        }

        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect("/users/login");
    }
}
