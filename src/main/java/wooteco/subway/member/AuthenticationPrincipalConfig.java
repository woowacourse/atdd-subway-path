package wooteco.subway.member;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.member.infrastructure.TokenAuthentication;
import wooteco.subway.member.ui.AuthenticationPrincipalArgumentResolver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {
    private static final String POST = "POST";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String AUTHENTICATION_URL = "http://localhost:8080/auth/token";

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
    public TokenAuthentication tokenAuthentication() throws IOException {
        URL url = new URL(AUTHENTICATION_URL);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod(POST);
        urlConnection.setRequestProperty(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);

        return new TokenAuthentication(urlConnection);
    }

}
