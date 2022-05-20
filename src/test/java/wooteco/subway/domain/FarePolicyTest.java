package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.domain.Fare.Fare;
import wooteco.subway.domain.Fare.FarePolicy;
import wooteco.subway.domain.distance.Kilometer;

public class FarePolicyTest {

    @ParameterizedTest(name = "{0}일 때 요금은 {1}이다")
    @MethodSource("provideDistanceAndFare")
    void getFare(Kilometer distance, Fare expected) {
        assertThat(FarePolicy.getFare(distance)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideDistanceAndFare() {
        return Stream.of(
                Arguments.of(new Kilometer(10), new Fare(1250)),
                Arguments.of(new Kilometer(11), new Fare(1350)),
                Arguments.of(new Kilometer(15), new Fare(1350)),
                Arguments.of(new Kilometer(16), new Fare(1450)),
                Arguments.of(new Kilometer(50), new Fare(2050)),
                Arguments.of(new Kilometer(51), new Fare(2150)),
                Arguments.of(new Kilometer(58), new Fare(2150)),
                Arguments.of(new Kilometer(59), new Fare(2250))
        );
    }
}
