package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    private final List<Station> stations = List.of(new Station(1L, "강남역"), new Station(2L, "선릉역"));

    @Test
    @DisplayName("10km 이하일 때 기본 운임은 1250원이다.")
    void calcualteDefaultFare() {
        Path path = new Path(stations, 10);

        assertThat(path.calculateFare()).isEqualTo(1250);
    }

    @Test
    @DisplayName("50km 이하일 때 5km 마다 100원 추가된다.")
    void calculate50Fare() {
        Path path = new Path(stations, 50);

        assertThat(path.calculateFare()).isEqualTo(2050);
    }

    @Test
    @DisplayName("50km 초과일 때 8km 마다 100원 추가된다.")
    void calculate58Fare() {
        Path path = new Path(stations, 58);

        assertThat(path.calculateFare()).isEqualTo(2150);
    }
}
