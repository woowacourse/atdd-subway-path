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
        Section section1 = new Section(1L, station1, station2, 10, line.getId());
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
}
