package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.LineResponse;
import wooteco.subway.admin.dto.response.StationResponse;
import wooteco.subway.admin.dto.response.WholeSubwayResponse;

public class LineStationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @Test
    void manageLineStation() {
        Long station1Id = createStation(STATION_NAME_KANGNAM);
        Long station2Id = createStation(STATION_NAME_YEOKSAM);
        Long station3Id = createStation(STATION_NAME_SEOLLEUNG);

        LineResponse lineResponse = createLine("2호선");

        addLineStation(lineResponse.getId(), null, station1Id);
        addLineStation(lineResponse.getId(), station1Id, station2Id);
        addLineStation(lineResponse.getId(), station2Id, station3Id);

        LineDetailResponse lineDetailResponse = getLine(lineResponse.getId());
        assertThat(lineDetailResponse.getStations()).hasSize(3);

        removeLineStation(lineResponse.getId(), station2Id);

        LineDetailResponse lineResponseAfterRemoveLineStation = getLine(lineResponse.getId());
        assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
    }

    @DisplayName("지하철 노선도 전체 정보 조회")
    @Test
    public void wholeSubway() {
        LineResponse lineResponse1 = createLine("2호선");
        Long stationResponse1 = createStation("강남역");
        Long stationResponse2 = createStation("역삼역");
        Long stationResponse3 = createStation("삼성역");
        addLineStation(lineResponse1.getId(), null, stationResponse1);
        addLineStation(lineResponse1.getId(), stationResponse1, stationResponse2);
        addLineStation(lineResponse1.getId(), stationResponse2, stationResponse3);

        LineResponse lineResponse2 = createLine("신분당선");
        Long stationResponse4 = createStation("잠실역");
        Long stationResponse5 = createStation("양재역");
        Long stationResponse6 = createStation("양재시민의숲역");
        addLineStation(lineResponse2.getId(), null, stationResponse4);
        addLineStation(lineResponse2.getId(), stationResponse4, stationResponse5);
        addLineStation(lineResponse2.getId(), stationResponse5, stationResponse6);

        List<LineDetailResponse> responses = retrieveWholeSubway().getLineDetailResponse();
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getStations().size()).isEqualTo(3);
        assertThat(responses.get(1).getStations().size()).isEqualTo(3);
    }

    private WholeSubwayResponse retrieveWholeSubway() {
        return given().
            when().
            get("/paths/lines/detail").
            then().
            extract().
            body().
            as(WholeSubwayResponse.class);
    }
}
