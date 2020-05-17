package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ValidatableResponse;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class LineStationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @Test
    void manageLineStation() {
        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_SEOLLEUNG);

        createLine("2호선");

        List<LineResponse> lines = getLines();
        assertThat(lines.size()).isEqualTo(1);

        LineDetailResponse lineResponse = getLine(lines.get(0).getId());

        List<StationResponse> stations = getStations();

        assertThat(stations.size()).isEqualTo(3);

        StationResponse stationResponse1 = stations.get(0);
        StationResponse stationResponse2 = stations.get(1);
        StationResponse stationResponse3 = stations.get(2);

        addLineStation(lineResponse.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse.getId(), stationResponse2.getId(), stationResponse3.getId());

        LineDetailResponse lineDetailResponse = getLine(lineResponse.getId());
        assertThat(lineDetailResponse.getStations()).hasSize(3);

        removeLineStation(lineResponse.getId(), stations.get(2).getId());

        LineDetailResponse lineResponseAfterRemoveLineStation = getLine(lineResponse.getId());
        assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
    }
}
