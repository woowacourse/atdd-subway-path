package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PathTest {

    private static Stream<Arguments> ageAndDistanceAndExpectedFare() {
        return Stream.of(
                Arguments.of(5, 10, 0),
                Arguments.of(6, 10, 800),
                Arguments.of(13, 10, 1070),
                Arguments.of(19, 10, 1250),

                Arguments.of(6, 50, 1200),
                Arguments.of(13, 50, 1710),
                Arguments.of(19, 50, 2050),

                Arguments.of(6, 178, 2000),
                Arguments.of(13, 178, 2990),
                Arguments.of(19, 178, 3650)
        );
    }

    @DisplayName("나이에 따른 요금 계산")
    @ParameterizedTest
    @MethodSource("ageAndDistanceAndExpectedFare")
    void 나이_별_최종_요금_계산(int age, int distance, int fare) {
        Path path = new Path(Collections.emptyList(), 0, distance);
        assertThat(path.finalFare(age)).isEqualTo(fare);
    }
}
