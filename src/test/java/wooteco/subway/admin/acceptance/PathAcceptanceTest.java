package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 경로를 조회한다.")
    @Test
    void findPaths() {

        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);

        LineResponse lineResponse = createLine("2호선");

        addLineStation(lineResponse.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse.getId(), stationResponse2.getId(), stationResponse3.getId());

        PathResponse response = getPath(STATION_NAME_KANGNAM, STATION_NAME_SEOLLEUNG, "DISTANCE");

        assertThat(response.getStations().get(0).getName()).isEqualTo(STATION_NAME_KANGNAM);
        assertThat(response.getStations().get(1).getName()).isEqualTo(STATION_NAME_YEOKSAM);
        assertThat(response.getStations().get(2).getName()).isEqualTo(STATION_NAME_SEOLLEUNG);
        assertThat(response.getDistance()).isEqualTo(20);
        assertThat(response.getDuration()).isEqualTo(20);
    }
}
