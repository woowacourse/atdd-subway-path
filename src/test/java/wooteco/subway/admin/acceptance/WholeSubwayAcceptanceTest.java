package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WholeSubwayAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선도 전체 정보 조회")
    @Test
    public void wholeSubway() {
        LineResponse lineResponse1 = createLine(LINE_NAME_2);
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);
        addLineStation(lineResponse1.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId());

        LineResponse lineResponse2 = createLine(LINE_NAME_SINBUNDANG);
        StationResponse stationResponse5 = createStation(STATION_NAME_YANGJAE);
        StationResponse stationResponse6 = createStation(STATION_NAME_YANGJAECITIZEN);
        addLineStation(lineResponse2.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse2.getId(), stationResponse1.getId(), stationResponse5.getId());
        addLineStation(lineResponse2.getId(), stationResponse5.getId(), stationResponse6.getId());

        List<LineDetailResponse> response = retrieveWholeSubway().getLineDetailResponse();
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getStations().size()).isEqualTo(3);
        assertThat(response.get(1).getStations().size()).isEqualTo(3);
    }

    private WholeSubwayResponse retrieveWholeSubway() {
        return given().
                when().
                    get("/lines/detail").
                then().
                    log().all().
                    extract().
                    as(WholeSubwayResponse.class);
    }
}
