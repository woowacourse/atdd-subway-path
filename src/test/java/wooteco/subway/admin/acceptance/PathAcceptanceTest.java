package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Feature: 지하철 경로 검색 실행
 *
 *   Scenario: 지하철 경로 검색 및 거리 조회를 한다.
 *     Given 지하철역이 여러 개 추가되어있다.
 *     And 지하철 노선이 여러 개 추가되어있다.
 *     And 지하철 노선에 지하철역이 여러 개 추가되어있다.
 *
 *     When 지하철 경로 검색 요청을 한다.
 *
 *     Then 두 역 사이의 거리, 시간을 반환한다.
 *     And 경로에 있는 역들의 목록을 반환한다.
 */
public class PathAcceptanceTest extends AcceptanceTest {
    @DisplayName("경로 검색을 실행하고 올바른 결과가 나오는지 테스트")
    @Test
    void findPath() {
        //given
        LineResponse lineResponse1 = createLine("1호선");
        StationResponse stationResponse1 = createStation("강남역");
        StationResponse stationResponse2 = createStation("역삼역");
        StationResponse stationResponse3 = createStation("선릉역");
        addLineStation(lineResponse1.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId());
        //and
        LineResponse lineResponse2 = createLine("2호선");
        StationResponse stationResponse4 = createStation("양재역");
        StationResponse stationResponse5 = createStation("양재시민의숲역");
        addLineStation(lineResponse2.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse2.getId(), stationResponse1.getId(), stationResponse4.getId());
        addLineStation(lineResponse2.getId(), stationResponse4.getId(), stationResponse5.getId());

        //when
        PathResponse pathResponse = calculatePath("양재시문의숲역","선릉역");

        //then
        assertThat(pathResponse.getDistance()).isEqualTo(40);
        assertThat(pathResponse.getDuration()).isEqualTo(40);
        //and
        List<Station> stations = pathResponse.getStations();
        assertThat(stations.get(0).getName()).isEqualTo("양재시민의숲역");
        assertThat(stations.get(1).getName()).isEqualTo("양재역");
        assertThat(stations.get(2).getName()).isEqualTo("강남역");
        assertThat(stations.get(3).getName()).isEqualTo("역삼역");
        assertThat(stations.get(4).getName()).isEqualTo("선릉역");
    }
}
