package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathSearchResponse;
import wooteco.subway.admin.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceTest extends AcceptanceTest{
    /*
    Feature: 출발역과 도착역의 최단 경로를 조회하는 기능 구현
    Scenario: 두 역의 최단 경로를 조회한다.
    Given 지하철역이 여러 개 추가되어있다.
    And 지하철 노선이 여러 개 추가되어있다.
    And 지하철 노선에 지하철역이 여러 개 추가되어있다.
    When 출발역과 도착역을 입력하여 조회 요청을 한다.
    Then 최단 거리 기준으로 경로와 기타 정보를 응답한다.
     */

    @DisplayName("지하철 경로 찾기 테스트")
    @Test
    void searchPath() {
        //given
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);
        //and
        LineResponse lineResponse1 = createLine(LINE_NAME_SINBUNDANG);
        LineResponse lineResponse2 = createLine(LINE_NAME_BUNDANG);
        LineResponse lineResponse3 = createLine(LINE_NAME_2);
        LineResponse lineResponse4 = createLine(LINE_NAME_3);
        //and
        addLineStation(lineResponse1.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId());

        //when
        PathSearchResponse pathSearchByDistanceResponse = searchPath(stationResponse1.getName(), stationResponse3.getName(), "distance");
        PathSearchResponse pathSearchByDurationResponse = searchPath(stationResponse1.getName(), stationResponse3.getName(), "duration");

        //then
        assertThat(pathSearchByDistanceResponse.getStations().size()).isEqualTo(3);
        assertThat(pathSearchByDurationResponse.getStations().size()).isEqualTo(3);
    }
}
