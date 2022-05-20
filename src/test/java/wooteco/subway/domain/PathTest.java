package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathTest {

    @Test
    @DisplayName("하나의 구간의 최단 경로를 확인한다.")
    void calculateShortestPath() {
        Sections sections = new Sections(List.of(new Section(1L, 1L, 2L, 5)));
        Stations stations = new Stations(List.of(new Station(1L, "미금역"), new Station(2L, "정자역")));
        Path path = new Path(stations, sections);
        List<Station> paths = path.calculateShortestPath(1L, 2L);
        List<Long> stationIds = convertStationToId(paths);
        assertThat(stationIds).containsExactly(1L, 2L);
    }

    @Test
    @DisplayName("여러 구간인 경우 최단 경로를 확인한다.")
    void calculateShortestTwoPath() {
        Sections sections = new Sections(
                List.of(
                        new Section(1L, 1L, 2L, 5),
                        new Section(1L, 2L, 3L, 6),
                        new Section(1L, 2L, 4L, 2),
                        new Section(1L, 3L, 4L, 1)
                )
        );
        Stations stations = new Stations(
                List.of(
                        new Station(1L, "미금역"),
                        new Station(2L, "정자역"),
                        new Station(3L, "수내역"),
                        new Station(4L, "서현역")
                )
        );
        Path path = new Path(stations, sections);
        List<Station> paths = path.calculateShortestPath(1L, 3L);
        List<Long> stationIds = convertStationToId(paths);

        assertThat(stationIds).containsExactly(1L, 2L, 4L, 3L);
    }

//    @Test
//    @DisplayName("하나의 구간의 최단 거리를 확인한다.")
//    void calculateShortestDistance() {
//        Path path = new Path(List.of(new Section(1L, 1L, 2L, 5)));
//        int distance = path.calculateShortestDistance(1L, 2L);
//
//        assertThat(distance).isEqualTo(5);
//    }
//
//    @Test
//    @DisplayName("여러 구간인 경우 최단 거리를 확인한다.")
//    void calculateShortestDistanceWithManySections() {
//        Path path = new Path(List.of(new Section(1L, 1L, 2L, 5), new Section(1L, 2L, 3L, 6),
//                new Section(1L, 2L, 4L, 2), new Section(1L, 3L, 4L, 1)));
//        int distance = path.calculateShortestDistance(1L, 3L);
//
//        assertThat(distance).isEqualTo(8);
//    }
//
//    @Test
//    @DisplayName("출발점이나 도착지가 존재하지 않을 경우 예외를 발생시킨다.")
//    void noExistStation() {
//        Path path = new Path(List.of(new Section(1L, 1L, 3L, 5)));
//        assertThatThrownBy(() -> path.calculateShortestPath(1L, 2L))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("출발지, 도착지 모두 존재해야 됩니다.");
//    }
//
//    @Test
//    @DisplayName("출발지에서 도착지로 갈 수 없는 경우 예외를 발생시킨다.")
//    void noReachable() {
//        Path path = new Path(List.of(new Section(1L, 1L, 2L, 5), new Section(1L, 3L, 4L, 5)));
//        assertThatThrownBy(() -> path.calculateShortestPath(1L, 4L))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("출발지에서 도착지로 갈 수 없습니다.");
//    }

    private List<Long> convertStationToId(List<Station> paths) {
        return paths.stream()
                .map(station -> station.getId())
                .collect(Collectors.toList());
    }
}
