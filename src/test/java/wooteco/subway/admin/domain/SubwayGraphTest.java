package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SubwayGraphTest {
    List<Line> lines;
    Map<Long, Station> stations;

    @BeforeEach
    void setUp() {
        Station stationA = new Station(1L, "A 역");
        Station stationB = new Station(2L, "B 역");
        Station stationC = new Station(3L, "C 역");
        Station stationD = new Station(4L, "D 역");

        stations = new HashMap<>();
        stations.put(1L, stationA);
        stations.put(2L, stationB);
        stations.put(3L, stationC);
        stations.put(4L, stationD);

        Line line = new Line(1L, "2호선", "bg-green-500", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
        line.addLineStation(new LineStation(null, 1L, 0, 0));
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(2L, 3L, 10, 10));
        line.addLineStation(new LineStation(3L, 4L, 10, 10));

        lines = Lists.newArrayList(line);
    }

    @DisplayName("그래프 생성")
    @Test
    void constructor() {
        assertThat(new SubwayDijkstraGraph(lines, stations, PathType.DISTANCE::weight)).isInstanceOf(SubwayGraph.class);
    }

    @DisplayName("경로 생성")
    @Test
    void genearatePath() {
        SubwayGraph subwayGraph = new SubwayDijkstraGraph(lines, stations, PathType.DISTANCE::weight);
        SubwayPath subwayPath = subwayGraph.generatePath(stations.get(1L), stations.get(4L));
        assertThat(subwayPath).isInstanceOf(SubwayPath.class);
    }
}
