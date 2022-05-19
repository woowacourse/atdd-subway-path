package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;

public class SectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 구간을 추가한다.")
    @Test
    void createSection() {
        // given
        LineRequest lineRequest = new LineRequest("분당선", "bg-red-600", 1L, 2L, 10, 900);
        SectionRequest sectionRequest = new SectionRequest(1L, 3L, 5);

        ExtractableResponse<Response> response = postLines(lineRequest);

        // when
        long resultLineId = response.jsonPath().getLong("id");
        ExtractableResponse<Response> sectionResponse = postSections(sectionRequest, resultLineId);

        // then
        assertThat(sectionResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 구간을 제거한다.")
    @Test
    void deleteSection() {
        // given
        LineRequest lineRequest = new LineRequest("분당선", "bg-red-600", 1L, 2L, 10, 900);
        SectionRequest sectionRequest = new SectionRequest(1L, 3L, 5);

        ExtractableResponse<Response> response = postLines(lineRequest);
        long resultLineId = response.jsonPath().getLong("id");
        long stationId = 1L;

        postSections(sectionRequest, resultLineId);

        // when
        ExtractableResponse<Response> sectionDeleteResponse = deleteSections(resultLineId, stationId);

        // then
        assertThat(sectionDeleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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

    private ExtractableResponse<Response> postSections(SectionRequest sectionRequest, long resultLineId) {
        return RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + resultLineId + "/sections")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteSections(long resultLineId, long stationId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/" + resultLineId + "/sections?stationId=" + stationId)
                .then().log().all()
                .extract();
    }
}
