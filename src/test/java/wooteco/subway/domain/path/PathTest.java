package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.station.Station;

@SuppressWarnings("NonAsciiCharacters")
class PathTest {

    private final List<Station> STATIONS = List.of(new Station(1L, "역1"),
            new Station(2L, "역2"), new Station(3L, "역3"));

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

    @DisplayName("경계값 검증")
    @Nested
    class BoundaryTest {

        @Test
        void 거리가_10인_경우_1250원_반환() {
            Path path = new Path(10, STATIONS);

            int actual = path.calculateFare();
            int expected = 1250;

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 거리가_11인_경우_1350원_반환() {
            Path path = new Path(11, STATIONS);

            int actual = path.calculateFare();
            int expected = 1350;

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 거리가_49인_경우_2050원_반환() {
            Path path = new Path(49, STATIONS);

            int actual = path.calculateFare();
            int expected = 2050;

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 거리가_50인_경우_2050원_반환() {
            Path path = new Path(50, STATIONS);

            int actual = path.calculateFare();
            int expected = 2050;

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 거리가_51인_경우_2150원_반환() {
            Path path = new Path(51, STATIONS);

            int actual = path.calculateFare();
            int expected = 2150;

            assertThat(actual).isEqualTo(expected);
        }
    }
}
