package wooteco.subway.acceptance.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.acceptance.line.LineRequestHandler;
import wooteco.subway.acceptance.station.StationRequestHandler;

@DisplayName("지하철 경로조회 관련 기능")
class PathAcceptanceTest extends AcceptanceTest {

    private final LineRequestHandler lineRequestHandler = new LineRequestHandler();
    private final StationRequestHandler stationRequestHandler = new StationRequestHandler();
    private final PathRequestHandler pathRequestHandler = new PathRequestHandler();

    private Map<String, String> createParamsOfLine(String name, String color, long upStationId, long downStationId,
                                                   long distance) {
        return Map.of(
                "name", name,
                "color", color,
                "upStationId", String.valueOf(upStationId),
                "downStationId", String.valueOf(downStationId),
                "distance", String.valueOf(distance));
    }

    @DisplayName("지하철 경로를 조회한다.")
    @Test
    void createStation() {
        // given
        long upStationId = stationRequestHandler.extractId(
                stationRequestHandler.createStation(Map.of("name", "강남역")));
        long middleStationId = stationRequestHandler.extractId(
                stationRequestHandler.createStation(Map.of("name", "선릉역")));
        long downStationId = stationRequestHandler.extractId(
                stationRequestHandler.createStation(Map.of("name", "잠실역")));

        lineRequestHandler.createLine(createParamsOfLine("2호선", "green", upStationId, middleStationId, 5));
        lineRequestHandler.createLine(createParamsOfLine("3호선", "blue", middleStationId, downStationId, 10));

        // when
        ExtractableResponse<Response> response = pathRequestHandler.findPath(upStationId, downStationId);

        // then
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        });
    }
}
