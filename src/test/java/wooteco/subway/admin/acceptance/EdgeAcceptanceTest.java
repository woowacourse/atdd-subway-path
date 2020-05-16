package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

public class EdgeAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @Test
    void manageLineStation() {
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);

        LineResponse lineResponse = createLine("2호선");

        addLineStation(lineResponse.getId(), null, stationResponse3.getId());
        addLineStation(lineResponse.getId(), stationResponse3.getId(), stationResponse1.getId());
        addLineStation(lineResponse.getId(), stationResponse3.getId(), stationResponse2.getId());

        LineDetailResponse lineDetailResponse = getLine(lineResponse.getId());
        assertThat(lineDetailResponse.getStations()).hasSize(3);
        assertThat(lineDetailResponse.getStations().get(0)).isEqualTo(stationResponse3);
        assertThat(lineDetailResponse.getStations().get(1)).isEqualTo(stationResponse2);
        assertThat(lineDetailResponse.getStations().get(2)).isEqualTo(stationResponse1);

        removeLineStation(lineResponse.getId(), stationResponse2.getId());

        LineDetailResponse lineResponseAfterRemoveLineStation = getLine(lineResponse.getId());
        assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
    }

}
