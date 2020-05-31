package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PathTypeTest {
    @DisplayName("타입별 가중치")
    @ParameterizedTest
    @MethodSource("createPathType")
    void findPathTypeWithInvalidInput(PathEdge pathEdge, int result) {
        assertThat(pathEdge.getWeight()).isEqualTo(result);
    }

    private static Stream<Arguments> createPathType() {
        return Stream.of(
            Arguments.of(
                PathEdge.of(new LineStation(1L, 2L, 10, 15), PathType.valueOf("DISTANCE")), 10),
            Arguments.of(
                PathEdge.of(new LineStation(1L, 2L, 10, 15), PathType.valueOf("DURATION")), 15)
        );
    }
}