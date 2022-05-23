package wooteco.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {

    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Line line1;
    private Line line2;

    @BeforeEach
    void setUp() {
        initStations();
        line1 = new Line(1L, "line1", "color1", 0);
        line2 = new Line(2L, "line2", "color2", 0);
    }

    private void initStations() {
        station1 = new Station(1L, "station1");
        station2 = new Station(2L, "station2");
        station3 = new Station(3L, "station3");
        station4 = new Station(4L, "station4");
        station5 = new Station(5L, "station5");
    }

    @DisplayName("경로가 1-2-3-5/ 2-4-5 있을 떄, 최소경로를 조회한다.")
    @Test
    void calculateMinPath() {
        // given
        Section section1To2 = new Section(1L, line1.getId(), station1, station2, 10);
        Section section2To3 = new Section(2L, line1.getId(), station2, station3, 10);
        Section section3To5 = new Section(3L, line1.getId(), station3, station5, 10);
        line1.addSection(section1To2);
        line1.addSection(section2To3);
        line1.addSection(section3To5);

        Section section2To4 = new Section(3L, line2.getId(), station2, station4, 10);
        Section section4To5 = new Section(4L, line2.getId(), station4, station5, 5);
        line2.addSection(section2To4);
        line2.addSection(section4To5);

        //when
        SubwayGraph subwayGraphFinder = new SubwayGraph(List.of(section1To2, section2To3, section3To5, section2To4, section4To5));
        List<Station> path = subwayGraphFinder.getPath(station1, station5, 0, 50).getStations();

        //then
        assertThat(path).containsExactly(station1, station2, station4, station5);
    }

    @DisplayName("거리가 10km 미만이면 기본 요금으로 계산한다")
    @Test
    void calculateFare1() {
        // given
        Section section1To2 = new Section(1L, line1.getId(), station1, station2, 4);
        Section section2To3 = new Section(2L, line1.getId(), station2, station3, 5);
        line1.addSection(section1To2);
        line1.addSection(section2To3);

        // when
        SubwayGraph subwayGraphFinder = new SubwayGraph(List.of(section1To2, section2To3));
        double fare = subwayGraphFinder.getPath(station1, station3, 0, 50).getFare();
        // then
        assertThat(fare).isEqualTo(1250D);
    }

    @DisplayName("거리가 10km ~ 50km 사이인 경우 추가로 5km마다 100원을 추가한다")
    @Test
    void calculateFare() {
        Section section1To2 = new Section(1L, line1.getId(), station1, station2, 10);
        Section section2To3 = new Section(2L, line1.getId(), station2, station3, 10);
        line1.addSection(section1To2);
        line1.addSection(section2To3);

        // when
        SubwayGraph subwayGraphFinder = new SubwayGraph(List.of(section1To2, section2To3));
        double fare = subwayGraphFinder.getPath(station1, station3, 0, 50).getFare();

        // then
        assertThat(fare).isEqualTo(1450);
    }

    @DisplayName("거리가 50km가 넘어가는 경우 추가로 8km마다 100원을 추가한다")
    @Test
    void calculateFareOver50() {

        Section section1To2 = new Section(1L, line1.getId(), station1, station2, 30);
        Section section2To3 = new Section(2L, line1.getId(), station2, station3, 28);

        line1.addSection(section1To2);
        line1.addSection(section2To3);

        // when
        SubwayGraph subwayGraphFinder = new SubwayGraph(List.of(section1To2, section2To3));
        double fare = subwayGraphFinder.getPath(station1, station3, 0, 50).getFare();

        // then
        assertThat(fare).isEqualTo(2150);
    }
}
