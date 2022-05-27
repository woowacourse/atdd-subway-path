package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.path.DijkstraPathCalculator;

public class DijkstraPathCalculatorTest {

    @Test
    @DisplayName("주어진 구간으로 최단 경로를 구한다.")
    void calculatePath() {
        Station 강남 = new Station(1L, "강남");
        Station 역삼 = new Station(2L, "역삼");
        Station 선릉 = new Station(3L, "선릉");
        List<Section> sections1 = List.of(
                new Section(강남, 역삼, 10)
        );

        List<Section> sections2 = List.of(
                new Section(역삼, 선릉, 10),
                new Section(선릉, 강남, 300)
        );

        Line line1 = new Line(1L, "2호선", "green", 500, sections1);
        Line line2 = new Line(1L, "3호선", "green", 300, sections2);

        DijkstraPathCalculator dijkstraPathCalculator = DijkstraPathCalculator.from(List.of(line1, line2));
        List<Station> stations = dijkstraPathCalculator.calculateShortestPath(선릉, 강남);

        assertThat(stations).hasSize(3);
    }

    @Test
    @DisplayName("주어진 구간으로 최단 경로의 거리를 구한다.")
    void calculateDistance() {
        Station 강남 = new Station(1L, "강남");
        Station 역삼 = new Station(2L, "역삼");
        Station 선릉 = new Station(3L, "선릉");
        List<Section> sections1 = List.of(
                new Section(강남, 역삼, 10)
        );

        List<Section> sections2 = List.of(
                new Section(역삼, 선릉, 10),
                new Section(선릉, 강남, 300)
        );

        Line line1 = new Line(1L, "2호선", "green", 500, sections1);
        Line line2 = new Line(1L, "3호선", "green", 300, sections2);

        DijkstraPathCalculator dijkstraPathCalculator = DijkstraPathCalculator.from(List.of(line1, line2));
        double distance = dijkstraPathCalculator.calculateShortestDistance(선릉, 강남);

        assertThat(distance).isEqualTo(20);
    }
}
