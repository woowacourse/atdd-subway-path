package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PathTest {

    @Test
    @DisplayName("하나의 구간의 최단 경로를 확인한다.")
    void calculateShortestPath() {
        Path path = new Path(List.of(new Section(1L, 1L, 2L, 5)));
        List<Long> paths = path.calculateShortestPath(1L, 2L);

        assertThat(paths).containsExactly(1L, 2L);
    }

    @Test
    @DisplayName("여러 구간인 경우 최단 경로를 확인한다.")
    void calculateShortestTwoPath() {
        Path path = new Path(List.of(new Section(1L, 1L, 2L, 5), new Section(1L, 2L, 3L, 6),
                new Section(1L, 2L, 4L, 2), new Section(1L, 3L, 4L, 1)));
        List<Long> paths = path.calculateShortestPath(1L, 3L);

        assertThat(paths).containsExactly(1L, 2L, 4L, 3L);
    }

    @Test
    @DisplayName("하나의 구간의 최단 거리를 확인한다.")
    void calculateShortestDistance() {
        Path path = new Path(List.of(new Section(1L, 1L, 2L, 5)));
        int distance = path.calculateShortestDistance(1L, 2L);

        assertThat(distance).isEqualTo(5);
    }

    @Test
    @DisplayName("여러 구간인 경우 최단 거리를 확인한다.")
    void calculateShortestDistanceWithManySections() {
        Path path = new Path(List.of(new Section(1L, 1L, 2L, 5), new Section(1L, 2L, 3L, 6),
                new Section(1L, 2L, 4L, 2), new Section(1L, 3L, 4L, 1)));
        int distance = path.calculateShortestDistance(1L, 3L);

        assertThat(distance).isEqualTo(8);
    }

    @Test
    @DisplayName("출발점이나 도착지가 존재하지 않을 경우 예외를 발생시킨다.")
    void noExistStation() {
        Path path = new Path(List.of(new Section(1L, 1L, 3L, 5)));
        assertThatThrownBy(() -> path.calculateShortestPath(1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발지, 도착지 모두 존재해야 됩니다.");
    }

    @Test
    @DisplayName("출발지에서 도착지로 갈 수 없는 경우 예외를 발생시킨다.")
    void noReachable() {
        Path path = new Path(List.of(new Section(1L, 1L, 2L, 5), new Section(1L, 3L, 4L, 5)));
        assertThatThrownBy(() -> path.calculateShortestPath(1L, 4L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발지에서 도착지로 갈 수 없습니다.");
    }

    @ParameterizedTest
    @CsvSource({"1,2,1250", "2,3,1350", "3,4,1450", "4,5,2150"})
    @DisplayName("금액을 계산한다.")
    void calculateFareTest(Long source, Long target, int fare) {
        Path path = new Path(List.of(new Section(1L, 1L, 2L, 9), new Section(1L, 2L, 3L, 12),
                new Section(1L, 3L, 4L, 16), new Section(1L, 4L, 5L, 58)));

        assertThat(path.calculateFare(source, target)).isEqualTo(fare);
    }
}
