package wooteco.subway.auth.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestTemplate;
import wooteco.subway.auth.exception.InvalidMemberException;
import wooteco.subway.auth.exception.InvalidTokenException;

@Component
public class MemberAuthentication {

    @Value("${property.url.base-url}")
    private String authenticationUrl;

    public MemberAuthentication() {
    }

    public boolean authenticate(String email, String password) {
        HttpStatus status = sendLoginInformationToMemberServer(email, password);
        if (status != HttpStatus.OK) {
            throw new InvalidTokenException();
        }
        return true;
    }

    private HttpStatus sendLoginInformationToMemberServer(String email, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<TokenRequest> httpEntity = new HttpEntity<>(new TokenRequest(email, password), headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                authenticationUrl + "/member/authenticate",
                HttpMethod.POST,
                httpEntity,
                Void.class
            );
            return response.getStatusCode();
        } catch (Unauthorized e) {
            throw new InvalidMemberException();
        }
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
