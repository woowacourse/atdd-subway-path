package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

//Feature: 출발역과 도착역의 최단 경로를 조회
//        Scenario: 출발역과 도착역의 최단 경로를 조회한다.
//
//        Given: 여러 개의 노선이 존재한다.
//        And 지하철역이 여러 개 추가되어있다.
//        And 지하철 구간이 여러 개 추가되어있다.
//        When: 출발역과 도착역의 최단 경로를 조회 요청한다.
//        Then: 출발역과 도착역의 최단 경로를 응답 받는다.

public class PathAcceptanceTest extends AcceptanceTest {
    @Test
    void managePath() {
        // given
        LineResponse line = createLine(LINE_NAME_2);

        StationResponse station1 = createStation(STATION_NAME_KANGNAM);
        StationResponse station2 = createStation(STATION_NAME_SEOLLEUNG);
        StationResponse station3 = createStation(STATION_NAME_YEOKSAM);

        addLineStation(line.getId(), null, station1.getId());
        addLineStation(line.getId(), station1.getId(), station2.getId(), 10, 5);
        addLineStation(line.getId(), station2.getId(), station3.getId(), 10, 5);

        // when
        PathResponse response = calculateShortestPath(station1.getId(), station3.getId());

        // then
        assertThat(response.getDistance()).isEqualTo(20);
        assertThat(response.getDuration()).isEqualTo(10);
    }
}
