package wooteco.subway.auth.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import wooteco.subway.auth.dto.TokenRequest;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class MemberAuthentication {

    private static final String AUTHENTICATION_URL = "http://localhost:8080/members/authentication";
    private static final String POST = "POST";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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

    public boolean authenticate(String email, String password) {
        try {
            URL url = new URL(AUTHENTICATION_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            initializeHttpRequest(new TokenRequest(email, password), urlConnection);

            return urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeHttpRequest(TokenRequest tokenRequest, HttpURLConnection urlConnection) throws IOException {
        urlConnection.setRequestMethod(POST);
        urlConnection.setRequestProperty(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
        outputStreamWriter.write(OBJECT_MAPPER.writeValueAsString(tokenRequest));
        outputStreamWriter.flush();
    }

}
