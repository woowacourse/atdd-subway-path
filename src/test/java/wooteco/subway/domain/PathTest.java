package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathTest {
    private Station 강남;
    private Station 역삼;
    private Station 선릉;
    private DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    @BeforeEach
    void setUp() {
        강남 = new Station(1L, "강남");
        역삼 = new Station(2L, "역삼");
        선릉 = new Station(3L, "선릉");
        List<Section> sections = List.of(
                new Section(강남, 역삼, Distance.fromMeter(10)),
                new Section(역삼, 선릉, Distance.fromMeter(10)),
                new Section(선릉, 강남, Distance.fromMeter(300))
        );

        shortestPath =
                ShortestPathFactory.getFrom(sections);
    }

    @Test
    @DisplayName("주어진 구간으로 최단 경로를 구한다.")
    void from() {
        Path path = Path.from(shortestPath, 선릉, 강남);

        assertThat(path.getStations()).hasSize(3);
    }

    @DisplayName("출발역과 도착역 중 하나라도 입력되지 않으면 예외가 발생한다.")
    @Test
    void from_null() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Path.from(shortestPath, null, 선릉))
                .withMessageContaining("모두 필수");

    }

    @DisplayName("출발역과 도착역이 같으면 예외가 발생한다.")
    @Test
    void from_same_source_target() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Path.from(shortestPath, 선릉, 선릉))
                .withMessageContaining("출발역과 도착역이 같아");

    }

    @Test
    @DisplayName("구간에 존재하지 않는 역일 경우 예외가 발생한다.")
    void from_no_such_station() {
        Station 망원 = new Station(4L, "망원");
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Path.from(shortestPath, 선릉, 망원))
                .withMessageContaining("존재하지 않습니다");
    }

    @Test
    @DisplayName("주어진 구간으로 최단 경로의 거리를 구한다.")
    void calculateDistance() {
        Path path = Path.from(shortestPath, 선릉, 강남);

        assertThat(path.getDistance()).isEqualTo(0.02);
    }
}
