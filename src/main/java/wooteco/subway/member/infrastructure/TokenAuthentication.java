package wooteco.subway.member.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.net.HttpURLConnection;
import org.springframework.beans.factory.annotation.Value;

public class TokenAuthentication {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${property.url.auth-token}")
    private String AUTHENTICATION_URL;

    private final HttpClient httpClient;

    public TokenAuthentication(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public boolean validate(String token) {
        try {
            int statusCode = sendTokenRequestToAuthServer(new TokenRequest(token));

            return statusCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int sendTokenRequestToAuthServer(TokenRequest tokenRequest) throws IOException {
        HttpPost httpPost = new HttpPost(AUTHENTICATION_URL);

        httpPost.setEntity(
                new StringEntity(
                        OBJECT_MAPPER.writeValueAsString(tokenRequest),
                        ContentType.APPLICATION_JSON
                )
        );

        HttpResponse execute = httpClient.execute(httpPost);

        return execute.getStatusLine().getStatusCode();
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


