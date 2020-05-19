package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.dto.ErrorResponse;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setup() {
        createStation("강남역");
        createStation("역삼역");
        createStation("선릉역");
        createStation("양재역");
        createStation("양재시민숲역");

        createLine("신분당선");
        createLine("2호선");

        List<LineResponse> lines = getLines();
        assertThat(lines.size()).isEqualTo(2);

        LineDetailResponse lineResponse1 = getLine(lines.get(0).getId());
        LineDetailResponse lineResponse2 = getLine(lines.get(1).getId());

        List<StationResponse> stations = getStations();

        assertThat(stations.size()).isEqualTo(5);

        StationResponse stationResponse1 = stations.get(0);
        StationResponse stationResponse2 = stations.get(1);
        StationResponse stationResponse3 = stations.get(2);
        StationResponse stationResponse4 = stations.get(3);
        StationResponse stationResponse5 = stations.get(4);

        addLineStation(lineResponse1.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse4.getId());
        addLineStation(lineResponse1.getId(), stationResponse4.getId(), stationResponse5.getId());

        addLineStation(lineResponse2.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse2.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse2.getId(), stationResponse2.getId(), stationResponse3.getId());
    }

    @Test
    void findPath() {
        PathResponse pathResponse = findShortestPath(5L, 3L, PathType.DISTANCE.name());

        assertThat(pathResponse.getStations().size()).isEqualTo(5);
        assertThat(pathResponse.getDistance()).isEqualTo(40);
        assertThat(pathResponse.getDuration()).isEqualTo(40);
    }

    @DisplayName("출발역과 도착역을 같은 역으로 입력했을 경우 예외처리")
    @Test
    void findSameStartEndPathTest() {
        ErrorResponse errorResponse = findSameStartEndPath(1L, 1L, PathType.DISTANCE.name());
        final List<ErrorResponse.FieldError> errors = errorResponse.getErrors();

        assertThat(errors.get(0).getReason()).isEqualTo("출발역과 도착역이 동일합니다.");
    }

    ErrorResponse findSameStartEndPath(Long source, Long target, String type) {
        return given().
                log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
            when().
                get("/api/paths?source=" + source + "&target=" + target + "&type=" + type).
            then().
                log().all().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                extract().as(ErrorResponse.class);
    }
}
