package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.Fixtures.CENTER;
import static wooteco.subway.Fixtures.DOWN;
import static wooteco.subway.Fixtures.GREEN;
import static wooteco.subway.Fixtures.LEFT;
import static wooteco.subway.Fixtures.LINE_2;
import static wooteco.subway.Fixtures.LINE_4;
import static wooteco.subway.Fixtures.RIGHT;
import static wooteco.subway.Fixtures.SKY_BLUE;
import static wooteco.subway.Fixtures.UP;

import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

public class PathFinderTest {

    @ParameterizedTest
    @MethodSource("findPath")
    @DisplayName("최단 경로와 최단 거리를 구한다.")
    void findPath(final Station source, final Station target, final List<Station> route, final int distance) {
        // given
        final List<Line> lines = List.of(
                new Line(LINE_4, SKY_BLUE, 0, new Sections(new Section(UP, CENTER, 5), new Section(CENTER, DOWN, 6))),
                new Line(LINE_2, GREEN, 0,
                        new Sections(new Section(LEFT, CENTER, 20), new Section(CENTER, RIGHT, 50))));
        final PathFinder pathFinder = new PathFinder(lines);

        // when
        final Path path = pathFinder.find(source, target);

        // then
        assertAll(
                () -> assertThat(path.getVertices()).isEqualTo(route),
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
        final List<Line> lines = List.of(
                new Line(LINE_4, SKY_BLUE, 0, new Sections(new Section(UP, CENTER, 5), new Section(CENTER, DOWN, 6))),
                new Line(LINE_2, GREEN, 0,
                        new Sections(new Section(LEFT, CENTER, 20), new Section(CENTER, RIGHT, 50))));
        final PathFinder pathFinder = new PathFinder(lines);

        //when & then
        Assertions.assertThatThrownBy(() -> pathFinder.find(UP, UP))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역은 같을 수 없습니다.");
    }

    @Test
    @DisplayName("출발역과 도착역 사이에 경로가 없으면 예외처리")
    void findPath_noPath() {
        // given
        final List<Line> lines = List.of(
                new Line(LINE_4, SKY_BLUE, 0, new Sections(new Section(UP, DOWN, 5))),
                new Line(LINE_2, GREEN, 0, new Sections(new Section(LEFT, RIGHT, 20))));
        final PathFinder pathFinder = new PathFinder(lines);

        //when & then
        Assertions.assertThatThrownBy(() -> pathFinder.find(UP, LEFT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("경로를 찾을 수 없습니다.");
    }
}
