package org.wit.hrmatching.config.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.wit.hrmatching.service.LoginHistoryService;
import org.wit.hrmatching.service.auth.AuthService;
import org.wit.hrmatching.vo.LoginHistoryVO;
import org.wit.hrmatching.vo.UserVO;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final LoginHistoryService loginHistoryService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Long userId = null;

        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            userId = userDetails.getUser().getId();
        } else if (authentication.getPrincipal() instanceof OAuth2User oauthUser) {
            String email = oauthUser.getAttribute("email");
            if (email != null) {
                UserVO user = authService.findByEmail(email);
                if (user != null) {
                    userId = user.getId();
                }
            }
        }

        if (userId != null) {
            // 로그인 시간 업데이트
            authService.updateLastLoginTime(userId);

            // 로그인 기록 저장
            String ip = extractClientIp(request);

            LoginHistoryVO history = new LoginHistoryVO();
            history.setUserId(userId);
            history.setIpAddress(ip);
            history.setUserAgent(request.getHeader("User-Agent"));
            loginHistoryService.insertLoginHistory(history);
        }

        // 이전 페이지로 이동 (단, /users/** 이면 홈으로)
        String referer = Objects.requireNonNullElse(
                (String) request.getSession().getAttribute("prevPage"), "/"
        );
        request.getSession().removeAttribute("prevPage");

        if (referer.contains("/users/")) {
            response.sendRedirect("/");
        } else {
            response.sendRedirect(referer);
        }
    }

    /**
     * 클라이언트의 실제 IP 주소를 반환
     */
    private String extractClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // IPv6 루프백을 IPv4로 변환
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        // X-Forwarded-For에 복수 IP가 있을 경우 첫 번째 것만 사용
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }
}
