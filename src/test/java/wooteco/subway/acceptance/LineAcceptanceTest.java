package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;

public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLines() {
        // given
        LineRequest lineRequest = new LineRequest("분당선", "bg-red-600", 1L, 2L, 10, 900);

        // when
        ExtractableResponse<Response> response = postLines(lineRequest);

        // then
        assertAll(() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank(),
                () -> assertThat(response.body().jsonPath().getString("name")).isEqualTo("분당선"),
                () -> assertThat(response.body().jsonPath().getString("color")).isEqualTo("bg-red-600"));
    }

    @DisplayName("지하철 노선 전체를 조회한다.")
    @Test
    void getLines() {
        // given
        LineRequest firstLineRequest = new LineRequest("분당선", "bg-red-600", 1L, 2L, 10, 900);
        LineRequest secondLineRequest = new LineRequest("분당4선", "bg-green-600", 1L, 3L, 10, 900);

        postLines(firstLineRequest);
        postLines(secondLineRequest);

        // when
        ExtractableResponse<Response> response = getLines("/lines");

        // then
        assertAll(() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getList(".", LineResponse.class)).hasSize(2));
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        LineRequest lineRequest = new LineRequest("분당선", "bg-red-600", 1L, 2L, 10, 900);
        ExtractableResponse<Response> response = postLines(lineRequest);

        // when
        long resultLineId = response.jsonPath().getLong("id");
        ExtractableResponse<Response> newResponse = getLines("/lines/" + resultLineId);

        // then
        assertAll(() -> assertThat(newResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(resultLineId).isEqualTo(newResponse.jsonPath().getLong("id")),
                () -> assertThat(newResponse.body().jsonPath().getString("name")).isEqualTo("분당선"),
                () -> assertThat(newResponse.body().jsonPath().getString("color")).isEqualTo("bg-red-600"));
    }

    @DisplayName("지하철 노선 수정")
    @Test
    void modifyLine() {
        // given
        LineRequest lineRequest = new LineRequest("분당선", "bg-red-600", 1L, 2L, 10, 900);
        ExtractableResponse<Response> response = postLines(lineRequest);

        LineRequest newLineRequest = new LineRequest("4호선", "bg-red-600", 1L, 2L, 10, 900);

        // when
        long resultLineId = response.jsonPath().getLong("id");
        ExtractableResponse<Response> newResponse = putLines(newLineRequest, "/lines/" + resultLineId);

        // then
        assertThat(newResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        LineRequest lineRequest = new LineRequest("분당선", "bg-red-600", 1L, 2L, 10, 900);
        ExtractableResponse<Response> response = postLines(lineRequest);

        // when
        long resultLineId = response.jsonPath().getLong("id");
        ExtractableResponse<Response> newResponse = deleteLines("/lines/" + resultLineId);

        // then
        assertThat(newResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> getLines(String path) {
        return RestAssured.given().log().all()
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> postLines(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> putLines(LineRequest newLineRequest, String path) {
        return RestAssured.given().log()
                .all()
                .body(newLineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(path)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteLines(String path) {
        return RestAssured.given().log()
                .all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(path)
                .then().log().all()
                .extract();
    }
}
