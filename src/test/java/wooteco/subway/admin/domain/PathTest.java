package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {
    private Path path;

    @BeforeEach
    void setUp() {
        Line line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5,
            "bg-green-600");

        line.addLineStation(new LineStation(null, 1L, 10, 5));
        line.addLineStation(new LineStation(1L, 2L, 10, 5));
        line.addLineStation(new LineStation(2L, 3L, 10, 5));
        path = new Path(Collections.singletonList(line));
    }

    @DisplayName("최단 거리 경로 조회")
    @Test
    void getShortestDistancePath() {
        //given
        List<Station> stations = Arrays.asList(new Station(1L, "잠실역"), new Station(2L, "삼성역"),
            new Station(3L, "선릉역"));
        //when
        List<Long> pathStations = path.searchShortestDistancePath(stations.get(0), stations.get(2));
        int distance = path.calculateDistance(pathStations);
        int duration = path.calculateDuration(pathStations);
        //then
        assertThat(pathStations.size()).isEqualTo(3);
        assertThat(distance).isEqualTo(20);
        assertThat(duration).isEqualTo(10);
    }
}
