package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.path.Path;
import wooteco.subway.util.GraphEdgeFactory;
import wooteco.subway.util.PathFinder;

public class PathFinderTest {

    private static final int CHILDREN_AGE = 12;

    @Test
    void getShortestPath() {
        //given
        var stations = new Stations(List.of(
                new Station(1L, "테스트1역"),
                new Station(2L, "테스트2역"),
                new Station(3L, "테스트3역"),
                new Station(4L, "테스트4역"),
                new Station(5L, "테스트5역")
        ));

        var sections = new Sections(List.of(
                new Section(1L, 2L, 10, 1L),
                new Section(2L, 3L, 10, 1L),
                new Section(2L, 4L, 1, 2L),
                new Section(4L, 5L, 1, 2L),
                new Section(5L, 3L, 1, 2L)
        ));

        var lines = List.of(
                new Line(1L, "테스트1호선", "테스트1색", 1000),
                new Line(2L, "테스트2호선", "테스트2색", 500)
        );

        var path = new Path(stations, sections, new Lines(lines));

        var pathFinder = new PathFinder(stations.getIds(), GraphEdgeFactory.from(sections.get()));

        var graphPathResponse = pathFinder.find(1L, 3L);

        //when
        var pathResult = path.getPath(graphPathResponse, CHILDREN_AGE);

        //then
        assertAll(
                () -> assertThat(pathResult.getStations().size()).isEqualTo(5),
                () -> assertThat(pathResult.getDistance()).isEqualTo(13),
                () -> assertThat(pathResult.getFare()).isEqualTo(1000)
        );
    }
}
