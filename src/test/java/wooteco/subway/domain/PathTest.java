package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.strategy.DijkstraStrategy;
import wooteco.subway.domain.strategy.ShortestPathStrategy;
import java.util.List;
import java.util.Optional;

class PathTest {

    private Station station1;
    private Station station2;
    private Station station3;
    private Sections sections;

    @BeforeEach
    void setUp() {
        station1 = new Station(1L, "선릉역");
        station2 = new Station(2L, "역삼역");
        station3 = new Station(3L, "강남역");
        Line line = new Line(1L, "2호선", "bg-green-600", 0);
        Section section1 = new Section(1L, station1, station2, 5, line);
        Section section2 = new Section(1L, station2, station3, 5, line);
        sections = new Sections(List.of(section1, section2));
    }

    @DisplayName("경로를 생성한다.")
    @Test
    void createPath() {
        ShortestPathStrategy strategy = new DijkstraStrategy();
        final Optional<Path> path = strategy.findPath(station1, station3, sections);
        assert (path.isPresent());

        assertThat(path.get()).isNotNull();
    }

    @DisplayName("경로에 있는 정점들을 가져온다.")
    @Test
    void getVertexList() {
        ShortestPathStrategy strategy = new DijkstraStrategy();
        final Optional<Path> path = strategy.findPath(station1, station3, sections);
        assert (path.isPresent());

        assertThat(path.get().getStations()).containsAll(List.of(station1, station2, station3));
    }

    @DisplayName("경로의 최단거리를 구한다.")
    @Test
    void getShortestDistance() {
        ShortestPathStrategy strategy = new DijkstraStrategy();
        final Optional<Path> path = strategy.findPath(station1, station3, sections);
        assert (path.isPresent());

        assertThat(path.get().getDistance()).isEqualTo(10);
    }
}
