package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.LineResponse;
import wooteco.subway.admin.dto.response.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @Test
    void manageLineStation() {
        // given : 역과 노선이 존재한다.
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);

        LineResponse lineResponse = createLine("2호선");

        // when : 노선에 구간을 추가한다.
        addLineStation(lineResponse.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse.getId(), stationResponse2.getId(), stationResponse3.getId());

        // then : 구간이 추가되었는지 확인한다.
        LineDetailResponse lineDetailResponse = getLine(lineResponse.getId());
        assertThat(lineDetailResponse.getStations()).hasSize(3);

        // when : 구간을 삭제한다.
        removeLineStation(lineResponse.getId(), stationResponse2.getId());

        //then : 구간이 삭제되었는지 확인한다.
        LineDetailResponse lineResponseAfterRemoveLineStation = getLine(lineResponse.getId());
        assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
    }
}
