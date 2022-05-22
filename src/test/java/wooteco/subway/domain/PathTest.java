package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.Fixtures.HYEHWA;
import static wooteco.subway.Fixtures.SUNGSHIN;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PathTest {

    @ParameterizedTest(name = "[{index}] 거리 = {0}")
    @ValueSource(ints = {1, 5, 10})
    @DisplayName("10km 이하일 때 기본 요금을 반환한다.")
    void calculateFare(final int distance) {
        final List<Station> routeStations = List.of(new Station(HYEHWA), new Station(SUNGSHIN));
        final Path path = new Path(routeStations, distance);

        assertThat(path.calculateFare(20)).isEqualTo(1250);
    }

    @ParameterizedTest(name = "[{index}] 거리 = {0}, 요금 = {1}")
    @CsvSource({"11, 1350", "25, 1550", "34, 1750", "50, 2050"})
    @DisplayName("10km 초과 50km 이하일 때 초과 요금이 발생한다.")
    void calculateFare_Over10Under50(final int distance, final int fare) {
        final List<Station> routeStations = List.of(new Station(HYEHWA), new Station(SUNGSHIN));
        final Path path = new Path(routeStations, distance);

        assertThat(path.calculateFare(20)).isEqualTo(fare);
    }

    @ParameterizedTest(name = "[{index}] 거리 = {0}, 요금 = {1}")
    @CsvSource({"51, 2150", "66, 2250", "75, 2450", "178, 3650"})
    @DisplayName("50km 초과일 때 초과 요금이 발생한다.")
    void calculateFare_Over50(final int distance, final int fare) {
        final List<Station> routeStations = List.of(new Station(HYEHWA), new Station(SUNGSHIN));
        final Path path = new Path(routeStations, distance);

        assertThat(path.calculateFare(20)).isEqualTo(fare);
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 15, 18})
    @DisplayName("청소년(13세 이상, 19세 미만) : 운임에서 350원을 공제한 금액의 20% 할인")
    void calculateFare_teenager(final int age) {
        final List<Station> routeStations = List.of(new Station(HYEHWA), new Station(SUNGSHIN));
        final Path path = new Path(routeStations, 25);

        assertThat(path.calculateFare(age)).isEqualTo(1310);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 9, 12})
    @DisplayName("어린이(6세 이상, 13세 미만) : 운임에서 350원을 공제한 금액의 50% 할인")
    void calculateFare_child(final int age) {
        final List<Station> routeStations = List.of(new Station(HYEHWA), new Station(SUNGSHIN));
        final Path path = new Path(routeStations, 25);

        assertThat(path.calculateFare(age)).isEqualTo(950);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5})
    @DisplayName("아기(6세 미만) : 운임을 부과하지 않음")
    void calculateFare_baby(final int age) {
        final List<Station> routeStations = List.of(new Station(HYEHWA), new Station(SUNGSHIN));
        final Path path = new Path(routeStations, 25);

        assertThat(path.calculateFare(age)).isEqualTo(0);
    }
}
