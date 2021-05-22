package wooteco.subway.path.application;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class PathServiceTest {
    @InjectMocks
    private PathService pathService;

    @Mock
    private StationDao stationDao;

    @Mock
    private SectionDao sectionDao;

    private Station 흑기역;
    private Station 백기역;
    private Station 낙성대역;
    private Section 흑기백기구간;
    private Section 백기낙성대구간;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        흑기역 = new Station(1L, "흑기역");
        백기역 = new Station(2L, "백기역");
        낙성대역 = new Station(3L,"낙성대역");
        흑기백기구간 = new Section(흑기역, 백기역, 4);
        백기낙성대구간 = new Section(백기역, 낙성대역, 7);
    }

    @Test
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

        double length = dijkstraShortestPath.getPath("v3", "v1").getWeight();
        assertThat(shortestPath.size()).isEqualTo(3);
        assertThat(length).isEqualTo(4);
    }

    @Test
    void findShortestPath() {
        //given
        잔체_역을_찾음();
        잔체_구간을_찾음();

        //when
        Station source = 아이디로_역을_찾음(흑기역);
        Station target = 아이디로_역을_찾음(낙성대역);
        PathResponse shortestPath = pathService.findShortestPath(source.getId(), target.getId());

        //then
        최단_경로_포함됨(shortestPath, Arrays.asList(흑기역, 백기역, 낙성대역));
    }

    private void 최단_경로_포함됨(PathResponse shortestPath, List<Station> stations) {
        List<String> pathsName = shortestPath.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        List<String> stationsName = stations.stream()
                .map(Station::getName)
                .collect(Collectors.toList());
        assertThat(pathsName).containsExactlyInAnyOrderElementsOf(stationsName);
    }

    private void 잔체_구간을_찾음() {
        when(sectionDao.findAll()).thenReturn(Arrays.asList(흑기백기구간, 백기낙성대구간));
    }

    private void 잔체_역을_찾음() {
        when(stationDao.findAll()).thenReturn(Arrays.asList(흑기역, 백기역, 낙성대역));
    }

    private Station 아이디로_역을_찾음(Station station) {
        when(stationDao.findById(station.getId())).thenReturn(station);
        return station;
    }
}