package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

// @Sql("../resource/truncate.sql")
public class PathDistanceAcceptanceTest extends AcceptanceTest {

    @DisplayName("최단경로를 조회한다")
    @Test
    void findPathByDistance() {
        LineResponse line1 = createLine(LINE_NAME_1);
        StationResponse bupoeng = createStation("부평");
        StationResponse bugae = createStation("부개");
        StationResponse songnae = createStation("송내");
        StationResponse jongdong = createStation("중동");
        StationResponse bucheon = createStation("부천");

        LineResponse line2 = createLine(LINE_NAME_2);
        StationResponse sindorim = createStation("신도림");
        StationResponse monrae = createStation("문래");
        StationResponse yongdungpo = createStation("영드포구청");
        StationResponse dangsan = createStation("당산");
        StationResponse hapjung = createStation("합정");

        addLineStation(line1.getId(), null, bupoeng.getId(), 0, 10);
        addLineStation(line1.getId(), bupoeng.getId(), monrae.getId(), 10, 10);
        addLineStation(line1.getId(), monrae.getId(), bugae.getId(), 2, 10);
        addLineStation(line1.getId(), bugae.getId(), yongdungpo.getId(), 2, 10);
        addLineStation(line1.getId(), yongdungpo.getId(), songnae.getId(), 7, 10);
        addLineStation(line1.getId(), songnae.getId(), dangsan.getId(), 5, 10);
        addLineStation(line1.getId(), dangsan.getId(), jongdong.getId(), 3, 10);
        addLineStation(line1.getId(), jongdong.getId(), hapjung.getId(), 3, 10);
        addLineStation(line1.getId(), hapjung.getId(), bucheon.getId(), 10, 10);

        addLineStation(line2.getId(), null, sindorim.getId(), 0, 10);
        addLineStation(line2.getId(), sindorim.getId(), monrae.getId(), 10, 10);
        addLineStation(line2.getId(), monrae.getId(), yongdungpo.getId(), 5, 10);
        addLineStation(line2.getId(), yongdungpo.getId(), dangsan.getId(), 10, 10);
        addLineStation(line2.getId(), dangsan.getId(), hapjung.getId(), 5, 10);

        String sourceStationId = String.valueOf(sindorim.getId());
        String targetStationId = String.valueOf(dangsan.getId());
        /*  Given 노선별로 지하철역이 여러 개 추가되어있다.

        when 최단 거리를 기준으로 경로를 조회를 요청한다

        then 최단거리와 경로상에 존재하는 역을 응답받는다.
        * */
        PathResponse pathResponse = getPathByDistance(sourceStationId, targetStationId);
        assertThat(pathResponse.getStations()).containsExactly(sindorim, monrae, bugae, yongdungpo,
            dangsan);
        assertThat(pathResponse.getDistance()).isEqualTo(24);
    }

    private PathResponse getPathByDistance(String sourceStationId, String targetStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("source", sourceStationId);
        params.put("target", targetStationId);

        return given().
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/path/source/" + sourceStationId + "/target/" + targetStationId).
            then().
            log().all().
            statusCode(HttpStatus.OK.value()).
            extract().as(PathResponse.class);
    }

    @Test
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph;
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath
            = new DijkstraShortestPath(graph);
        List<String> shortestPath
            = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }
}
