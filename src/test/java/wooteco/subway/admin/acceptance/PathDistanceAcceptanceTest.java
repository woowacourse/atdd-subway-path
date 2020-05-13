package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

// @Sql("../resource/truncate.sql")
public class PathDistanceAcceptanceTest extends AcceptanceTest{

    private static final String LINE_NAME_1 = "1호선";

    @DisplayName("최단경로를 조회한다")
    @Test
    void findPathByDistance() {
        /*  Given 노선별로 지하철역이 여러 개 추가되어있다.

        when 최단 거리를 기준으로 경로를 조회를 요청한다

        then 최단거리와 경로상에 존재하는 역을 응답받는다.
        * */
        LineResponse line1 = createLine(LINE_NAME_1);
        StationResponse bupoeng = createStation("부평");
        StationResponse bugae = createStation("부개");
        StationResponse songnae = createStation("송내");
        StationResponse jongdong = createStation("중동");
        StationResponse bucheon = createStation("부천");

        LineResponse line2 = createLine(LINE_NAME_2);
        StationResponse sindorim = createStation("신도룀");
        StationResponse monrae = createStation("문래");
        StationResponse yongdungpo = createStation("영드포구청");
        StationResponse dangsan = createStation("당산");
        StationResponse hapjung = createStation("합정");
        StationResponse hongdae = createStation("홍대입구");

        addLineStation(line1.getId(),null,bupoeng.getId(),0,10);
        addLineStation(line1.getId(),bupoeng.getId(),sindorim.getId(),10,10);
        addLineStation(line1.getId(),sindorim.getId(),bugae.getId(),10,10);
        addLineStation(line1.getId(),bugae.getId(),monrae.getId(),10,10);
        addLineStation(line1.getId(),monrae.getId(),songnae.getId(),10,10);
        addLineStation(line1.getId(),songnae.getId(),yongdungpo.getId(),10,10);
        addLineStation(line1.getId(),yongdungpo.getId(),jongdong.getId(),10,10);
        addLineStation(line1.getId(),jongdong.getId(),dangsan.getId(),10,10);
        addLineStation(line1.getId(),dangsan.getId(),bucheon.getId(),10,10);
        addLineStation(line1.getId(),bucheon.getId(),hapjung.getId(),10,10);




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
