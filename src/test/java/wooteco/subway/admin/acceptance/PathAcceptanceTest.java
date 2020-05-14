package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.ShortestDistanceResponse;
import wooteco.subway.admin.dto.StationResponse;

public class PathAcceptanceTest extends AcceptanceTest {
    @Test
    void searchShortestDistancePath() {
        //given 여러 개의 노선에 여러 개의 지하철역이 추가되어있다.
        LineResponse line = createLine("2호선");
        StationResponse station1 = createStation("잠실역");
        StationResponse station2 = createStation("삼성역");
        StationResponse station3 = createStation("선릉역");

        addLineStation(line.getId(), null, station1.getId(), 10, 10);
        addLineStation(line.getId(), station1.getId(), station2.getId(), 10, 10);
        addLineStation(line.getId(), station2.getId(), station3.getId(), 10, 10);
        //when 시작역과 도착역의 최단 거리 조회를 요청한다.
        ShortestDistanceResponse path = getShortestPath(station1.getName(),
            station3.getName(), "DISTANCE");
        //then 시작역과 도착역의 최단 거리를 응답 받는다.
        List<StationResponse> stations = path.getStations();
        assertThat(stations.size()).isEqualTo(3);
        assertThat(stations.get(0).getId()).isEqualTo(station1.getId());
        assertThat(stations.get(1).getId()).isEqualTo(station2.getId());
        assertThat(stations.get(2).getId()).isEqualTo(station3.getId());
        assertThat(path.getDistance()).isEqualTo(20);
        assertThat(path.getDuration()).isEqualTo(20);
    }

    @Test
    void searchShortestDurationPath() {
        //given 여러 개의 노선에 여러 개의 지하철역이 추가되어있다.
        LineResponse line = createLine("2호선");
        StationResponse station1 = createStation("잠실역");
        StationResponse station2 = createStation("삼성역");
        StationResponse station3 = createStation("선릉역");

        addLineStation(line.getId(), null, station1.getId(), 10, 5);
        addLineStation(line.getId(), station1.getId(), station2.getId(), 10, 5);
        addLineStation(line.getId(), station2.getId(), station3.getId(), 10, 5);
        //when 시작역과 도착역의 최소 시간 조회를 요청한다.
        ShortestDistanceResponse path = getShortestPath(station1.getName(),
            station3.getName(), "DURATION");
        //then 시작역과 도착역의 최소 시간을 응답 받는다.
        List<StationResponse> stations = path.getStations();
        assertThat(stations.size()).isEqualTo(3);
        assertThat(stations.get(0).getId()).isEqualTo(station1.getId());
        assertThat(stations.get(1).getId()).isEqualTo(station2.getId());
        assertThat(stations.get(2).getId()).isEqualTo(station3.getId());
        assertThat(path.getDistance()).isEqualTo(20);
        assertThat(path.getDuration()).isEqualTo(10);
    }

    private ShortestDistanceResponse getShortestPath(String source, String target,
        String pathType) {
        return given().
            queryParam("source", source).
            queryParam("target", target).
            queryParam("type", pathType).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/paths").
            then().
            log().all().
            statusCode(HttpStatus.OK.value()).
            extract().as(ShortestDistanceResponse.class);
    }
}
