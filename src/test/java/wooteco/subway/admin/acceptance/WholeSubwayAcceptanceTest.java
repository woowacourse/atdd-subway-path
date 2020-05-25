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
        createLine(LINE_NAME_2);
        createLine(LINE_NAME_SINBUNDANG);

        List<LineResponse> lines = getLines();

        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_SEOLLEUNG);
        createStation(STATION_NAME_YANGJAE);
        createStation(STATION_NAME_YANGJAECITIZEN);

        List<StationResponse> stations = getStations();

        addLineStation(lines.get(0).getId(), null, stations.get(0).getId());
        addLineStation(lines.get(0).getId(), stations.get(0).getId(), stations.get(1).getId());
        addLineStation(lines.get(0).getId(), stations.get(1).getId(), stations.get(2).getId());


        addLineStation(lines.get(1).getId(), null, stations.get(0).getId());
        addLineStation(lines.get(1).getId(), stations.get(0).getId(), stations.get(3).getId());
        addLineStation(lines.get(1).getId(), stations.get(3).getId(), stations.get(4).getId());

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
