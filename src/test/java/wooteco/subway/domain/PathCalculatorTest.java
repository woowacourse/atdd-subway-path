package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathCalculatorTest {

    private Station 강남 = new Station(1L, "강남");
    private Station 역삼 = new Station(2L, "역삼");
    private Station 선릉 = new Station(3L, "선릉");
    private List<Section> sections = List.of(
            new Section(강남, 역삼, 10),
            new Section(역삼, 선릉, 10),
            new Section(선릉, 강남, 300)
    );

    @Test
    @DisplayName("주어진 구간으로 최단 경로를 구한다.")
    void calculatePath() {
        PathCalculator pathCalculator = PathCalculator.from(sections);
        List<Station> stations = pathCalculator.calculateShortestPath(선릉, 강남);

        assertThat(stations).hasSize(3);
    }

    @Test
    @DisplayName("주어진 구간으로 최단 경로의 거리를 구한다.")
    void calculateDistance() {
        PathCalculator pathCalculator = PathCalculator.from(sections);
        double distance = pathCalculator.calculateShortestDistance(선릉, 강남);

        assertThat(distance).isEqualTo(20);
    }
}
