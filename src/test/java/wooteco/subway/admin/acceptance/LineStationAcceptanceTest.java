package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;

public class LineStationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @Test
    void manageLineStation() {
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);

        LineResponse lineResponse = createLine("2호선");

        addLineStation(lineResponse.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse.getId(), stationResponse2.getId(), stationResponse3.getId());

        LineDetailResponse lineDetailResponse = getLine(lineResponse.getId());
        assertThat(lineDetailResponse.getStations()).hasSize(3);

        removeLineStation(lineResponse.getId(), stationResponse2.getId());

        LineDetailResponse lineResponseAfterRemoveLineStation = getLine(lineResponse.getId());
        assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
    }

    @DisplayName("지하철 노선도 전체 정보 조회")
    @Test
    void wholeSubwayInfo() {
        LineResponse line1 = createLine("1호선");
        StationResponse sindorim = createStation("신도림");
        StationResponse guro = createStation("구로");
        StationResponse guil = createStation("구일");
        addLineStation(line1.getId(), null, sindorim.getId(), 10, 10);
        addLineStation(line1.getId(), sindorim.getId(), guro.getId(), 10, 10);
        addLineStation(line1.getId(), guro.getId(), guil.getId(), 10, 10);

        LineResponse line2 = createLine("2호선");
        StationResponse jamsil = createStation("잠실");
        StationResponse kunkok = createStation("건대");
        StationResponse hong = createStation("홍대");
        addLineStation(line2.getId(), null, jamsil.getId(), 10, 10);
        addLineStation(line2.getId(), jamsil.getId(), kunkok.getId(), 10, 10);
        addLineStation(line2.getId(), kunkok.getId(), hong.getId(), 10, 10);

        WholeSubwayResponse wholeSubwayInfo = getWholeSubwayInfo();
        List<LineDetailResponse> responses = wholeSubwayInfo.getResponses();
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getStations().size()).isEqualTo(3);
        assertThat(responses.get(1).getStations().size()).isEqualTo(3);
    }

    private WholeSubwayResponse getWholeSubwayInfo() {
        return given()
            .when()
            .get("/lines/detail")
            .then()
            .log().all()
            .extract().as(WholeSubwayResponse.class);
    }
}
