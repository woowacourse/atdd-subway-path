package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FareTest {

    @DisplayName("거리별 요금테스트")
    @ParameterizedTest
    @MethodSource("provideParameters1")
    void calculateFarePerDistance(int distance, int extraFare, int age, int expected) {
        Fare fare = new Fare();
        int actual = fare.calculateFare(distance, extraFare, age);

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideParameters1() {
        return Stream.of(
                Arguments.of(9, 0, 20, 1250),
                Arguments.of(10, 0, 20, 1250),
                Arguments.of(11, 0, 20, 1350),
                Arguments.of(15, 0, 20, 1350),
                Arguments.of(16, 0, 20, 1450),
                Arguments.of(21, 0, 20, 1550),
                Arguments.of(49, 0, 20, 2050),
                Arguments.of(50, 0, 20, 2050),
                Arguments.of(58, 0, 20, 2150),
                Arguments.of(59, 0, 20, 2250)
        );
    }

    @DisplayName("연령별 요금테스트")
    @ParameterizedTest
    @MethodSource("provideParameters2")
    void calculateFarePerAge(int distance, int extraFare, int age, int expected) {
        Fare fare = new Fare();
        int actual = fare.calculateFare(distance, extraFare, age);

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideParameters2() {
        return Stream.of(
                Arguments.of(10, 0, 5, 0),
                Arguments.of(10, 0, 6, 450),
                Arguments.of(10, 0, 12, 450),
                Arguments.of(10, 0, 13, 720),
                Arguments.of(10, 0, 19, 720),
                Arguments.of(10, 0, 20, 1250),
                Arguments.of(10, 0, 64, 1250),
                Arguments.of(10, 0, 65, 0)
        );
    }
}
