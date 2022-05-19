package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PathTest {

    @DisplayName("경로를 탐색한다.")
    @Test
    void calculateMinPath() {
        // given
        Station station1 = new Station(1L, "station1");
        Station station2 = new Station(2L, "station2");
        Station station3 = new Station(3L, "station3");
        Station station4 = new Station(4L, "station4");
        Station station5 = new Station(5L, "station5");

        Line line1 = new Line(1L, "line1", "color1");

        Section section1To2 = new Section(1L, line1.getId(), station1, station2, 10);
        Section section2To3 = new Section(2L, line1.getId(), station2, station3, 10);
        Section section3To5 = new Section(3L, line1.getId(), station3, station5, 10);

        line1.addSection(section1To2);
        line1.addSection(section2To3);
        line1.addSection(section3To5);

        Line line2 = new Line(2L, "line2", "color2");

        Section section2To4 = new Section(3L, line2.getId(), station2, station4, 10);
        Section section4To5 = new Section(4L, line2.getId(), station4, station5, 5);

        line2.addSection(section4To5);
        line2.addSection(section2To4);

        Path pathFinder = new Path(List.of(section1To2, section2To3, section3To5, section2To4, section4To5));

        // when
        List<Station> path = pathFinder.findRoute(station1, station5);

        // then
        assertThat(path).containsExactly(station1, station2, station4, station5);
    }

    @DisplayName("이동할 수 있는 경로가 없는 경우 예외를 던진다.")
    @Test
    void throwExceptionWhenNoRoute() {
        // given
        Station station1 = new Station(1L, "station1");
        Station station2 = new Station(2L, "station2");
        Station station3 = new Station(3L, "station3");
        Station station4 = new Station(4L, "station4");
        Station station5 = new Station(5L, "station5");

        Line line1 = new Line(1L, "line1", "color1");

        Section section1To2 = new Section(1L, line1.getId(), station1, station2, 10);
        Section section2To3 = new Section(2L, line1.getId(), station2, station3, 10);

        line1.addSection(section1To2);
        line1.addSection(section2To3);

        Line line2 = new Line(2L, "line2", "color2");

        Section section4To5 = new Section(3L, line2.getId(), station4, station5, 10);
        line2.addSection(section4To5);

        Path path = new Path(List.of(section1To2, section2To3, section4To5));
        // when && then
        assertThatThrownBy(() -> path.findRoute(station1, station4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이동 가능한 경로가 존재하지 않습니다");
    }

    @DisplayName("거리가 10km 미만이면 기본 요금으로 계산한다")
    @ParameterizedTest
    @CsvSource(value = {"1,1", "3,4", "5,4"})
    void calculateFare1(int distance1, int distance2) {
        // given
        Station station1 = new Station(1L, "station1");
        Station station2 = new Station(2L, "station2");
        Station station3 = new Station(3L, "station3");

        Line line1 = new Line(1L, "line1", "color1");

        Section section1To2 = new Section(1L, line1.getId(), station1, station2, distance1);
        Section section2To3 = new Section(2L, line1.getId(), station2, station3, distance2);

        line1.addSection(section1To2);
        line1.addSection(section2To3);

        // when
        Path pathFinder = new Path(List.of(section1To2, section2To3));
        int fare = pathFinder.calculateFare(station1, station3);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("거리가 10km ~ 50km 사이인 경우 추가로 5km마다 100원을 추가한다")
    @ParameterizedTest
    @CsvSource(value = {"5,5,1350", "10,5,1350", "10,6,1450", "44,1,1950", "45,1,2050", "49,1,2050"})
    void calculateFare2(int distance1, int distance2, int expectedFare) {
        // given
        Station station1 = new Station(1L, "station1");
        Station station2 = new Station(2L, "station2");
        Station station3 = new Station(3L, "station3");

        Line line1 = new Line(1L, "line1", "color1");

        Section section1To2 = new Section(1L, line1.getId(), station1, station2, distance1);
        Section section2To3 = new Section(2L, line1.getId(), station2, station3, distance2);

        line1.addSection(section1To2);
        line1.addSection(section2To3);

        // when
        Path pathFinder = new Path(List.of(section1To2, section2To3));
        int fare = pathFinder.calculateFare(station1, station3);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @DisplayName("거리가 50km가 넘어가는 경우 추가로 8km마다 100원을 추가한다")
    @ParameterizedTest
    @CsvSource(value = {"50,1,2150", "50,8,2150", "51,8,2250"})
    void calculateFare3(int distance1, int distance2, int expectedFare) {
        // given
        Station station1 = new Station(1L, "station1");
        Station station2 = new Station(2L, "station2");
        Station station3 = new Station(3L, "station3");

        Line line1 = new Line(1L, "line1", "color1");

        Section section1To2 = new Section(1L, line1.getId(), station1, station2, distance1);
        Section section2To3 = new Section(2L, line1.getId(), station2, station3, distance2);

        line1.addSection(section1To2);
        line1.addSection(section2To3);

        // when
        Path pathFinder = new Path(List.of(section1To2, section2To3));
        int fare = pathFinder.calculateFare(station1, station3);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}
