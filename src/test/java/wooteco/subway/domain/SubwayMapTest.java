package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.exception.EmptyResultException;

public class SubwayMapTest {
    private static SubwayMap subwayMap;
    private static final List<Station> stations = new ArrayList<>();

    @BeforeAll
    public static void setUp() {

        for (char c = 'a'; c <= 'k'; c++) {
            stations.add(new Station(String.valueOf(c)));
        }

        Line line1 = createLine1();
        Line line2 = createLine2();
        Line line3 = createLine3();
        Line line4 = createLine4();
        subwayMap = SubwayMap.of(List.of(line1, line2, line3, line4));
    }

    private static Line createLine1() {
        List<Section> sections1 = new ArrayList<>();
        sections1.add(new Section(stations.get(0), stations.get(1), 5));
        sections1.add(new Section(stations.get(1), stations.get(2), 15));
        sections1.add(new Section(stations.get(2), stations.get(3), 10));
        return Line.from(new Line(1L, "1", "red"), sections1);
    }

    private static Line createLine2() {
        List<Section> sections2 = new ArrayList<>();
        sections2.add(new Section(stations.get(1), stations.get(4), 4));
        sections2.add(new Section(stations.get(4), stations.get(5), 7));
        sections2.add(new Section(stations.get(5), stations.get(6), 4));
        return Line.from(new Line(2L, "2", "green"), sections2);
    }

    private static Line createLine3() {
        List<Section> sections3 = new ArrayList<>();
        sections3.add(new Section(stations.get(6), stations.get(2), 10));
        sections3.add(new Section(stations.get(2), stations.get(7), 15));
        sections3.add(new Section(stations.get(7), stations.get(8), 23));
        return Line.from(new Line(3L, "3", "orange"), sections3);
    }

    private static Line createLine4() {
        List<Section> sections4 = new ArrayList<>();
        sections4.add(new Section(stations.get(9), stations.get(10), 10));
        return Line.from(new Line(4L, "4", "blue"), sections4);
    }

    @Test
    @DisplayName("최단경로 거리의 합이 10km 이내인 경우 경로(1,2), 거리(5)가 반환된다.")
    void findShortestPath1() {
        Path path = subwayMap.findShortestPath(stations.get(0), stations.get(1));

        assertAll(
            () -> assertThat(path.getStations())
                .containsExactly(stations.get(0), stations.get(1)),
            () -> assertThat(path.getDistance()).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("최단경로 거리의 합이 10km 이상 50km 이하인 경우 경로(1,2,5,6,7), 거리(20)이 반환된다.")
    void findShortestPath2() {
        Path path = subwayMap.findShortestPath(stations.get(0), stations.get(6));

        assertAll(
            () -> assertThat(path.getStations())
                .containsExactly(stations.get(0), stations.get(1), stations.get(4), stations.get(5), stations.get(6)),
            () -> assertThat(path.getDistance()).isEqualTo(20)
        );
    }

    @Test
    @DisplayName("최단경로 거리의 합이 50km 초과인 경우 경로(1,2,3,8,9), 거리(58)이 반환되어야 한다.")
    void findShortestPath3() {
        Path path = subwayMap.findShortestPath(stations.get(0), stations.get(8));

        assertAll(
            () -> assertThat(path.getStations())
                .containsExactly(stations.get(0), stations.get(1), stations.get(2), stations.get(7), stations.get(8)),
            () -> assertThat(path.getDistance()).isEqualTo(58)
        );
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어있지 않으면 예외를 던져야 한다.")
    void findInvalidPath() {
        assertThatThrownBy(() -> subwayMap.findShortestPath(stations.get(0), stations.get(9)))
            .hasMessage("출발역과 도착역 사이에 연결된 경로가 없습니다.")
            .isInstanceOf(EmptyResultException.class);
    }
}
