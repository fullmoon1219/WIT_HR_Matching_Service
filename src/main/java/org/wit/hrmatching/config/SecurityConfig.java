package org.wit.hrmatching.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.wit.hrmatching.config.auth.CustomOAuth2FailureHandler;
import org.wit.hrmatching.service.auth.CustomOAuth2UserService;
import org.wit.hrmatching.service.auth.CustomUserDetailsService;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(customUserDetailsService);
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> {
                    // 필요시 CSRF 설정을 비활성화하거나 조정
                })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/users/login", "/users/register", "/users/register-success",
                                "/users/logout-success", "/oauth2/**")
                        .permitAll()  // 로그인 없이 접근 허용
                        .anyRequest().authenticated()  // 그 외에는 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/users/login")  // 커스텀 로그인 페이지
                        .failureUrl("/users/login?error")  // 로그인 실패 시 URL
                        .loginProcessingUrl("/login")  // 로그인 처리 URL
                        .usernameParameter("email")  // 이메일로 로그인
                        .passwordParameter("password")  // 비밀번호 파라미터 이름 설정
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout")  // 로그아웃 경로 설정
                        .logoutSuccessUrl("/users/logout-success")  // 로그아웃 후 이동할 페이지 설정
                        .invalidateHttpSession(true)  // 세션 무효화
                        .deleteCookies("JSESSIONID")  // 로그아웃 시 쿠키 삭제
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/users/login")  // 소셜 로그인 페이지
                        .failureUrl("/users/login?oauth2error")  // 소셜 로그인 실패 시 URL
                        .failureHandler(new CustomOAuth2FailureHandler())  // 소셜 로그인 실패 처리 핸들러
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                )
                .authenticationProvider(authenticationProvider());  // 사용자 인증 제공자 설정

        return http.build();
    }

}

