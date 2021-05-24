package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.util.DijkstraPath;
import wooteco.subway.station.domain.Station;

public class PathTest {

    @Autowired
    private PathService pathService;

    @DisplayName("최단경로 알고리즘 경로, 최단거리 탐색")
    @Test
    public void getDijkstraShortestPath() {
        Station source = new Station("station1");
        Station target = new Station("station2");
        Station middle = new Station("station3");
        Section section1 = new Section(source, middle, 10);
        Section section2 = new Section(middle, target, 20);
        Sections sections = new Sections(Arrays.asList(section1, section2));

        DijkstraPath dijkstraPath = new DijkstraPath(source, target, sections);

        assertThat(dijkstraPath.findShortestDistance()).isEqualTo(30);
        assertThat(dijkstraPath.findShortestRouteToStationResponse()).hasSize(3);
    }

}
