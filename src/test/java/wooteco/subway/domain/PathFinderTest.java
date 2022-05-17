package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest {

    /*
    * 노선 2개 만들어서(1호선(~~~), 2호선(~~~))
    *
    * 객체 생성하고, 메서드 호출
    *
    * 최단 경로를 잘 반환하는지
    * */

    @Test
    void calculateMinPath() {
        // given
        Station station1 = new Station(1L, "station1");
        Station station2 = new Station(2L, "station2");
        Station station3 = new Station(3L, "station3");
        Station station4 = new Station(4L, "station4");
        Station station5 = new Station(5L, "station5");

        Section section1To2 = new Section(1L, station1, station2, 10);
        Section section2To3 = new Section(2L, station2, station3, 10);
        Section section3To5 = new Section(3L, station3, station5, 10);

        Line line1 = new Line(1L, "line1", "color1");
        line1.addSection(section1To2);
        line1.addSection(section2To3);
        line1.addSection(section3To5);

        Section section2To4 = new Section(3L, station2, station4, 10);
        Section section4To5 = new Section(4L, station4, station5, 5);

        Line line2 = new Line(2L, "line2", "color2");
        line2.addSection(section4To5);
        line2.addSection(section2To4);

        PathFinder pathFinder = new PathFinder(List.of(line1, line2));
        List<Station> path = pathFinder.findPath(station1, station5);

        assertThat(path).containsExactly(station1, station2, station4, station5);
    }

    @DisplayName("거리가 10km 미만이면 기본 요금으로 계산한다")
    @Test
    void calculateFare1() {
        // given
        Station station1 = new Station(1L, "station1");
        Station station2 = new Station(2L, "station2");
        Station station3 = new Station(3L, "station3");

        Section section1To2 = new Section(1L, station1, station2, 4);
        Section section2To3 = new Section(2L, station2, station3, 5);

        Line line1 = new Line(1L, "line1", "color1");
        line1.addSection(section1To2);
        line1.addSection(section2To3);

        // when
        PathFinder pathFinder = new PathFinder(List.of(line1));
        int fare = pathFinder.calculateFare(station1, station3);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("거리가 10km ~ 50km 사이인 경우 추가로 5km마다 100원을 추가한다")
    @Test
    void calculateFare() {
        // given
        Station station1 = new Station(1L, "station1");
        Station station2 = new Station(2L, "station2");
        Station station3 = new Station(3L, "station3");

        Section section1To2 = new Section(1L, station1, station2, 10);
        Section section2To3 = new Section(2L, station2, station3, 6);

        Line line1 = new Line(1L, "line1", "color1");
        line1.addSection(section1To2);
        line1.addSection(section2To3);

        // when
        PathFinder pathFinder = new PathFinder(List.of(line1));
        int fare = pathFinder.calculateFare(station1, station3);

        // then
        assertThat(fare).isEqualTo(1450);
    }

    @DisplayName("거리가 50km가 넘어가는 경우 추가로 8km마다 100원을 추가한다")
    @Test
    void calculateFareOver50() {
        // given
        Station station1 = new Station(1L, "station1");
        Station station2 = new Station(2L, "station2");
        Station station3 = new Station(3L, "station3");

        Section section1To2 = new Section(1L, station1, station2, 30);
        Section section2To3 = new Section(2L, station2, station3, 28);

        Line line1 = new Line(1L, "line1", "color1");
        line1.addSection(section1To2);
        line1.addSection(section2To3);

        // when
        PathFinder pathFinder = new PathFinder(List.of(line1));
        int fare = pathFinder.calculateFare(station1, station3);

        // then
        assertThat(fare).isEqualTo(2150);
    }
}
