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

public class TypeTest {

    static Stream<Arguments> generateTypeArguments() {
        return Stream.of(
                Arguments.of("DISTANCE", Type.DISTANCE),
                Arguments.of("distance", Type.DISTANCE),
                Arguments.of("Distance", Type.DISTANCE),
                Arguments.of("DURATION", Type.DURATION),
                Arguments.of("duration", Type.DURATION),
                Arguments.of("Duration", Type.DURATION));
    }

    static Stream<Arguments> generateTypeWeightArguments() {
        return Stream.of(
                Arguments.of(Type.DISTANCE, 10),
                Arguments.of(Type.DURATION, 5));
    }

    static Stream<Arguments> generateTypeSubWeightArguments() {
        return Stream.of(
                Arguments.of(Type.DISTANCE, 5),
                Arguments.of(Type.DURATION, 10));
    }

    @DisplayName("생성 성공")
    @ParameterizedTest
    @MethodSource("generateTypeArguments")
    void of(String input, Type type) {
        assertThat(Type.of(input)).isEqualTo(type);
    }

    @DisplayName("생성 실패")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "time"})
    void of_Fail(String input) {
        assertThatThrownBy(() -> Type.of(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("generateTypeWeightArguments")
    void findWeight(Type type, int expected) {
        LineStation lineStation = new LineStation(1L, 2L, 10, 5);
        int actual = type.findWeight(lineStation);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("generateTypeSubWeightArguments")
    void findSubWeight(Type type, int expected) {
        LineStation lineStation = new LineStation(1L, 2L, 10, 5);
        int actual = type.findSubWeight(lineStation);
        assertThat(actual).isEqualTo(expected);
    }
}
