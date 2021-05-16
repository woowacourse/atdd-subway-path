package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.PathFinder;
import wooteco.subway.station.domain.Station;

public class PathFinderTest {

    private PathFinder pathFinder;
    private List<Station> stations;
    private List<Line> lines;

    @BeforeEach
    void setUp() {
        stations = Arrays.asList(
            new Station(1L, "답십리역"),
            new Station(2L, "왕십리역"),
            new Station(3L, "잠실역"),
            new Station(4L, "홍대입구역"),
            new Station(5L, "사당역")
        );

        List<Section> sections = Arrays.asList(
            new Section(1L, stations.get(0), stations.get(1), 1),
            new Section(2L, stations.get(1), stations.get(2), 2),
            new Section(3L, stations.get(2), stations.get(3), 3)
        );

        lines = Arrays.asList(
            new Line(1L, "1호선", "white", new Sections(
                Collections.singletonList(new Section(4L, stations.get(0), stations.get(3), 10)))),
            new Line(2L, "2호선", "black", new Sections(sections))
        );

        pathFinder = new PathFinder(stations, lines);
    }

    @DisplayName("최소 경로를 구한다")
    @Test
    void shortestPath() {
        // when
        Path path = pathFinder.shortestPath(stations.get(0), stations.get(3));

        // then
        List<Station> expected = lines.get(1).getStations();
        assertThat(path.getDistance()).isEqualTo(6);
        assertThat(path.getStations()).isEqualTo(expected);
    }

    @DisplayName("출발역과 도착역이 같음녀 예외 처리한다.")
    @Test
    void shortestPathWithDuplicatedStations() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> pathFinder.shortestPath(stations.get(0), stations.get(0)))
            .withMessage("출발점과 도착점이 같을 수 없습니다.");
    }

    @DisplayName("경로가 존재하지 않으면 예외 처리한다.")
    @Test
    void shortestPathWhenPathDoesNotExists() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> pathFinder.shortestPath(stations.get(0), stations.get(4)))
            .withMessage("경로가 존재하지 않습니다.");
    }
}
