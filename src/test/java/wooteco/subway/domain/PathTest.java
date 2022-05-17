package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

class PathTest {

    @DisplayName("다익스트라 최단경로를 생성한다.")
    @Test
    void createPath() {
        Station station1 = new Station(1L, "선릉역");
        Station station2 = new Station(2L, "역삼역");
        Station station3 = new Station(3L, "강남역");
        Line line = new Line(1L, "2호선", "bg-green-600");
        Section section1 = new Section(1L, station1, station2, 10, line.getId());
        Section section2 = new Section(1L, station2, station3, 10, line.getId());
        Sections sections = new Sections(List.of(section1, section2));

        Path path = Path.from(sections);

        assertThat(path.getDijkstraShortestPath()).isNotNull();
    }
}
