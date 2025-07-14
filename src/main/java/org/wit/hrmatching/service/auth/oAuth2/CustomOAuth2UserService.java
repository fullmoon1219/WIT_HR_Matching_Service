package org.wit.hrmatching.service.auth.oAuth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.config.auth.oAuth2.CustomOAuth2User;
import org.wit.hrmatching.config.auth.oAuth2.OAuth2UserInfoFactory;
import org.wit.hrmatching.mapper.UserMapper;
import org.wit.hrmatching.service.auth.AuthService;
import org.wit.hrmatching.vo.user.UserVO;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthService authService;
    private final UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oAuth2User = super.loadUser(request);
        String registrationId = request.getClientRegistration().getRegistrationId(); // e.g., google, naver, kakao
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 사용자 정보 파싱 (email, name 등)
        UserVO parsedUser = OAuth2UserInfoFactory.extract(registrationId, attributes);
        parsedUser.setPassword(null); // 소셜 로그인에서는 비밀번호 null 처리

        // 기존 사용자 조회
        UserVO existingUser = userMapper.findByEmail(parsedUser.getEmail());

        if (existingUser != null) {
            if (existingUser.getLoginType() == null || !existingUser.getLoginType().equalsIgnoreCase(registrationId)) {
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("invalid_login_type", "이미 이메일로 가입된 계정입니다. 일반 로그인을 사용하세요.", null)
                );
            }
            // 이미 존재하며 동일한 소셜 제공자로 로그인한 경우 → 업데이트 필요 시 수행
            parsedUser.setId(existingUser.getId()); // ID 유지
        } else {
            // 신규 사용자라면 로그인 유형 지정
            parsedUser.setLoginType(registrationId.toUpperCase()); // 예: NAVER, GOOGLE, KAKAO
        }

        // 저장 또는 갱신
        authService.saveOrUpdate(parsedUser);

        // 유저 정보 전체 조회 (프로필 포함)
        UserVO fullUser = authService.getUserWithProfile(
                userMapper.findByEmail(parsedUser.getEmail()).getId());

        // 로그인 세션용 OAuth2User 반환
        return new CustomOAuth2User(fullUser, attributes);
    }



}
