package org.wit.hrmatching.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        System.out.println("접근 거부됨: " + request.getRequestURI());

        // REST API 요청일 경우
        if (request.getRequestURI().startsWith("/api")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // 일반 브라우저 요청일 경우
        } else {
            response.sendRedirect("/error/access-denied");
        }
    }
}
