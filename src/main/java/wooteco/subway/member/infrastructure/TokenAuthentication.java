package wooteco.subway.member.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TokenAuthentication {

    @Value("${property.url.auth-token}")
    private java.lang.String AUTHENTICATION_URL;

    public TokenAuthentication() {
    }

    public boolean validate(String token) {
        HttpStatus status = sendTokenRequestToAuthServer(token);
        return status == HttpStatus.OK;
    }

    private HttpStatus sendTokenRequestToAuthServer(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<TokenRequest> httpEntity = new HttpEntity<>(new TokenRequest(token), headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Void> response = restTemplate.exchange(
            AUTHENTICATION_URL,
            HttpMethod.POST,
            httpEntity,
            Void.class
        );
        return response.getStatusCode();

    }

    private static class TokenRequest {

        private final String token;

        public TokenRequest(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

    }

}


