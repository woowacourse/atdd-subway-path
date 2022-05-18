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
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;

@DisplayName("지하철 구간 관련 기능")
class SectionAcceptanceTest extends AcceptanceTest {

    private Long stationId1;
    private Long stationId2;
    private Long stationId3;
    private Long lineId;
    private SectionRequest sectionRequest;

    @BeforeEach
    void setup() {
        stationId1 = createStation(new StationRequest("아차산역"));
        stationId2 = createStation(new StationRequest("군자역"));
        stationId3 = createStation(new StationRequest("마장역"));
        lineId = createLine(new LineRequest("5호선", "bg-purple-600", stationId1, stationId2, 10));
        sectionRequest = new SectionRequest(stationId2, stationId3, 5);
    }

    @DisplayName("특정 노선의 구간을 추가한다.")
    @Test
    void createSection() {
        final ExtractableResponse<Response> response = post("/lines/" + lineId + "/sections", sectionRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("특정 노선의 구간을 삭제한다.")
    @Test
    void deleteSection() {
        post("/lines/" + lineId + "/sections", sectionRequest);

        final ExtractableResponse<Response> createResponse =
                delete("/lines/" + lineId + "/sections?stationId=" + stationId2);

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private Long createStation(final StationRequest stationRequest) {
        final ExtractableResponse<Response> response = post("/stations", stationRequest);

        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    private Long createLine(final LineRequest lineRequest) {
        final ExtractableResponse<Response> response = post("/lines", lineRequest);

        return response.jsonPath().getLong("id");
    }
}
