package wooteco.subway.auth.infrastructure;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;

public class MemberAuthentication {
    private static final String AUTHENTICATION_URL = "http://localhost:8080/members/authentication";

    private final RestTemplate restTemplate;

    public MemberAuthentication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean authenticate(String email, String password) {
        try {
            int statusCode = sendLoginInformationToMemberServer(email, password);

            return statusCode == HttpURLConnection.HTTP_OK;
        } catch (HttpClientErrorException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int sendLoginInformationToMemberServer(String email, String password) throws IOException {
        RequestEntity<TokenRequest> request = RequestEntity
                .post(AUTHENTICATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TokenRequest(email, password));

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        return response.getStatusCode().value();
    }

    private static class TokenRequest {
        private final String email;
        private final String password;

        public TokenRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

    }

}
