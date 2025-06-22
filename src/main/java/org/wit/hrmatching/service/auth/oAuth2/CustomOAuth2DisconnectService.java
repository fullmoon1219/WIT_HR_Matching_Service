package org.wit.hrmatching.service.auth.oAuth2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CustomOAuth2DisconnectService {

    private final OAuth2AuthorizedClientService authorizedClientService;

    @Value("spring.security.oauth2.client.registration.naver.client-id")
    private String naverClientID;

    @Value("spring.security.oauth2.client.registration.naver.client-secret")
    private String naverClientSecret;

    public void disconnectFromSocial(OAuth2AuthenticationToken token, String registrationId) {
        String accessToken = getAccessToken(token);

        if ("google".equals(registrationId)) {
            disconnectGoogle(accessToken);

        } else if ("naver".equals(registrationId)) {
            disconnectNaver(accessToken);
        }
    }

    private String getAccessToken(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );
        return client != null ? client.getAccessToken().getTokenValue() : null;
    }

    // Google 연결 해제
    public void disconnectGoogle(String accessToken) {
        String url = "https://oauth2.googleapis.com/revoke";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(url, request, String.class);
    }

    // Naver 연결 해제
    public void disconnectNaver(String accessToken) {

        String url = "https://nid.naver.com/oauth2.0/token"
                + "?grant_type=delete"
                + "&client_id=" + naverClientID
                + "&client_secret=" + naverClientSecret
                + "&access_token=" + accessToken
                + "&service_provider=NAVER";

        new RestTemplate().getForObject(url, String.class);
    }
}
