package wooteco.subway.admin.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class AcceptanceTest {
    static final String 강남역 = "강남역";
    static final String 역삼역 = "역삼역";
    static final String 선릉역 = "선릉역";
    static final String 삼성역 = "삼성역";
    static final String 종합운동장역 = "종합운동장역";
    static final String 잠실새내역 = "잠실새내역";
    static final String 잠실역 = "잠실역";
    static final String 양재역 = "양재역";
    static final String 매봉역 = "매봉역";
    static final String 도곡역 = "도곡역";
    static final String 대치역 = "대치역";
    static final String 학여울역 = "학여울역";
    static final String 대청역 = "대청역";
    static final String 수서역 = "수서역";
    static final String 가락시장역 = "가락시장역";
    static final String 송파역 = "송파역";
    static final String 석촌역 = "석촌역";
    static final String 양재시민의숲역 = "양재시민의숲역";
    static final String 청계산입구역 = "청계산입구역";
    static final String 판교역 = "판교역";
    static final String 정자역 = "정자역";
    static final String 한티역 = "한티역";
    static final String 구룡역 = "구룡역";
    static final String 개포동역 = "개포동역";
    static final String 대모산입구역 = "대모산입구역";

    static final String LINE_NAME_2 = "2호선";
    static final String LINE_NAME_3 = "3호선";
    static final String 분당선 = "분당선";
    static final String 신분당선 = "신분당선";

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }
    <T> T post(String path, Map<String, String> params, Class<T> responseType) {
        if (responseType.equals(void.class)) {
            okStatusPost(path, params);
            return null;
        }
        return createStatusPost(path, params, responseType);
    }

    private <T> T createStatusPost(String path, Map<String, String> params, Class<T> responseType) {
        return given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post(path).
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                extract().as(responseType);
    }

    private void okStatusPost(String path, Map<String, String> params) {
        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post(path).
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    void delete(String path) {
        given().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                delete(path).
                then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    <T> T get(String path, Class<T> responseType) {
        return
                given().
                        when().
                        get(path).
                        then().
                        log().all().
                        extract().as(responseType);
    }

    void put(String path, Map<String, String> params) {
        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                put(path).
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    <T> List<T> getList(String path, Class<T> responseType) {
        return
                given().when().
                        get(path).
                        then().
                        log().all().
                        extract().
                        jsonPath().getList(".", responseType);
    }

}
