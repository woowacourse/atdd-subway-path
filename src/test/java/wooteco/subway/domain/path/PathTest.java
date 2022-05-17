package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.station.Station;

@SuppressWarnings("NonAsciiCharacters")
class PathTest {

    private final List<Station> STATIONS = List.of(
            new Station(1L, "역1"),
            new Station(2L, "역2"),
            new Station(3L, "역3"));

    @Test
    void 거리가_10_이하인_경우_1250원_반환() {
        Path path = new Path(9, STATIONS);

        int actual = path.calculateFare();
        int expected = 1250;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 거리가_10초과인_경우_5km마다_100원씩_증가() {
        Path path = new Path(12, STATIONS);

        int actual = path.calculateFare();
        int expected = 1350;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 거리가_50초과인_경우_8km마다_100원씩_증가() {
        Path path = new Path(58, STATIONS);

        int actual = path.calculateFare();
        int expected = 2150;

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"9,1250", "10,1250", "11,1350", "49,2050", "50,2050", "51,2150"})
    void 경계값_유효성_검증(int input, int expected) {
        Path path = new Path(input, STATIONS);
        int actual = path.calculateFare();

        assertThat(actual).isEqualTo(expected);
    }
}
