package wooteco.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

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

    @DisplayName("경로가 한 개있으면 그 경로를 조회한다.")
    @Test
    void getOneWayPath() {
        // given
        Section section1To2 = new Section(1L, line1.getId(), station1, station2, 10);
        Section section2To3 = new Section(2L, line1.getId(), station2, station3, 10);
        Section section3To5 = new Section(3L, line1.getId(), station3, station5, 10);
        line1.addSection(section1To2);
        line1.addSection(section2To3);
        line1.addSection(section3To5);

        //when
        ShortestPath path = new SubwayGraph(List.of(section1To2, section2To3, section3To5));
        List<Station> stations = path.getPath(station1, station5, 0, 50).getStations();

        //then
        assertThat(stations).containsExactly(station1, station2, station3, station5);
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
        ShortestPath path = new SubwayGraph(List.of(section1To2, section2To3, section3To5, section2To4, section4To5));
        List<Station> stations = path.getPath(station1, station5, 0, 50).getStations();

        //then
        assertThat(stations).containsExactly(station1, station2, station4, station5);
    }

    @DisplayName("경로가 1-2-3-5/ 2-4-5/ 2-5 있을 떄, 최소경로를 조회한다.")
    @Test
    void getPathWhen3Way() {
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

        Line line3 = new Line("3호선", "black", 0);
        Section section2To5 = new Section(4L, line2.getId(), station2, station5, 10);
        line3.addSection(section2To5);

        //when
        ShortestPath path = new SubwayGraph(List.of(
                section1To2, section2To3, section3To5,
                section2To4, section4To5, section2To5));
        List<Station> stations = path.getPath(station1, station5, 0, 50).getStations();

        //then
        assertThat(stations).containsExactly(station1, station2, station5);
    }

    @DisplayName("거리가 10km 미만이면 기본 요금으로 계산한다")
    @ParameterizedTest
    @ValueSource(ints = {4, 5})
    void calculateFare1(int distance) {
        // given
        Section section1To2 = new Section(1L, line1.getId(), station1, station2, 4);
        Section section2To3 = new Section(2L, line1.getId(), station2, station3, distance);
        line1.addSection(section1To2);
        line1.addSection(section2To3);

        // when
        ShortestPath path = new SubwayGraph(List.of(section1To2, section2To3));
        double fare = path.getPath(station1, station3, 0, 50).getFare();
        // then
        assertThat(fare).isEqualTo(1250D);
    }

    @DisplayName("거리가 10km 초과 50km 이하인 경우 추가로 5km마다 100원을 추가한다")
    @ParameterizedTest
    @CsvSource({"1, 1250", "35, 1950", "39, 2050", "40, 2050", "41, 2150"})
    void calculateFare(int distance, int expected) {
        //given
        Section section1To2 = new Section(1L, line1.getId(), station1, station2, 9);
        Section section2To3 = new Section(2L, line1.getId(), station2, station3, distance);
        line1.addSection(section1To2);
        line1.addSection(section2To3);

        // when
        ShortestPath path = new SubwayGraph(List.of(section1To2, section2To3));
        double fare = path.getPath(station1, station3, 0, 50).getFare();

        // then
        assertThat(fare).isEqualTo(expected);
    }

    @DisplayName("거리가 50km가 넘어가는 경우 추가로 8km마다 100원을 추가한다")
    @ParameterizedTest
    @CsvSource({"20, 2150", "28, 2150", "36, 2250", "37, 2350", "38, 2350"})
    void calculateFareOver50(int distance, int actual) {
        //given
        Section section1To2 = new Section(1L, line1.getId(), station1, station2, 30);
        Section section2To3 = new Section(2L, line1.getId(), station2, station3, distance);

        line1.addSection(section1To2);
        line1.addSection(section2To3);

        // when
        ShortestPath path = new SubwayGraph(List.of(section1To2, section2To3));
        double fare = path.getPath(station1, station3, 0, 50).getFare();

        // then
        assertThat(fare).isEqualTo(actual);
    }

    @DisplayName("2개의 길이 존재하나 거리가 같은 경우 그래프에 먼저 담긴 section을 기준으로 조회한다.")
    @Test
    void getPathWhen2WayWithSameDistance() {
        //given
        Section section1To2 = new Section(line1.getId(), station1, station2, 10);
        Section section2To4 = new Section(line1.getId(), station2, station4, 10);
        line1.addSection(section1To2);
        line1.addSection(section2To4);

        Section section1To3 = new Section(line2.getId(), station1, station3, 10);
        Section section3To4 = new Section(line2.getId(), station3, station4, 10);
        line2.addSection(section1To3);
        line2.addSection(section3To4);

        // when
        ShortestPath path = new SubwayGraph(List.of(section1To2, section1To3, section2To4, section3To4));
        List<Station> stations = path.getPath(station1, station4, 0, 50).getStations();

        // then
        assertThat(stations).containsExactly(station1, station2, station4);
    }
}
