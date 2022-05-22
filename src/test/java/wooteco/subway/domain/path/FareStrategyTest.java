package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.TestFixtures.STANDARD_DISTANCE;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FareStrategyTest {

    private FareStrategy fareStrategy = new FareStrategy();

    @DisplayName("거리 당 요금을 계산한다.")
    @ParameterizedTest
    @MethodSource("provideDistanceAndFare")
    void calculateDefaultFare(final int distance, final int expectedFare) {
        assertThat(fareStrategy.calculateFare(distance)).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> provideDistanceAndFare() {
        return Stream.of(
                Arguments.of(STANDARD_DISTANCE, 1250),
                Arguments.of(STANDARD_DISTANCE + 1, 1350),
                Arguments.of(STANDARD_DISTANCE + 6, 1450),
                Arguments.of(51, 2150),
                Arguments.of(59, 2250)
        );
    }
}
