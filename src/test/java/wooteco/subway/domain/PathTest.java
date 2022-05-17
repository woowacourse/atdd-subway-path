package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.List;

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
        Line line = new Line(1L, "2호선", "bg-green-600");
        Section section1 = new Section(1L, station1, station2, 5, line.getId());
        Section section2 = new Section(1L, station2, station3, 5, line.getId());
        sections = new Sections(List.of(section1, section2));
    }

    @DisplayName("다익스트라 최단경로를 생성한다.")
    @Test
    void createPath() {
        Path path = Path.from(sections);

        assertThat(path.getDijkstraShortestPath()).isNotNull();
    }

    @DisplayName("경로에 있는 정점들을 가져온다.")
    @Test
    void getVertexList() {
        Path path = Path.from(sections);
        assertThat(path.getVertexList(station1, station3)).containsAll(List.of(station1, station2, station3));
    }

    @DisplayName("경로의 최단거리를 구한다.")
    @Test
    void getShortestDistance() {
        final Path path = Path.from(sections);
        assertThat(path.getDistance(station1, station3)).isEqualTo(10);
    }

    @DisplayName("경로의 요금을 구한다.")
    @Test
    void getFare() {
        final Path path = Path.from(sections);
        assertThat(path.getFare(station1, station2)).isEqualTo(1250);
    }

    @DisplayName("이용 거리 초과 시 추가운임 부과한다.")
    @Test
    void calculateOverFare() {
        final Station station = new Station(4L, "교대역");
        final Section section = new Section(3L, station3, station, 10, 1L);
        sections.add(section);
        final Path path = Path.from(sections);

        assertThat(path.getFare(station1, station)).isEqualTo(1450);
    }
}
