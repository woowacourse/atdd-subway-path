package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.vo.Path;
import wooteco.subway.exception.EmptyResultException;

public class SubwayTest {
    private static Subway subway;
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
        subway = Subway.of(List.of(line1, line2, line3, line4));
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
    @DisplayName("0 -> 1의 경로는 0 1, 거리는 5가 반환되어야 한다.")
    void findShortestPath1() {
        Path path = subway.findShortestPath(stations.get(0), stations.get(1));

        assertAll(
            () -> assertThat(path.getStations())
                .containsExactly(stations.get(0), stations.get(1)),
            () -> assertThat(path.getDistance()).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("0 -> 6의 경로는 0 1 4 5 6, 거리는 20이 반환되어야 한다.")
    void findShortestPath2() {
        Path path = subway.findShortestPath(stations.get(0), stations.get(6));

        assertAll(
            () -> assertThat(path.getStations())
                .containsExactly(stations.get(0), stations.get(1), stations.get(4), stations.get(5), stations.get(6)),
            () -> assertThat(path.getDistance()).isEqualTo(20)
        );
    }

    @Test
    @DisplayName("0 -> 8의 경로는 0 1 2 7 8, 거리는 58이 반환되어야 한다.")
    void findShortestPath3() {
        Path path = subway.findShortestPath(stations.get(0), stations.get(8));

        assertAll(
            () -> assertThat(path.getStations())
                .containsExactly(stations.get(0), stations.get(1), stations.get(2), stations.get(7), stations.get(8)),
            () -> assertThat(path.getDistance()).isEqualTo(58)
        );
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어있지 않으면 예외를 던져야 한다.")
    void findInvalidPath() {
        assertThatThrownBy(() -> subway.findShortestPath(stations.get(0), stations.get(9)))
            .hasMessage("출발역과 도착역 사이에 연결된 경로가 없습니다.")
            .isInstanceOf(EmptyResultException.class);
    }

    @Test
    @DisplayName("0 -> 1 경로의 요금은 1250원이어야 한다.")
    void calculateFare1() {
        Path path = subway.findShortestPath(stations.get(0), stations.get(1));
        int fare = subway.calculateFare(path.getDistance());
        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("0 -> 6 경로의 요금은 1450원이어야 한다.")
    void calculateFare2() {
        Path path = subway.findShortestPath(stations.get(0), stations.get(6));
        int fare = subway.calculateFare(path.getDistance());
        assertThat(fare).isEqualTo(1450);
    }

    @Test
    @DisplayName("0 -> 8 경로의 요금은 2150원이어야 한다.")
    void calculateFare3() {
        Path path = subway.findShortestPath(stations.get(0), stations.get(8));
        int fare = subway.calculateFare(path.getDistance());
        assertThat(fare).isEqualTo(2150);
    }
}
