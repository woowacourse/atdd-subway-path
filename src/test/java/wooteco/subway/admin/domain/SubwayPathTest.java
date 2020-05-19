package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SubwayPathTest {
    Map<Long, Station> stations;
    SubwayPath subwayDistancePath;
    SubwayPath subwayDurationPath;

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

        Line line1 = new Line(1L, "2호선", "bg-green-500", LocalTime.of(5, 30), LocalTime.of(22, 30),
                5);
        line1.addLineStation(new LineStation(null, 1L, 0, 0));
        line1.addLineStation(new LineStation(1L, 2L, 10, 10));
        line1.addLineStation(new LineStation(2L, 3L, 10, 10));
        line1.addLineStation(new LineStation(3L, 4L, 10, 3));

        Line line2 = new Line(1L, "3호선", "bg-green-500", LocalTime.of(5, 30), LocalTime.of(22, 30),
                5);
        line2.addLineStation(new LineStation(null, 2L, 0, 0));
        line2.addLineStation(new LineStation(2L, 4L, 10, 20));

        List<Line> lines = Lists.newArrayList(line1, line2);

        SubwayGraph subwayDistanceGraph = new SubwayDijkstraGraph(lines, stations,
                PathType.DISTANCE::weight);
        SubwayGraph subwayDurationGraph = new SubwayDijkstraGraph(lines, stations,
                PathType.DURATION::weight);
        subwayDistancePath = subwayDistanceGraph.generatePath(stations.get(1L), stations.get(4L));
        subwayDurationPath = subwayDurationGraph.generatePath(stations.get(1L), stations.get(4L));
    }

    @DisplayName("최단 거리 경로")
    @Test
    void shortest_distance_path_stations() {
        assertThat(subwayDistancePath.stations())
                .isEqualTo(Arrays.asList(stations.get(1L), stations.get(2L), stations.get(4L)));
    }

    @DisplayName("최단 거리 경로 총 거리")
    @Test
    void shortest_distance_path_total_distance() {
        assertThat(subwayDistancePath.distance()).isEqualTo(20);
    }

    @DisplayName("최단 거리 경로 총 소요시간")
    @Test
    void  shortest_distance_path_total_duration() {
        assertThat(subwayDistancePath.duration()).isEqualTo(30);
    }

    @DisplayName("최소 시간 경로")
    @Test
    void shortest_duration_path_stations() {
        assertThat(subwayDurationPath.stations())
                .isEqualTo(Arrays.asList(stations.get(1L), stations.get(2L), stations.get(3L),
                        stations.get(4L)));
    }

    @DisplayName("최소 시간 경로 총 거리")
    @Test
    void shortest_duration_path_total_distance() {
        assertThat(subwayDurationPath.distance()).isEqualTo(30);
    }

    @DisplayName("최소 시간 경로 총 소요시간")
    @Test
    void  shortest_duration_path_total_duration() {
        assertThat(subwayDurationPath.duration()).isEqualTo(23);
    }
}
