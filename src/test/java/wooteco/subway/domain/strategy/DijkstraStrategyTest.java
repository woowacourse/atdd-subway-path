package wooteco.subway.domain.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.*;
import java.util.List;

class DijkstraStrategyTest {

    @DisplayName("다익스트라 알고리즘을 이용해 최단 경로를 반환한다.")
    @Test
    void findPath() {
        Station station1 = new Station(1L, "선릉역");
        Station station2 = new Station(2L, "역삼역");
        Line line = new Line(1L, "2호선", "bg-green-600");
        Section section1 = new Section(1L, station1, station2, 5, line.getId());
        Sections sections = new Sections(List.of(section1));
        ShortestPathStrategy strategy = new DijkstraStrategy();

        assertThat(strategy.findPath(station1, station2, sections)).isInstanceOf(Path.class);
    }
}
