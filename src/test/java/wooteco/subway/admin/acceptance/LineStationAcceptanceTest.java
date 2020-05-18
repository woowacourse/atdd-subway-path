package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @Test
    void manageLineStation() {
        StationResponse gangNamStation = createStation(STATION_NAME_KANGNAM);
        StationResponse yeokSamStation = createStation(STATION_NAME_YEOKSAM);
        StationResponse seolLeungStation = createStation(STATION_NAME_SEOLLEUNG);

        LineResponse line2 = createLine("2호선");

        addLineStation(line2.getId(), null, gangNamStation.getId());
        addLineStation(line2.getId(), gangNamStation.getId(), yeokSamStation.getId());
        addLineStation(line2.getId(), yeokSamStation.getId(), seolLeungStation.getId());

        LineDetailResponse line2Detail = getLine(line2.getId());
        assertThat(line2Detail.getStations()).hasSize(3);

        removeLineStation(line2.getId(), yeokSamStation.getId());

        LineDetailResponse lineResponseAfterRemoveLineStation = getLine(line2.getId());
        assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
    }
}
