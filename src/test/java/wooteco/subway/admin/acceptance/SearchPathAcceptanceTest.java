package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.SearchPathResponse;
import wooteco.subway.admin.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchPathAcceptanceTest extends AcceptanceTest {
    //    Feature: 출발역과 도착역의 최단 경로를 조회하는 기능 구현
//    Scenario: 두 역의 최단 경로를 조회한다.
    @DisplayName("지하철 경로 조회 테스트")
    @Test
    void searchTest() {
//        Given 지하철역이 여러 개 추가되어있다.
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);
//        And 지하철 노선이 여러 개 추가되어있다.
        LineResponse lineResponse1 = createLine("2호선");
        LineResponse lineResponse2 = createLine("3호선");
        LineResponse lineResponse3 = createLine("4호선");
//        And 지하철 노선에 지하철역이 여러 개 추가되어있다.
        addLineStation(lineResponse1.getId(), null, stationResponse1.getId(), 1, 2);
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId(), 3, 4);
        addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId(), 5, 6);

//        When 출발역과 도착역을 입력하여 조회 요청을 한다.
//        Then 최단 거리 기준으로 경로와 기타 정보를 응답한다.
        SearchPathResponse searchPathResponse = searchPath(stationResponse1.getName(), stationResponse2.getName(), "distance");
        assertThat(searchPathResponse.getPathStationNames()).containsExactly("강남역", "역삼역");
        assertThat(searchPathResponse.getDistanceSum()).isEqualTo(3);
        assertThat(searchPathResponse.getDurationSum()).isEqualTo(4);
    }
}
