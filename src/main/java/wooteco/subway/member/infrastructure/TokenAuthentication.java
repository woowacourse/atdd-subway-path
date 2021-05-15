package wooteco.subway.member.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class TokenAuthentication {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final HttpURLConnection httpURLConnection;

    public TokenAuthentication(HttpURLConnection httpURLConnection) {
        this.httpURLConnection = httpURLConnection;
    }

    public boolean validate(String token) {
        try {
            sendTokenRequestToAuthServer(new TokenRequest(token), httpURLConnection);

            return httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendTokenRequestToAuthServer(TokenRequest tokenRequest, HttpURLConnection urlConnection) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
        outputStreamWriter.write(OBJECT_MAPPER.writeValueAsString(tokenRequest));
        outputStreamWriter.flush();
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


