package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("노선이 하나일 때 경로를 조회할 수 있다.")
    @Test
    public void findPathByOneLine() {
        // given
        final Long stationId1 = extractStationIdFromName("교대역");
        final Long stationId2 = extractStationIdFromName("강남역");
        final Long stationId3 = extractStationIdFromName("역삼역");

        final LineRequest params = new LineRequest("2호선", "bg-red-600", stationId1, stationId3, 7, 0);
        Long lineId = extractId(AcceptanceFixture.post(params, "/lines"));

        final SectionRequest sectionRequest = new SectionRequest(stationId1, stationId2, 4);

        AcceptanceFixture.post(sectionRequest, "/lines/" + lineId + "/sections");

        // when
        final ExtractableResponse<Response> response = AcceptanceFixture.get(
                "/paths?source=" + stationId1 + "&target=" + stationId3 + "&age=20");

        final PathResponse pathResponse = response.jsonPath().getObject(".", PathResponse.class);

        // then
        assertAll(
                () -> assertEquals(response.statusCode(), HttpStatus.OK.value()),
                () -> assertThat(pathResponse.getStations()).hasSize(3)
                        .extracting("id", "name")
                        .containsExactly(
                                tuple(stationId1, "교대역"),
                                tuple(stationId2, "강남역"),
                                tuple(stationId3, "역삼역")
                        ),
                () -> assertEquals(pathResponse.getDistance(), 7),
                () -> assertEquals(pathResponse.getFare(), 1250)
        );
    }

    @DisplayName("이어지지 않는 역들의 경로를 검색할 떄 예외가 발생한다.")
    @Test
    public void canNotFindPath() {
        // given
        final Long stationId1 = extractStationIdFromName("교대역");
        final Long stationId2 = extractStationIdFromName("강남역");
        final Long stationId3 = extractStationIdFromName("역삼역");
        final Long stationId4 = extractStationIdFromName("왕십리역");

        final LineRequest params = new LineRequest("2호선", "bg-red-600", stationId1, stationId2, 7, 0);
        Long line2Id = extractId(AcceptanceFixture.post(params, "/lines"));

        final LineRequest params2 = new LineRequest("수인분당선", "bg-yellow-600", stationId3, stationId4, 7, 0);
        Long 수인분당Id = extractId(AcceptanceFixture.post(params2, "/lines"));

        // when
        final ExtractableResponse<Response> response = AcceptanceFixture.get(
                "/paths?source=" + stationId1 + "&target=" + stationId3 + "&age=15");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private Long extractId(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", LineResponse.class)
                .getId();
    }

    private Long extractStationIdFromName(String name) {
        final StationRequest stationRequest = new StationRequest(name);
        final ExtractableResponse<Response> stationResponse = AcceptanceFixture.post(stationRequest, "/stations");

        return stationResponse.jsonPath()
                .getObject(".", StationResponse.class)
                .getId();
    }
}
