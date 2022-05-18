package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;
import java.util.List;

@DisplayName("지하철 경로 관련 기능")
class PathAcceptanceTest extends AcceptanceTest {

    private StationRequest stationRequest1;
    private StationRequest stationRequest2;
    private StationRequest stationRequest3;
    private Long stationId1;
    private Long stationId2;
    private Long stationId3;
    private Long lineId;

    @BeforeEach
    void setup() {
        stationRequest1 = new StationRequest("강남역");
        stationRequest2 = new StationRequest("역삼역");
        stationRequest3 = new StationRequest("선릉역");
        ExtractableResponse<Response> stationResponse1 = createStation(stationRequest1);
        ExtractableResponse<Response> stationResponse2 = createStation(stationRequest2);
        ExtractableResponse<Response> stationResponse3 = createStation(stationRequest3);

        stationId1 = Long.parseLong(stationResponse1.header("Location").split("/")[2]);
        stationId2 = Long.parseLong(stationResponse2.header("Location").split("/")[2]);
        stationId3 = Long.parseLong(stationResponse3.header("Location").split("/")[2]);

        lineId = createLine(new LineRequest("2호선", "bg-green-600", stationId1, stationId2, 10));
        createSection(new SectionRequest(stationId2, stationId3, 5));
    }

    @DisplayName("최단 경로를 생성한다.")
    @Test
    void createPath() {
        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/paths?source=" + stationId1 + "&target=" + stationId3 + "&age=15")
                .then().log().all()
                .extract();

        // then
        final List<StationResponse> stations = response.jsonPath().getList("stations", StationResponse.class);
        final double distance = response.jsonPath().getDouble("distance");
        final int fare = response.jsonPath().getInt("fare");

        assertAll(
                () -> assertThat(stations).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(List.of(stationRequest1, stationRequest2, stationRequest3)),
                () -> assertThat(distance).isEqualTo(15),
                () -> assertThat(fare).isEqualTo(1350)
        );
    }

    private ExtractableResponse<Response> createStation(final StationRequest stationRequest) {
        return RestAssured.given().log().all()
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    private Long createLine(final LineRequest lineRequest) {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
        return response.jsonPath().getLong("id");
    }

    private void createSection(final SectionRequest sectionRequest) {
        RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }
}

