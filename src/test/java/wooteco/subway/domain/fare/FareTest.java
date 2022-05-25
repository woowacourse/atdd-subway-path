package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.TestFixtures.STANDARD_DISTANCE;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FareTest {

    private static final int DEFAULT_EXTRA_FARE = 0;

    @DisplayName("거리, 노선 추가 요금, 나이에 따른 요금을 계산한다.")
    @ParameterizedTest
    @MethodSource("provideDistanceAndFare")
    void calculateDefaultFare(final int distance, final int expectedFare) {
        final Fare fare = Fare.of(distance, DEFAULT_EXTRA_FARE, 25);
        assertThat(fare.getFare()).isEqualTo(expectedFare);
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
