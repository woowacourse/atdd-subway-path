package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static wooteco.subway.domain.path.Fixture.강남;
import static wooteco.subway.domain.path.Fixture.강남_역삼_선릉;
import static wooteco.subway.domain.path.Fixture.선릉;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Station;
import wooteco.subway.support.ShortestPath;

class PathAlgorithmTest {
    private PathAlgorithm pathAlgorithm = new ShortestPath(강남_역삼_선릉);

    @Test
    @DisplayName("주어진 구간으로 최단 경로를 구한다.")
    void from() {
        Path path = pathAlgorithm.getPath(선릉, 강남);

        assertThat(path.getStations()).hasSize(3);
    }

    @DisplayName("출발역과 도착역 중 하나라도 입력되지 않으면 예외가 발생한다.")
    @Test
    void from_null() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> pathAlgorithm.getPath(null, 선릉))
                .withMessageContaining("모두 필수");

    }

    @DisplayName("출발역과 도착역이 같으면 예외가 발생한다.")
    @Test
    void from_same_source_target() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> pathAlgorithm.getPath(선릉, 선릉))
                .withMessageContaining("출발역과 도착역이 같아");
    }

    @Test
    @DisplayName("구간에 존재하지 않는 역일 경우 예외가 발생한다.")
    void from_no_such_station() {
        Station 망원 = new Station(4L, "망원");
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> pathAlgorithm.getPath(선릉, 망원))
                .withMessageContaining("존재하지 않습니다");
    }

    @Test
    @DisplayName("주어진 구간으로 최단 경로의 거리를 구한다.")
    void calculateDistance() {
        Path path = pathAlgorithm.getPath(선릉, 강남);

        assertThat(path.getDistance()).isEqualTo(0.02);
    }
}
