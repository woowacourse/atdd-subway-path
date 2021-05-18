package wooteco.subway.member.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.beans.ConstructorProperties;
import java.io.IOException;
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

    private int sendTokenRequestToAuthServer(TokenRequest tokenRequest) throws IOException {
        HttpEntity<TokenRequest> request = new HttpEntity<>(tokenRequest);

        ResponseEntity<TokenRequest> response = restTemplate.postForEntity(AUTHENTICATION_URL, request, TokenRequest.class);

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


