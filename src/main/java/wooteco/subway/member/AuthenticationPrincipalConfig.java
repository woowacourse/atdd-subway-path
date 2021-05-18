package wooteco.subway.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.member.infrastructure.TokenAuthentication;
import wooteco.subway.member.ui.AuthenticationPrincipalArgumentResolver;

import java.io.IOException;
import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {

    private final RestTemplate restTemplate;

    public AuthenticationPrincipalConfig(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {

        try {
            argumentResolvers.add(createAuthenticationPrincipalArgumentResolver());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() throws IOException {
        return new AuthenticationPrincipalArgumentResolver(tokenAuthentication());
    }

    @Bean
    public TokenAuthentication tokenAuthentication() {
        return new TokenAuthentication(restTemplate);
    }

}
