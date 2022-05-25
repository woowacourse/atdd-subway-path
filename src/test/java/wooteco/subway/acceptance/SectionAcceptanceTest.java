package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.service.dto.LineRequest;
import wooteco.subway.service.dto.SectionRequest;
import wooteco.subway.service.dto.SectionSaveRequest;
import wooteco.subway.service.dto.StationRequest;

class SectionAcceptanceTest extends AcceptanceTest {

    ExtractableResponse<Response> response_동묘;
    ExtractableResponse<Response> response_신설동;
    ExtractableResponse<Response> response_용두;

    Long 동묘_StationId;
    Long 신설동_StationId;
    Long 용두_StationId;

    ExtractableResponse<Response> 이호선_응답;

    Long 이호선_LineId;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        response_동묘 = postWithBody("/stations", new StationRequest("동묘앞역"));
        response_신설동 = postWithBody("/stations", new StationRequest("신설동역"));
        response_용두 = postWithBody("/stations", new StationRequest("용두역"));
        동묘_StationId = response_동묘.jsonPath().getLong("id");
        신설동_StationId = response_신설동.jsonPath().getLong("id");
        용두_StationId = response_용두.jsonPath().getLong("id");

        LineRequest 이호선_요청 = new LineRequest("2호선", "GREEN", 신설동_StationId, 용두_StationId, 10, 800);
        이호선_응답 = postWithBody("/lines", 이호선_요청);

        이호선_LineId = 이호선_응답.jsonPath().getLong("id");
    }

    @Test
    @DisplayName("구간 등록하기")
    void save() {
        // given
        SectionRequest request = new SectionRequest(동묘_StationId, 신설동_StationId, 5);

        // when
        ExtractableResponse<Response> response =
                postWithBody("/lines/" + 이호선_LineId + "/sections", request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("구간 삭제하기")
    void delete() {
        //given
        SectionRequest request = new SectionRequest(동묘_StationId, 신설동_StationId, 5);
        postWithBody("/lines/" + 이호선_LineId + "/sections", request);
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("stationId", 신설동_StationId)
                .when()
                .delete("/lines/" + 이호선_LineId + "/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
