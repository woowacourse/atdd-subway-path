package wooteco.subway.path.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathServiceDto;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Sql("classpath:initializationForPathTest.sql")
class PathServiceTest {

    @Autowired
    private LineService lineService;
    @Autowired
    private StationService stationService;
    @Autowired
    private PathService pathService;

    @Test
    @DisplayName("라이브러리 체크 테스트")
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
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

    @Test
    @DisplayName("최단거리 반환 테스트")
    void optimalPath() {
        //given
        long sourceStationId = 1L;
        String sourceStationName = "구로디지털단지역";
        long targetStationId = 5L;
        String targetStationName = "가산디지털단지역";
        PathServiceDto pathServiceDto = new PathServiceDto(sourceStationId, targetStationId);

        //when
        PathResponse pathResponse = pathService.optimalPath(pathServiceDto);

        //then
        assertThat(pathResponse.getDistance()).isEqualTo(1);
        assertThat(pathResponse.getStations().get(0).getName()).isEqualTo(sourceStationName);
        assertThat(pathResponse.getStations().get(1).getName()).isEqualTo(targetStationName);
    }
}