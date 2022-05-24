package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.Fixtures.STATION_1;
import static wooteco.subway.Fixtures.STATION_2;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.domain.path.Path;

class PathTest {

    @ParameterizedTest(name = "[{index}] 거리 = {0}")
    @ValueSource(ints = {1, 5, 10})
    @DisplayName("10km 이하일 때 기본 요금을 반환한다.")
    void calculateFare(final int distance) {
        final List<Station> routeStations = List.of(new Station(STATION_1), new Station(STATION_2));
        final Path path = new Path(routeStations, distance, 0);
        assertThat(path.calculateFare(20)).isEqualTo(1250);
    }

    @ParameterizedTest(name = "[{index}] 거리 = {0}, 요금 = {1}")
    @CsvSource({"11, 1350", "25, 1550", "34, 1750", "50, 2050"})
    @DisplayName("10km 초과 50km 이하일 때 초과 요금이 발생한다.")
    void calculateFare_Over10Under50(final int distance, final int fare) {
        final List<Station> routeStations = List.of(new Station(STATION_1), new Station(STATION_2));
        final Path path = new Path(routeStations, distance, 0);
        assertThat(path.calculateFare(20)).isEqualTo(fare);
    }

    @ParameterizedTest(name = "[{index}] 거리 = {0}, 요금 = {1}")
    @CsvSource({"51, 2150", "66, 2250", "75, 2450", "178, 3650"})
    @DisplayName("50km 초과일 때 초과 요금이 발생한다.")
    void calculateFare_Over50(final int distance, final int fare) {
        final List<Station> routeStations = List.of(new Station(STATION_1), new Station(STATION_2));
        final Path path = new Path(routeStations, distance, 0);
        assertThat(path.calculateFare(20)).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource({"100, 1350", "20, 1270"})
    @DisplayName("노선의 추가 요금이 부과되는가")
    void calculateFare_extraFare(final int extraFare, final int fare) {
        final List<Station> routeStations = List.of(new Station(STATION_1), new Station(STATION_2));
        Path path = new Path(routeStations, 10, extraFare);
        assertThat(path.calculateFare(20)).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource({"12, 1013", "13, 1410", "18, 1410", "19, 1675"})
    @DisplayName("청소년 요금을 할인해준다")
    void calculateFare_Teenager(final int age, final int fare) {
        final List<Station> routeStations = List.of(new Station(STATION_1), new Station(STATION_2));
        Path path = new Path(routeStations, 25, 125);
        assertThat(path.calculateFare(age)).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource({"5, 0", "6, 1400", "12, 1400", "13, 2030"})
    @DisplayName("어린이 요금을 할인해준다")
    void calculateFare_Children(final int age, final int fare) {
        final List<Station> routeStations = List.of(new Station(STATION_1), new Station(STATION_2));
        Path path = new Path(routeStations, 45, 500);
        assertThat(path.calculateFare(age)).isEqualTo(fare);
    }
}
