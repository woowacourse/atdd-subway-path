package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;


import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathCalculatorTest {
    private Station 강남;
    private Station 역삼;
    private Station 선릉;
    private PathCalculator pathCalculator;

    @BeforeEach
    void setUp() {
        강남 = new Station(1L, "강남");
        역삼 = new Station(2L, "역삼");
        선릉 = new Station(3L, "선릉");
        List<Station> stations = List.of(강남, 역삼, 선릉);
        List<Section> sections = List.of(
                new Section(강남, 역삼, 10),
                new Section(역삼, 선릉, 10),
                new Section(선릉, 강남, 300)
        );

        pathCalculator = PathCalculator.from(stations, sections);
    }

    @Test
    @DisplayName("주어진 구간으로 최단 경로를 구한다.")
    void calculatePath() {
        List<Station> path = pathCalculator.calculateShortestPath(선릉, 강남);

        assertThat(path).hasSize(3);
    }

    @Test
    @DisplayName("주어진 구간으로 최단 경로의 거리를 구한다.")
    void calculateDistance() {
        double distance = pathCalculator.calculateShortestDistance(선릉, 강남);

        assertThat(distance).isEqualTo(20);
    }
}
