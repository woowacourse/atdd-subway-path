package wooteco.subway.admin.acceptance;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceTest extends AcceptanceTest {
    /*
     * Scenario: 지하철 경로 조회를 한다.
     *      Given 지하철역이 여러 개 추가되어있다.
     *      And 지하철 노선이 추가되어있다.
     *      And 지하철 경로가 여러 개 추가되어있다.
     *
     *      When 출발역과 도착역을 입력하여 최단 경로를 조회하는 요청을 한다.
     *      Then 최단 거리 기준으로 경로와 기타 정보를 응답한다.
     *      And 소요시간과 거리등의 정보를 포함한다.
     *      And 최단 경로는 하나만 응답한다.
     */

    @DisplayName("지하철 최단 경로 조회")
    @Test
    void getPath() {
        // given: 지하철역, 지하철 노선, 지하철 경로 추가
        LineResponse lineResponse1 = createLine("2호선");
        LineResponse lineResponse2 = createLine("7호선");
        LineResponse lineResponse3 = createLine("분당선");

        StationResponse stationResponse = createStation("왕십리");
        StationResponse stationResponse1 = createStation("한양대");
        StationResponse stationResponse2 = createStation("뚝섬");
        StationResponse stationResponse3 = createStation("성수");
        StationResponse stationResponse4 = createStation("건대입구");
        StationResponse stationResponse5 = createStation("뚝섬유원지");
        StationResponse stationResponse6 = createStation("청담");
        StationResponse stationResponse7 = createStation("강남구청");
        StationResponse stationResponse8 = createStation("압구정로데오");
        StationResponse stationResponse9 = createStation("서울숲");

        addLineStation(lineResponse1.getId(), null, stationResponse.getId(), 0, 0);
        addLineStation(lineResponse1.getId(), stationResponse.getId(), stationResponse1.getId(), 5, 2);
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId(), 5, 2);
        addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId(), 5, 2);
        addLineStation(lineResponse1.getId(), stationResponse3.getId(), stationResponse4.getId(), 5, 2);

        addLineStation(lineResponse2.getId(), null, stationResponse4.getId(), 0, 0);
        addLineStation(lineResponse2.getId(), stationResponse4.getId(), stationResponse5.getId(), 7, 4);
        addLineStation(lineResponse2.getId(), stationResponse5.getId(), stationResponse6.getId(), 7, 4);
        addLineStation(lineResponse2.getId(), stationResponse6.getId(), stationResponse7.getId(), 7, 4);

        addLineStation(lineResponse3.getId(), null, stationResponse7.getId(), 0, 0);
        addLineStation(lineResponse3.getId(), stationResponse7.getId(), stationResponse8.getId(), 3, 1);
        addLineStation(lineResponse3.getId(), stationResponse8.getId(), stationResponse9.getId(), 3, 1);
        addLineStation(lineResponse3.getId(), stationResponse9.getId(), stationResponse.getId(), 3, 1);

        // when: 출발역과 도착역을 입력하여 최단 경로를 조회 요청
        PathResponse path = findPath(stationResponse.getId(), stationResponse7.getId());

        assertThat(path.getPath().size()).isEqualTo(4);
        assertThat(path.getPath().get(1).getName()).isEqualTo("서울숲");
        assertThat(path.getPath().get(2).getName()).isEqualTo("압구정로데오");
        assertThat(path.getPath().get(3).getName()).isEqualTo("강남구청");

        assertThat(path.getTotalDistance()).isEqualTo(9);
        assertThat(path.getTotalDuration()).isEqualTo(3);
    }

    private PathResponse findPath(Long start, Long end) {
        return given().
                when().
                get("/paths" + "?source=" + start.toString() + "&target=" + end.toString()).
                then().
                log().all().
                extract().as(PathResponse.class);
    }
}
