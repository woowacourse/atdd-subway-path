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

}
