package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubwayMapTest {

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

        SubwayMap subwayMap = new SubwayMap(List.of(section1To2, section2To3, section3To5, section2To4, section4To5));

        // when
        List<Station> stations = subwayMap.calculatePath(station1, station5).getStations();

        // then
        assertThat(stations).containsExactly(station1, station2, station4, station5);
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

        SubwayMap subwayMap = new SubwayMap(List.of(section1To2, section2To3, section4To5));
        // when && then
        assertThatThrownBy(() -> subwayMap.calculatePath(station1, station4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이동 가능한 경로가 존재하지 않습니다");
    }

    @DisplayName("지나는 노선의 id를 추가한다.")
    @Test
    void calculatePassingLineId() {
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

        SubwayMap subwayMap = new SubwayMap(List.of(section1To2, section2To3, section3To5, section2To4, section4To5));

        // when
        Path path = subwayMap.calculatePath(station1, station5);

        // then
        assertThat(path.getPassingLineIds()).containsExactly(line1.getId(), line2.getId());
    }
}
