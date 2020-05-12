package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WholeSubwayAcceptanceTest extends AcceptanceTest {
    @LocalServerPort
    int port;

    @DisplayName("지하철 노선도 전체 정보 조회")
    @Test
    public void wholeSubway() {
        LineResponse lineResponse1 = createLine("2호선");
        StationResponse gangnamStationResponse = createStation("강남역");
        StationResponse yeokSamStationResponse = createStation("역삼역");
        StationResponse samseongStationResponse = createStation("삼성역");
        addLineStation(lineResponse1.getId(), null, gangnamStationResponse.getId());
        addLineStation(lineResponse1.getId(), gangnamStationResponse.getId(), yeokSamStationResponse.getId());
        addLineStation(lineResponse1.getId(), yeokSamStationResponse.getId(), samseongStationResponse.getId());

        LineResponse lineResponse2 = createLine("신분당선");
        StationResponse yangjaeStationResponse = createStation("양재역");
        StationResponse yangjaeForestStationResponse = createStation("양재시민의숲역");
        addLineStation(lineResponse2.getId(), null, gangnamStationResponse.getId());
        addLineStation(lineResponse2.getId(), gangnamStationResponse.getId(), yangjaeStationResponse.getId());
        addLineStation(lineResponse2.getId(), yangjaeStationResponse.getId(), yangjaeForestStationResponse.getId());

        List<LineDetailResponse> response = retrieveWholeSubway().getLineDetailResponses();
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getStations().size()).isEqualTo(3);
        assertThat(response.get(1).getStations().size()).isEqualTo(3);
    }

    private WholeSubwayResponse retrieveWholeSubway() {
        return given()
                .when().
                        get("/lines/detail").
                        then().
                        log().all().
                        extract().as(WholeSubwayResponse.class);
    }
}
