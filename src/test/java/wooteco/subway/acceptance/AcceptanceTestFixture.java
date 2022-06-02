package wooteco.subway.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;

public class AcceptanceTestFixture {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static final LineRequest line1Post = new LineRequest("1호선", "blue", 1L, 2L, 10, 0);
    public static final LineRequest line2Post = new LineRequest("2호선", "green", 3L, 4L, 10, 0);
    public static final LineRequest line2Put = new LineRequest("2호선", "blue");

    public static final SectionRequest sectionBetweenOneAndTwo = new SectionRequest(1L, 2L, 10);
    public static final SectionRequest sectionBetweenTwoAndThree = new SectionRequest(2L, 3L, 10);
    public static final SectionRequest sectionBetweenThreeAndFour = new SectionRequest(3L, 4L, 10);

    public static String getLineRequest(LineRequest lineRequestPut) {
        try {
            return objectMapper.writeValueAsString(lineRequestPut);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JsonParsing 에러가 발생하였습니다");
        }
    }

    public static String getStationRequest(String name) {
        try {
            return objectMapper.writeValueAsString(new StationRequest(name));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JsonParsing 에러가 발생하였습니다");
        }
    }

    public static String getSectionRequest(SectionRequest sectionRequest) {
        try {
            return objectMapper.writeValueAsString(sectionRequest);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JsonParsing 에러가 발생하였습니다");
        }
    }

    public static <T> String convertValueAsString(T value){
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
