package wooteco.subway.path.infrastructure;

import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.Path;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {

    /**
     * [A역] ---- {2} ---- [B역]
     * \                 /
     * \              /
     * {10}        {2}
     * \        /
     * \     /
     * [C역]
     */
    @Test
    void shortestPath() {
        // given
        // 역 생성
        Station stationA = new Station(1L, "A역");
        Station stationB = new Station(2L, "B역");
        Station stationC = new Station(3L, "C역");

        // 구간 생성
        Sections sections1 = new Sections(Arrays.asList(
                new Section(1L, stationA, stationB, 2),
                new Section(2L, stationB, stationC, 2)
        ));
        Sections sections2 = new Sections(Collections.singletonList(
                new Section(3L, stationA, stationC, 10)
        ));

        // 노선 생성
        Line line1 = new Line(1L, "1호선", "blue", sections1);
        Line line2 = new Line(2L, "2호선", "green", sections2);


        // when
        PathFinder pathFinder = new PathFinder(Arrays.asList(line1, line2));
        Path path = pathFinder.shortestPath(stationA, stationC);

        // then
        assertThat(path.getPathStations()).containsExactly(stationA, stationB, stationC);
        assertThat(path.getDistance()).isEqualTo(4);
    }
}