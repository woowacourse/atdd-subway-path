package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

public class PathAcceptanceTest extends AcceptanceTest {
    /*
        given 역이 여러개 추가되어있다
        and 라인이 여러개 추가되어있다.
        and 역이 라인에 등록되어있다.

        when 출발역과 도착역을 입력받는다.
        then 최단 거리와 소요시간, 거리를 응답한다.
     */
    @DisplayName("최단 거리를 구한다")
    @Test
    void shortestDistancePath() {
        // Given
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);
        StationResponse stationResponseStart = createStation(STATION_NAME_START);
        StationResponse stationResponseEnd = createStation(STATION_NAME_END);

        // And
        LineResponse lineResponse1 = createLine(LINE_NAME_2);
        LineResponse lineResponse2 = createLine(LINE_NAME_3);
        LineResponse lineResponse3 = createLine(LINE_NAME_BUNDANG);

        // And
        addLineStation(lineResponse1.getId(), null, stationResponseStart.getId(), 0, 0);
        addLineStation(lineResponse1.getId(), stationResponseStart.getId(), stationResponse1.getId(), 10, 10);
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId(), 10, 10);
        addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponseEnd.getId(), 10, 10);
        addLineStation(lineResponse2.getId(), null, stationResponseStart.getId(), 0, 0);
        addLineStation(lineResponse2.getId(), stationResponseStart.getId(), stationResponse3.getId(), 10, 10);
        addLineStation(lineResponse3.getId(), null, stationResponse3.getId(), 0, 0);
        addLineStation(lineResponse3.getId(), stationResponse3.getId(), stationResponseEnd.getId(), 10, 10);

        // When
        PathResponse pathResponse = getShortestPath(STATION_NAME_START, STATION_NAME_END, PathType.DISTANCE.name());

        // Then
        List<StationResponse> stations = pathResponse.getStations();
        assertThat(stations.size()).isEqualTo(3);
        assertThat(stations.get(0).getId()).isEqualTo(stationResponseStart.getId());
        assertThat(stations.get(1).getId()).isEqualTo(stationResponse3.getId());
        assertThat(stations.get(2).getId()).isEqualTo(stationResponseEnd.getId());
        assertThat(pathResponse.getDistance()).isEqualTo(20);
        assertThat(pathResponse.getDuration()).isEqualTo(20);
    }

    @DisplayName("최소 시간을 구한다")
    @Test
    void shortestDurationPath() {
        // Given
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);
        StationResponse stationResponseStart = createStation(STATION_NAME_START);
        StationResponse stationResponseEnd = createStation(STATION_NAME_END);

        // And
        LineResponse lineResponse1 = createLine(LINE_NAME_2);
        LineResponse lineResponse2 = createLine(LINE_NAME_3);
        LineResponse lineResponse3 = createLine(LINE_NAME_BUNDANG);

        // And
        addLineStation(lineResponse1.getId(), null, stationResponseStart.getId(), 0, 0);
        addLineStation(lineResponse1.getId(), stationResponseStart.getId(), stationResponse1.getId(), 10, 1);
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId(), 10, 1);
        addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponseEnd.getId(), 10, 1);

        addLineStation(lineResponse2.getId(), null, stationResponseStart.getId(), 0, 0);
        addLineStation(lineResponse2.getId(), stationResponseStart.getId(), stationResponse3.getId(), 10, 10);

        addLineStation(lineResponse3.getId(), null, stationResponse3.getId(), 0, 0);
        addLineStation(lineResponse3.getId(), stationResponse3.getId(), stationResponseEnd.getId(), 10, 10);

        // When
        PathResponse pathResponse = getShortestPath(STATION_NAME_START, STATION_NAME_END, PathType.DURATION.name());

        // Then
        List<StationResponse> stations = pathResponse.getStations();
        assertThat(stations.size()).isEqualTo(4);
        assertThat(stations.get(0).getId()).isEqualTo(stationResponseStart.getId());
        assertThat(stations.get(1).getId()).isEqualTo(stationResponse1.getId());
        assertThat(stations.get(2).getId()).isEqualTo(stationResponse2.getId());
        assertThat(stations.get(3).getId()).isEqualTo(stationResponseEnd.getId());
        assertThat(pathResponse.getDistance()).isEqualTo(30);
        assertThat(pathResponse.getDuration()).isEqualTo(3);
    }
}
