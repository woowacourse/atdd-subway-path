package wooteco.subway.auth.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MemberAuthentication {

    @Value("${property.url.member-authenticate}")
    private String AUTHENTICATION_URL;

    public MemberAuthentication() {
    }

    public boolean authenticate(String email, String password) {
        HttpStatus status = sendLoginInformationToMemberServer(email, password);
        return status == HttpStatus.OK;
    }

    private HttpStatus sendLoginInformationToMemberServer(String email, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<TokenRequest> httpEntity = new HttpEntity<>(new TokenRequest(email, password), headers);
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
