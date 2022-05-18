package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.Fixtures.CENTER;
import static wooteco.subway.Fixtures.DOWN;
import static wooteco.subway.Fixtures.LEFT;
import static wooteco.subway.Fixtures.RIGHT;
import static wooteco.subway.Fixtures.UP;

import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PathFinderTest {

    @ParameterizedTest
    @MethodSource
    @DisplayName("최단 경로와 최단 거리를 구한다.")
    void findPath(final Station source, final Station target, final List<Station> route, final int distance) {
        // given
        final List<Station> stations = List.of(UP, LEFT, CENTER, RIGHT, DOWN);
        final List<Section> sections = List.of(new Section(UP, CENTER, 5), new Section(CENTER, DOWN, 6),
                new Section(LEFT, CENTER, 20), new Section(CENTER, RIGHT, 50));
        final PathFinder pathFinder = new PathFinder(stations, sections);

        // when
        final Path path = pathFinder.findPath(source, target);

        // then
        assertAll(
                () -> assertThat(path.getRouteStations()).isEqualTo(route),
                () -> assertThat(path.getDistance()).isEqualTo(distance)
        );
    }

    private static Stream<Arguments> findPath() {
        return Stream.of(
                Arguments.of(UP, DOWN, List.of(UP, CENTER, DOWN), 11),
                Arguments.of(DOWN, UP, List.of(DOWN, CENTER, UP), 11),
                Arguments.of(UP, LEFT, List.of(UP, CENTER, LEFT), 25),
                Arguments.of(UP, RIGHT, List.of(UP, CENTER, RIGHT), 55),
                Arguments.of(LEFT, RIGHT, List.of(LEFT, CENTER, RIGHT), 70),
                Arguments.of(CENTER, RIGHT, List.of(CENTER, RIGHT), 50)
        );
    }

    @Test
    @DisplayName("출발역과 도착역이 같으면 예외처리")
    void findPath_SameStation() {
        // given
        final List<Station> stations = List.of(UP, LEFT, CENTER, RIGHT, DOWN);
        final List<Section> sections = List.of(new Section(UP, CENTER, 5), new Section(CENTER, DOWN, 6),
                new Section(LEFT, CENTER, 20), new Section(CENTER, RIGHT, 50));
        final PathFinder pathFinder = new PathFinder(stations, sections);

        //when & then
        Assertions.assertThatThrownBy(() -> pathFinder.findPath(UP, UP))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역은 같을 수 없습니다.");
    }

    @Test
    @DisplayName("출발역과 도착역 사이에 경로가 없으면 예외처리")
    void findPath_noPath() {
        // given
        final List<Station> stations = List.of(UP, LEFT, RIGHT, DOWN);
        final List<Section> sections = List.of(new Section(UP, DOWN, 5), new Section(LEFT, RIGHT, 20));
        final PathFinder pathFinder = new PathFinder(stations, sections);

        //when & then
        Assertions.assertThatThrownBy(() -> pathFinder.findPath(UP, LEFT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("경로를 찾을 수 없습니다.");
    }
}
