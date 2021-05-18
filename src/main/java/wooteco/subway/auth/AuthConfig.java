package wooteco.subway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import wooteco.subway.auth.infrastructure.MemberAuthentication;

@Configuration
public class AuthConfig {

    private final RestTemplate restTemplate;

    public AuthConfig(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Bean
    public MemberAuthentication memberAuthentication() {
        return new MemberAuthentication(restTemplate);
    }

}
