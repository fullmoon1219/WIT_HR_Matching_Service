package org.wit.hrmatching.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.config.auth.CustomOAuth2User;
import org.wit.hrmatching.config.auth.OAuth2UserInfoFactory;
import org.wit.hrmatching.vo.UserVO;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oAuth2User = super.loadUser(request);
        String registrationId = request.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 사용자 정보 파싱
        UserVO user = OAuth2UserInfoFactory.extract(registrationId, attributes);

        // DB에 저장 (없으면 insert)
        user.setPassword("SOCIAL_LOGIN_USER");
        userService.saveOrUpdate(user);

        // 로그인 세션용 OAuth2User 리턴
        return new CustomOAuth2User(user, attributes);
    }
}
