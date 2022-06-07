package wooteco.subway.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:test-schema.sql"})
public class AcceptanceTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }


    public static <T> String convertRequest(T value){
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JsonParsing 에러가 발생하였습니다");
        }
    }

    public static ExtractableResponse<Response> insert(String body, String path) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> update(String body, String path) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> get(String path) {
        return RestAssured.given().log().all()
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(String path) {
        return RestAssured.given().log().all()
                .when()
                .delete(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(String path, Long stationId) {
        return RestAssured.given().log().all()
                .when()
                .param("stationId", stationId)
                .delete(path)
                .then().log().all()
                .extract();
    }
    public static ExtractableResponse<Response> createPathResponse(Long source, Long target, int age) {
        return RestAssured.given().log().all()
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("age", age)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();
    }
}