package wooteco.subway.auth;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.auth.infrastructure.MemberAuthentication;

@Configuration
public class AuthConfig {

    @Bean
    public MemberAuthentication memberAuthentication() {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(100)
                .setMaxConnPerRoute(50)
                .build();

        return new MemberAuthentication(httpClient);
    }

}
