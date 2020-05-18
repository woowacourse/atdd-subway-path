package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setup() {
        StationResponse gangNamStation = createStation("강남역");
        StationResponse yeokSamStation = createStation("역삼역");
        StationResponse seolLeungStation = createStation("선릉역");
        StationResponse yangJaeStation = createStation("양재역");
        StationResponse yangJaeCitizensForestStation = createStation("양재시민숲역");

        LineResponse shinBunDangLine = createLine("신분당선");
        LineResponse line2 = createLine("2호선");

        addLineStation(shinBunDangLine.getId(), null, gangNamStation.getId());
        addLineStation(shinBunDangLine.getId(), gangNamStation.getId(), yangJaeStation.getId());
        addLineStation(shinBunDangLine.getId(), yangJaeStation.getId(), yangJaeCitizensForestStation.getId());

        addLineStation(line2.getId(), null, gangNamStation.getId());
        addLineStation(line2.getId(), gangNamStation.getId(), yeokSamStation.getId());
        addLineStation(line2.getId(), yeokSamStation.getId(), seolLeungStation.getId());
    }

    @Test
    void findPath() {
        PathResponse pathResponse = findShortestPath(5L, 3L, "DISTANCE");

        assertThat(pathResponse.getStations().size()).isEqualTo(5);
        assertThat(pathResponse.getDistance()).isEqualTo(40);
        assertThat(pathResponse.getDuration()).isEqualTo(40);
    }

    PathResponse findShortestPath(Long source, Long target, String type) {
        return given().
            log().all().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/api/paths?source=" + source + "&target=" + target + "&type=" + type).
            then().
            log().all().
            statusCode(HttpStatus.OK.value()).
            extract().as(PathResponse.class);
    }
}
