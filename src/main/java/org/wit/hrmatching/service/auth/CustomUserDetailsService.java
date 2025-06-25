package org.wit.hrmatching.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.vo.CustomUserDetails;
import org.wit.hrmatching.enums.UserStatus;
import org.wit.hrmatching.vo.UserVO;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserVO user = authService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("존재하지 않는 이메일입니다.");
        }
        if (!UserStatus.ACTIVE.name().equals(user.getStatus())) {
            throw new DisabledException("비활성화된 계정입니다. 관리자에게 문의하세요.");
        }

        return new CustomUserDetails(user);
    }
}
