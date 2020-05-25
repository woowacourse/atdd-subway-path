package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @Test
    void manageLineStation() {
        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_SEOLLEUNG);

        List<StationResponse> stations = getStations();

        createLine("2호선");

        List<LineResponse> lines = getLines();
        LineResponse lineResponse = lines.get(0);

        addLineStation(lineResponse.getId(), null, stations.get(0).getId());
        addLineStation(lineResponse.getId(), stations.get(0).getId(), stations.get(1).getId());
        addLineStation(lineResponse.getId(), stations.get(1).getId(), stations.get(2).getId());

        LineDetailResponse lineDetailResponse = getLine(lineResponse.getId());
        assertThat(lineDetailResponse.getStations()).hasSize(3);

        removeLineStation(lineResponse.getId(), stations.get(1).getId());

        LineDetailResponse lineResponseAfterRemoveLineStation = getLine(lineResponse.getId());
        assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
    }
}
