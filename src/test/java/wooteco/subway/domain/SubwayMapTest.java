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
    private static final Station station1 = new Station("1");
    private static final Station station2 = new Station("2");
    private static final Station station3 = new Station("3");
    private static final Station station4 = new Station("4");
    private static final Station station5 = new Station("5");
    private static final Station station6 = new Station("6");
    private static final Station station7 = new Station("7");
    private static final Station station8 = new Station("8");
    private static final Station station9 = new Station("9");
    private static final Station station10 = new Station("10");
    private static final Station station11 = new Station("11");

    @BeforeAll
    public static void setUp() {
        List<Section> sections1 = new ArrayList<>();

        sections1.add(new Section(station1, station2, 5));
        sections1.add(new Section(station2, station3, 15));
        sections1.add(new Section(station3, station4, 10));

        List<Section> sections2 = new ArrayList<>();
        sections2.add(new Section(station2, station5, 4));
        sections2.add(new Section(station5, station6, 7));
        sections2.add(new Section(station6, station7, 4));

        List<Section> sections3 = new ArrayList<>();
        sections3.add(new Section(station7, station3, 10));
        sections3.add(new Section(station3, station8, 15));
        sections3.add(new Section(station8, station9, 23));

        List<Section> sections4 = new ArrayList<>();
        sections4.add(new Section(station10, station11, 10));

        Line line1 = Line.from(new Line(1L, "1", "red"), sections1);
        Line line2 = Line.from(new Line(2L, "2", "green"), sections2);
        Line line3 = Line.from(new Line(3L, "3", "orange"), sections3);
        Line line4 = Line.from(new Line(4L, "4", "blue"), sections4);

        List<Line> lines = List.of(line1, line2, line3, line4);
        subwayMap = SubwayMap.of(lines);
    }

    @Test
    @DisplayName("최단경로 거리의 합이 10km 이내인 경우 경로, 거리, 요금은 1250원이 반환되어야 한다.")
    void findShortestPath1() {
        Path path = subwayMap.findShortestPath(station1, station2);

        assertAll(
            () -> assertThat(path.getStations())
                .containsExactly(station1, station2),
            () -> assertThat(path.getDistance()).isEqualTo(5),
            () -> assertThat(path.calculateFare()).isEqualTo(1250)
        );
    }

    @Test
    @DisplayName("최단경로 거리의 합이 10km 이상 50km 이하인 경우 경로, 거리, 요금은 1450원 이 반환되어야 한다.")
    void findShortestPath2() {
        Path path = subwayMap.findShortestPath(station1, station7);

        assertAll(
            () -> assertThat(path.getStations())
                .containsExactly(station1, station2, station5, station6, station7),
            () -> assertThat(path.getDistance()).isEqualTo(20),
            () -> assertThat(path.calculateFare()).isEqualTo(1450)
        );
    }

    @Test
    @DisplayName("최단경로 거리의 합이 50km 초과인 경우 경로, 거리, 요금은 2150원 이 반환되어야 한다.")
    void findShortestPath3() {
        Path path = subwayMap.findShortestPath(station1, station9);

        assertAll(
            () -> assertThat(path.getStations())
                .containsExactly(station1, station2, station3, station8, station9),
            () -> assertThat(path.getDistance()).isEqualTo(58),
            () -> assertThat(path.calculateFare()).isEqualTo(2150)
        );
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어있지 않으면 예외를 던져야 한다.")
    void findInvalidPath() {
        assertThatThrownBy(() -> subwayMap.findShortestPath(station1, station10))
            .hasMessage("출발역과 도착역 사이에 연결된 경로가 없습니다.")
            .isInstanceOf(EmptyResultException.class);
    }
}
