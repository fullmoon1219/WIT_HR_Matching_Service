package org.wit.hrmatching.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.config.auth.CustomUserDetails;
import org.wit.hrmatching.mapper.UserMapper;
import org.wit.hrmatching.vo.UserVO;

@Slf4j
@Service
@RequiredArgsConstructor
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserVO user = userMapper.findByEmail(email);
        if (user == null) {
            log.warn("이메일이 존재하지 않음: {}", email);
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);
        }
        log.info("사용자 조회 완료: {}", email);
        return new CustomUserDetails(user);
    }
}