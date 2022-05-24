package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.domain.fixture.StationFixture.강남;
import static wooteco.subway.domain.fixture.StationFixture.선릉;
import static wooteco.subway.domain.fixture.StationFixture.역삼;
import static wooteco.subway.domain.fixture.StationFixture.이호선;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Fare.Fare;
import wooteco.subway.domain.PathCalculator;
import wooteco.subway.domain.distance.Kilometer;

public class PathCalculatorTest {

    @Test
    @DisplayName("주어진 구간으로 출발역과 도착역 사이의 최단 경로를 구한다.")
    void calculatePath() {
        ShortestPath shortestPath = PathCalculator.getShortestPath(List.of(이호선), 강남, 선릉);
        assertAll(
                () -> assertThat(shortestPath.getDistance()).isEqualTo(Kilometer.from(20)),
                () -> assertThat(shortestPath.getStations()).containsExactly(강남, 역삼, 선릉),
                () -> assertThat(shortestPath.getFare()).isEqualTo(new Fare(500))
        );
    }
}
