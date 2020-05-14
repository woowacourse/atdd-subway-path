package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;

public class WholeSubwayAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선도 전체 정보 조회")
    @Test
    void wholeSubway() {
        LineResponse lineResponse1 = createLine("2호선");
        StationResponse stationResponse1 = createStation("강남역");
        StationResponse stationResponse2 = createStation("역삼역");
        StationResponse stationResponse3 = createStation("삼성역");
        addLineStation(lineResponse1, stationResponse1, new StationResponse());
        addLineStation(lineResponse1, stationResponse2, stationResponse1);
        addLineStation(lineResponse1, stationResponse3, stationResponse2);

        LineResponse lineResponse2 = createLine("신분당선");
        StationResponse stationResponse5 = createStation("양재역");
        StationResponse stationResponse6 = createStation("양재시민의숲역");
        addLineStation(lineResponse2, stationResponse1, new StationResponse());
        addLineStation(lineResponse2, stationResponse5, stationResponse1);
        addLineStation(lineResponse2, stationResponse6, stationResponse5);

        List<LineDetailResponse> response = retrieveWholeSubway().getLineDetailResponse();
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getStations().size()).isEqualTo(3);
        assertThat(response.get(1).getStations().size()).isEqualTo(3);
    }

    private WholeSubwayResponse retrieveWholeSubway() {
        return given()
            .when()
            .get("/lines/detail")
            .then()
            .log().all()
            .extract().as(WholeSubwayResponse.class);
    }
}
