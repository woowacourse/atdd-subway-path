package wooteco.subway.auth.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.net.HttpURLConnection;

public class MemberAuthentication {
    private static final String AUTHENTICATION_URL = "http://localhost:8080/members/authentication";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final HttpClient httpClient;

    public MemberAuthentication(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public boolean authenticate(String email, String password) {
        try {
            int statusCode = sendLoginInformationToMemberServer(email, password);


            return statusCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int sendLoginInformationToMemberServer(String email, String password) throws IOException {
        HttpPost httpPost = new HttpPost(AUTHENTICATION_URL);

        httpPost.setEntity(
                new StringEntity(
                        OBJECT_MAPPER.writeValueAsString(
                                new TokenRequest(email, password)
                        ), ContentType.APPLICATION_JSON
                )
        );

        HttpResponse execute = httpClient.execute(httpPost);

        return execute.getStatusLine().getStatusCode();
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
