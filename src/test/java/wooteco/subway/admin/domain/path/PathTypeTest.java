package wooteco.subway.admin.domain.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.admin.domain.LineStation;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathTypeTest {
    @DisplayName("생성 성공")
    @ParameterizedTest
    @MethodSource("generateTypeArguments")
    void of(String input, PathType pathType) {
        assertThat(PathType.of(input)).isEqualTo(pathType);
    }

    static Stream<Arguments> generateTypeArguments() {
        return Stream.of(
                Arguments.of("DISTANCE", PathType.DISTANCE),
                Arguments.of("distance", PathType.DISTANCE),
                Arguments.of("Distance", PathType.DISTANCE),
                Arguments.of("DURATION", PathType.DURATION),
                Arguments.of("duration", PathType.DURATION),
                Arguments.of("Duration", PathType.DURATION));
    }

    @DisplayName("생성 실패")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "time"})
    void of_Fail(String input) {
        assertThatThrownBy(() -> PathType.of(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("generateTypeWeightArguments")
    void findWeight(PathType pathType, int expected) {
        LineStation lineStation = new LineStation(1L, 2L, 10, 5);
        int actual = pathType.findWeight(lineStation);
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> generateTypeWeightArguments() {
        return Stream.of(
                Arguments.of(PathType.DISTANCE, 10),
                Arguments.of(PathType.DURATION, 5));
    }

    @ParameterizedTest
    @MethodSource("generateTypeSubWeightArguments")
    void findSubWeight(PathType pathType, int expected) {
        LineStation lineStation = new LineStation(1L, 2L, 10, 5);
        int actual = pathType.findSubWeight(lineStation);
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> generateTypeSubWeightArguments() {
        return Stream.of(
                Arguments.of(PathType.DISTANCE, 5),
                Arguments.of(PathType.DURATION, 10));
    }
}
