package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.domain.PathCriteria;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

/* 출발역과 도착역의 최단 경로를 조회 출발역과 도착역의 최단 경로를 조회 한다.
 * 지하철역이 여러 개 추가되어있다.
 * 지하철 노선이 여러 개 추가되어있다.
 * 지하철 노선에 지하철역이 여러 개 추가되어있다.
 * 출발역과 도착역이 추가되어있다.
 *
 * 출발역과 도착역의 최단 경로 조회 요청을 한다.

 * 출발역과 도착역의 최단 경로 정보를 응답 받는다.
 */
public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("출발역과 도착역의 최단거리 경를 조회한다.")
    @Test
    void getPath() {
        // given 지하철역이 여러 개 추가되어있다.
        StationResponse station1 = createStation("1역");
        StationResponse station2 = createStation("2역");
        StationResponse station3 = createStation("3역");
        StationResponse station4 = createStation("4역");
        StationResponse station5 = createStation("5역");

        // and 지하철 노선이 여러 개 추가되어있다.
        LineResponse line1 = createLine("1호선");
        LineResponse line2 = createLine("2호선");

        // and 지하철 노선에 지하철역이 여러 개 추가되어있다.
        addLineStation(line1.getId(), null, station1.getId(), 0, 0);
        addLineStation(line1.getId(), station1.getId(), station2.getId(), 10, 2);
        addLineStation(line1.getId(), station2.getId(), station3.getId(), 10, 0);

        addLineStation(line2.getId(), null, station2.getId(), 0, 0);
        addLineStation(line2.getId(), station2.getId(), station4.getId(), 2, 0);
        addLineStation(line2.getId(), station4.getId(), station5.getId(), 2, 2);
        addLineStation(line2.getId(), station5.getId(), station3.getId(), 2, 0);

        // and 출발역과 도착역이 추가되어있다.
        String departure = station1.getName();
        String arrival = station3.getName();

        // when 출발역과 도착역의 최단 경로 조회 요청을 한다.
        PathResponse pathResponse = retrievePath(departure, arrival, PathCriteria.DISTANCE);

        // then 출발역과 도착역의 최단 경로 정보를 응답 받는다.
        assertThat(pathResponse.getStations()).hasSize(5);
        assertThat(pathResponse.getDistance()).isEqualTo(16);
        assertThat(pathResponse.getDuration()).isEqualTo(4);
    }

    @DisplayName("출발역과 도착역의 최단시간 경로를 조회한다.")
    @Test
    void getShortestTimePath() {
        // given 지하철역이 여러 개 추가되어있다.
        StationResponse station1 = createStation("1역");
        StationResponse station2 = createStation("2역");
        StationResponse station3 = createStation("3역");
        StationResponse station4 = createStation("4역");
        StationResponse station5 = createStation("5역");

        // and 지하철 노선이 여러 개 추가되어있다.
        LineResponse line1 = createLine("1호선");
        LineResponse line2 = createLine("2호선");

        // and 지하철 노선에 지하철역이 여러 개 추가되어있다.
        addLineStation(line1.getId(), null, station1.getId(), 0, 0);
        addLineStation(line1.getId(), station1.getId(), station2.getId(), 10, 2);
        addLineStation(line1.getId(), station2.getId(), station3.getId(), 10, 0);

        addLineStation(line2.getId(), null, station2.getId(), 0, 0);
        addLineStation(line2.getId(), station2.getId(), station4.getId(), 2, 0);
        addLineStation(line2.getId(), station4.getId(), station5.getId(), 2, 2);
        addLineStation(line2.getId(), station5.getId(), station3.getId(), 2, 0);

        // and 출발역과 도착역이 추가되어있다.
        String departure = station1.getName();
        String arrival = station3.getName();

        // when 출발역과 도착역의 최단 시간 조회 요청을 한다.
        PathResponse pathResponse = retrievePath(departure, arrival, PathCriteria.DURATION);

        // then 출발역과 도착역의 최단 시간 정보를 응답 받는다.
        assertThat(pathResponse.getStations()).hasSize(3);
        assertThat(pathResponse.getDistance()).isEqualTo(20);
        assertThat(pathResponse.getDuration()).isEqualTo(2);
    }
}
