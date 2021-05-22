package wooteco.subway;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CommonConfig {
    public static final int READ_TIMEOUT = 5000;
    public static final int CONNECTION_REQUEST_TIMEOUT = 3000;
    public static final int MAX_CONN_TOTAL = 100;
    public static final int MAX_CONN_PER_ROUTE = 50;

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(READ_TIMEOUT);
        factory.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);

        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(MAX_CONN_TOTAL)
                .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
                .build();

        factory.setHttpClient(httpClient);

        return new RestTemplate(factory);
    }

}
