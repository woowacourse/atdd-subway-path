package wooteco.subway.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import wooteco.auth.dto.MemberRequest;
import wooteco.auth.dto.TokenResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@SpringBootTest
@TestPropertySource("classpath:application-test.yml")
public class AccountUtil {
    private static String accessToken;

    public static ExtractableResponse<Response> requestSignUp(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> requestLogIn(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        ExtractableResponse<Response> response = RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/login/token").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract();

        accessToken = response.as(TokenResponse.class).getAccessToken();

        return response;
    }

    public static String getAccessToken() {
        if (accessToken == null) {
            throw new NoSuchElementException("You should call login request first!");
        }
        return accessToken;
    }

}
