package wooteco.subway.member.infrastructure;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.beans.ConstructorProperties;
import java.net.HttpURLConnection;

public class TokenAuthentication {
    private static final String AUTHENTICATION_URL = "http://localhost:8080/auth/token";

    private final RestTemplate restTemplate;

    public TokenAuthentication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validate(String token) {
        try {
            int statusCode = sendTokenRequestToAuthServer(new TokenRequest(token));

            return statusCode == HttpURLConnection.HTTP_OK;
        } catch (HttpClientErrorException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int sendTokenRequestToAuthServer(TokenRequest tokenRequest) {
        RequestEntity<TokenRequest> request = RequestEntity
                .post(AUTHENTICATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tokenRequest);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        return response.getStatusCode().value();
    }

    private static class TokenRequest {

        private final String token;

        @ConstructorProperties({"token"})
        public TokenRequest(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

    }

}


