package org.wit.hrmatching.config.auth.oAuth2;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.wit.hrmatching.vo.user.CustomUserDetails;
import org.wit.hrmatching.vo.user.UserVO;

import java.util.Map;

public class CustomOAuth2User extends CustomUserDetails implements OAuth2User {

    private final Map<String, Object> attributes;

    public CustomOAuth2User(UserVO user, Map<String, Object> attributes) {
        super(user);
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(getUser().getId());
    }
}
