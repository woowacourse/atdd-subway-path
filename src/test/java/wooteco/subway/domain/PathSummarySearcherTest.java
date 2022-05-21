package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.exception.UnreachablePathException;

class PathSummarySearcherTest {

    private List<Station> stations;
    private List<Section> sections;
    private Graph graph;
    private Map<Long, Integer> extraFares;
    private FareCalculator fareCalculator;

    @BeforeEach
    void setUp() {
        stations = List.of(
            new Station(1L, "강남역"),
            new Station(2L, "역삼역"),
            new Station(3L, "잠실역"));
        sections = List.of(
            new Section(1L, 1L, new SectionEdge(1L, 2L, 3)),
            new Section(1L, 1L, new SectionEdge(2L, 3L, 7))
        );
        graph = (source, target) -> new Path(stations, sections, 10);
        extraFares = Map.of(1L, 0);
        fareCalculator = new FareCalculator(extraFares, Passenger.valueOf(21));
    }

    @DisplayName("source와 target이 같은 경우 예외 발생")
    @Test
    void throwExceptionWhenSourceSameAsTarget() {
        assertThatThrownBy(() -> new PathSummarySearcher(graph, fareCalculator)
            .search(stations.get(0), stations.get(0)))
            .isInstanceOf(UnreachablePathException.class);
    }

    @DisplayName("Graph와 FareCalculator를 사용한 경로 조회")
    @Test
    void searchTransferLinePath() {
        PathSummary pathSummary = new PathSummarySearcher(graph, fareCalculator)
            .search(stations.get(0), stations.get(2));

        assertThat(pathSummary.getPath().getStations()).containsExactlyElementsOf(stations);
        assertThat(pathSummary.getPath().getDistance()).isEqualTo(10);
        assertThat(pathSummary.getFare()).isEqualTo(1250);
    }

    @Test
    void searchUnreachablePath() {
        Graph graph = (source, target) -> Path.EMPTY;

        assertThatThrownBy(() -> new PathSummarySearcher(graph, fareCalculator)
            .search(stations.get(0), stations.get(2)))
        .isInstanceOf(UnreachablePathException.class);
    }
}