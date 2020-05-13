package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {
    private Path path;

    @BeforeEach
    void setUp() {
        path = new Path();
    }

    @DisplayName("최단 거리 경로 조회")
    @Test
    void getShortestDistancePath() {
        //given
        List<Station> stations = Arrays.asList(new Station(1L, "잠실역"), new Station(2L, "삼성역"),
            new Station(3L, "선릉역"));
        path.addStation(stations.get(0));
        path.addStation(stations.get(1));
        path.addStation(stations.get(2));
        path.setDistanceWeight(stations.get(0), stations.get(1), 10);
        path.setDurationWeight(stations.get(0), stations.get(1), 5);
        path.setDistanceWeight(stations.get(1), stations.get(2), 10);
        path.setDurationWeight(stations.get(1), stations.get(2), 5);
        //when
        List<Station> pathStations = path.searchShortestDistancePath(stations.get(0),
            stations.get(2));
        int distance = path.calculateDistance();
        int duration = path.calculateDuration();
        //then
        assertThat(pathStations.size()).isEqualTo(3);
        assertThat(distance).isEqualTo(20);
        assertThat(duration).isEqualTo(10);
    }
}
