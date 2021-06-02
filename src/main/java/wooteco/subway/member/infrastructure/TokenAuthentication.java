package wooteco.subway.member.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestTemplate;
import wooteco.subway.member.exception.InvalidTokenException;

public class TokenAuthentication {

    @Value("${property.url.base-url}")
    private String authenticationUrl;

    public TokenAuthentication() {
    }

    public boolean validate(String token) {
        HttpStatus status = sendTokenRequestToAuthServer(token);
        if (status != HttpStatus.OK) {
            throw new InvalidTokenException();
        }
        return true;
    }

    private HttpStatus sendTokenRequestToAuthServer(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<TokenRequest> httpEntity = new HttpEntity<>(new TokenRequest(token), headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                authenticationUrl + "/auth/token",
                HttpMethod.POST,
                httpEntity,
                Void.class
            );
            return response.getStatusCode();
        } catch (Unauthorized e) {
            throw new InvalidTokenException();
        }

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


