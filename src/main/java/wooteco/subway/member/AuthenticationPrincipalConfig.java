package wooteco.subway.member;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.member.infrastructure.TokenAuthentication;
import wooteco.subway.member.ui.AuthenticationPrincipalArgumentResolver;

import java.io.IOException;
import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {

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
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(100)
                .setMaxConnPerRoute(50)
                .build();

        return new TokenAuthentication(httpClient);
    }

}
