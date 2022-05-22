package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PathTest {

    private final List<Station> stations = List.of(new Station(1L, "강남역"), new Station(2L, "선릉역"));

    @Test
    @DisplayName("경로의 역들이 비어 있으면 예외가 발생한다.")
    void constructExceptionByEmptyStations() {
        List<Line> lines = List.of(new Line("1호선", "bg-green-600", 0));
        assertThatThrownBy(() -> new Path(new ArrayList<>(), 10, lines))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("경로의 거리가 0보다 작거나 같으면 예외가 발생한다.")
    void constructExceptionByLessThanZeroDistance(final int distance) {
        List<Line> lines = List.of(new Line("1호선", "bg-green-600", 0));
        assertThatThrownBy(() -> new Path(stations, distance, lines))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @ParameterizedTest
    @CsvSource(value = {"1, 1250", "10, 1250"})
    @DisplayName("10km 이하일 때 기본 운임은 1250원이다.")
    void calculateDefaultFare(final int distance, final int expected) {
        List<Line> lines = List.of(new Line("1호선", "bg-green-600", 0));
        Path path = new Path(stations, distance, lines);

        assertThat(path.calculateFinalFare(20)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"16, 1450", "20, 1450", "30, 1650", "50, 2050"})
    @DisplayName("50km 이하일 때 5km 마다 100원 추가된다.")
    void calculateFarLessThan50(final int distance, final int expected) {
        List<Line> lines = List.of(new Line("1호선", "bg-green-600", 0));
        Path path = new Path(stations, distance, lines);

        assertThat(path.calculateFinalFare(20)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"58, 2150", "60, 2250", "90, 2550"})
    @DisplayName("50km 초과일 때 8km 마다 100원 추가된다.")
    void calculateMoreThan50(final int distance, final int expected) {
        List<Line> lines = List.of(new Line("1호선", "bg-green-600", 0));
        Path path = new Path(stations, distance, lines);

        assertThat(path.calculateFinalFare(20)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"8, 2150", "12, 2250"})
    @DisplayName("환승을 하지 않고 추가요금이 있는 노선을 이용할 수 있다.")
    void calculateExtraFareWithOutTransfer() {
        List<Station> stations = List.of(new Station(1L, "강남역"), new Station(2L, "선릉역"), new Station(3L, "선정릉역"));
        List<Line> lines = List.of(new Line("1호선", "bg-red-600", 900));
        Path path = new Path(stations, 8, lines);

        assertThat(path.calculateFinalFare(20)).isEqualTo(2150);
    }

    @ParameterizedTest
    @CsvSource(value = {"8, 2150", "12, 2250"})
    @DisplayName("환승을 하여 추가요금이 있는 노선을 이용할 수 있다.")
    void calculateExtraFareWithTransfer() {
        List<Station> stations = List.of(new Station(1L, "강남역"), new Station(2L, "선릉역"), new Station(3L, "선정릉역"));
        List<Line> lines = List.of(
                new Line("1호선", "bg-red-600", 900),
                new Line("2호선", "bg-green-600", 500),
                new Line("2호선", "bg-green-600", 0));
        Path path = new Path(stations, 8, lines);

        assertThat(path.calculateFinalFare(20)).isEqualTo(2150);
    }

    @Test
    @DisplayName("영유아 요금은 0원이다.")
    void calculateInfantFare() {
        List<Station> stations = List.of(new Station(1L, "강남역"), new Station(2L, "선릉역"), new Station(3L, "선정릉역"));
        List<Line> lines = List.of(new Line("1호선", "bg-red-600", 900));
        Path path = new Path(stations, 8, lines);

        assertThat(path.calculateFinalFare(5)).isEqualTo(0);
    }
    
    @Test
    @DisplayName("어린이 요금으로 할인할 수 있다.")
    void calculateChildFare() {
        List<Station> stations = List.of(new Station(1L, "강남역"), new Station(2L, "선릉역"), new Station(3L, "선정릉역"));
        List<Line> lines = List.of(new Line("1호선", "bg-red-600", 900));
        Path path = new Path(stations, 8, lines);

        assertThat(path.calculateFinalFare(6)).isEqualTo(900);
    }

    @Test
    @DisplayName("청소년 요금으로 할인할 수 있다.")
    void calculateYouthFare() {
        List<Station> stations = List.of(new Station(1L, "강남역"), new Station(2L, "선릉역"), new Station(3L, "선정릉역"));
        List<Line> lines = List.of(new Line("1호선", "bg-red-600", 900));
        Path path = new Path(stations, 8, lines);

        assertThat(path.calculateFinalFare(13)).isEqualTo(1440);
    }
}
