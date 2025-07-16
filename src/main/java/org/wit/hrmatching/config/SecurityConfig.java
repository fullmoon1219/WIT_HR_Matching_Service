package org.wit.hrmatching.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.wit.hrmatching.config.auth.CustomAuthenticationFailureHandler;
import org.wit.hrmatching.config.auth.CustomAuthenticationProvider;
import org.wit.hrmatching.config.auth.CustomLoginSuccessHandler;
import org.wit.hrmatching.config.auth.oAuth2.CustomOAuth2FailureHandler;
import org.wit.hrmatching.service.auth.oAuth2.CustomOAuth2UserService;
import org.wit.hrmatching.service.auth.CustomUserDetailsService;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(customUserDetailsService, passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(customAuthenticationProvider())
                .build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher("/api/community/image-upload"),
                                new AntPathRequestMatcher("/api/community/delete-temp-images")
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/users/login", "/users/register", "/users/register-success", "/users/verify",
                                "/users/logout-success", "/oauth2/**", "/css/**", "/js/**", "/images/**", "/static/**",
                                "/api/users/email-exists", "/error/**", "/api/users", "/api/users/verify-email",
                                "/users/delete-success", "/employer/**", "/users/mypage/**",
                                "/recruit/**", "/api/recruit/**", "/api/tech-stacks")
                        .permitAll()  // 로그인 없이 접근 허용

                        .requestMatchers("/api/auth/resend-verification").authenticated()

                        .requestMatchers("/admin/**").hasAuthority("ADMIN") // 관리자 전용
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
//                        .requestMatchers("/employer/**").hasAuthority("EMPLOYER")
//                        .requestMatchers("/applicant/**").hasAuthority("APPLICANT")
                        .anyRequest().authenticated()  // 그 외에는 인증 필요
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .formLogin(form -> form
                        .loginPage("/users/login")
                        .failureHandler(customAuthenticationFailureHandler)
                        .successHandler(customLoginSuccessHandler)
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
//                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/users/login")
                        .failureUrl("/users/login?oauth2error")
                        .failureHandler(customOAuth2FailureHandler)
                        .successHandler(customLoginSuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessUrl("/users/logout-success")
                        .invalidateHttpSession(true)  // 세션 무효화
                        .deleteCookies("JSESSIONID")   // 로그아웃 시 쿠키 삭제
                )
                .authenticationProvider(customAuthenticationProvider());

        return http.build();
    }

}

