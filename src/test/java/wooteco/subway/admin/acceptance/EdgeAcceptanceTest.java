package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class EdgeAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @Test
    void manageEdge() {
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);

        LineResponse lineResponse = createLine("2호선");

        addEdge(lineResponse.getId(), null, stationResponse1.getId());
        addEdge(lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId());
        addEdge(lineResponse.getId(), stationResponse2.getId(), stationResponse3.getId());

        LineDetailResponse lineDetailResponse = getLine(lineResponse.getId());
        assertThat(lineDetailResponse.getStations()).hasSize(3);

        removeEdge(lineResponse.getId(), stationResponse2.getId());

        LineDetailResponse lineResponseAfterRemoveEdge = getLine(lineResponse.getId());
        assertThat(lineResponseAfterRemoveEdge.getStations().size()).isEqualTo(2);
    }
}
