package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

public class PathDistanceAcceptanceTest extends AcceptanceTest {
    private StationResponse jamsil;
    private StationResponse jamsilsaenae;
    private StationResponse playgound;
    private StationResponse samjun;
    private StationResponse sukchongobun;
    private StationResponse sukchon;

    private LineResponse line2;
    private LineResponse line8;
    private LineResponse line9;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        jamsil = createStation("잠실");
        jamsilsaenae = createStation("잠실새내");
        playgound = createStation("종합운동장");
        samjun = createStation("삼전");
        sukchongobun = createStation("석촌고분");
        sukchon = createStation("석촌");

        line2 = createLine("2호선");
        line8 = createLine("8호선");
        line9 = createLine("9호선");

        addLineStation(line2.getId(), null, jamsil.getId(), 0, 0);
        addLineStation(line2.getId(), jamsil.getId(), jamsilsaenae.getId(), 10, 1);
        addLineStation(line2.getId(), jamsilsaenae.getId(), playgound.getId(), 10, 1);

        addLineStation(line9.getId(), null, playgound.getId(), 0, 0);
        addLineStation(line9.getId(), playgound.getId(), samjun.getId(), 10, 1);
        addLineStation(line9.getId(), samjun.getId(), sukchongobun.getId(), 1, 10);
        addLineStation(line9.getId(), sukchongobun.getId(), sukchon.getId(), 1, 10);

        addLineStation(line8.getId(), null, jamsil.getId(), 0, 0);
        addLineStation(line8.getId(), jamsil.getId(), sukchon.getId(), 1, 10);
    }

    @DisplayName("최단경로를 조회한다")
    @Test
    void findPathByDistance() {
        //when
        PathResponse path = getPath(jamsil.getName(), samjun.getName(), "distance");

        //then
        assertThat(path.getStations()).hasSize(4);
        assertThat(path.getStations()).extracting(StationResponse::getName)
            .containsExactly("잠실", "석촌", "석촌고분", "삼전");
        assertThat(path.getDistance()).isEqualTo(3);
        assertThat(path.getDuration()).isEqualTo(30);
    }

    @DisplayName("경로를 최단거리와 최소시간으로 조회단다")
    @Test
    void findPathByDistanceAndDuration() {
        //when
        PathResponse pathByDistance = getPath(jamsil.getName(), sukchongobun.getName(), "distance");

        //then
        assertThat(pathByDistance.getStations()).hasSize(3);
        assertThat(pathByDistance.getStations()).extracting(StationResponse::getName)
            .containsExactly("잠실", "석촌", "석촌고분");
        assertThat(pathByDistance.getDistance()).isEqualTo(2);
        assertThat(pathByDistance.getDuration()).isEqualTo(20);

        //when
        PathResponse pathByDuration = getPath(jamsil.getName(), sukchongobun.getName(), "duration");

        //then
        assertThat(pathByDuration.getStations()).hasSize(5);
        assertThat(pathByDuration.getStations()).extracting(StationResponse::getName)
            .containsExactly("잠실", "잠실새내", "종합운동장", "삼전", "석촌고분");
        assertThat(pathByDuration.getDistance()).isEqualTo(31);
        assertThat(pathByDuration.getDuration()).isEqualTo(13);
    }

    private PathResponse getPath(String source, String target, String criteriaType) {
        return given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/paths?source=" + source + "&target=" + target + "&criteria=" + criteriaType).
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
