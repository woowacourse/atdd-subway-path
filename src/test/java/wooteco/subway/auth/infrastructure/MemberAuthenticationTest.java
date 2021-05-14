package wooteco.subway.auth.infrastructure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberAuthenticationTest {
    private static final String EMAIL = "test@test";
    private static final String PASSWORD = "123";

    @InjectMocks
    private MemberAuthentication memberAuthentication;

    @Mock
    private HttpURLConnection httpURLConnection;

    @Test
    void authenticate() throws IOException {
        //How to mick URL -> it is impossible because it is final

        given(httpURLConnection.getResponseCode())
                .willReturn(HttpURLConnection.HTTP_OK);

        given(httpURLConnection.getOutputStream())
                .willReturn(new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                    }
                });


        memberAuthentication.authenticate(EMAIL, PASSWORD);
    }
}