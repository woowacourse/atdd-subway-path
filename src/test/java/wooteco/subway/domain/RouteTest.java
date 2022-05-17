package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RouteTest {

    @DisplayName("최단 경로를 구한다.")
    @Test
    void calculateShortestPath() {
        Route route = new Route();
        Section section1 = new Section(1L, 1L, new Station(1L, "잠실역"), new Station(2L, "선릉역"), 3);
        Section section2 = new Section(2L, 2L, new Station(2L, "선릉역"), new Station(3L, "강남역"), 3);
        Section section3 = new Section(3L, 2L, new Station(3L, "강남역"), new Station(4L, "건대역"), 4);
        Section section4 = new Section(4L, 3L, new Station(4L, "건대역"), new Station(5L, "사가정역"), 5);
        Section section5 = new Section(5L, 4L, new Station(5L, "사가정역"), new Station(1L, "잠실역"), 7);
        route.addSections(List.of(section1, section2, section3, section4, section5));
        List<Station> stations = route.calculateShortestPath(new Station(1L, "잠실역"), new Station(4L, "건대역"));

        assertThat(stations).isEqualTo(List.of(new Station(1L, "잠실역"), new Station(2L, "선릉역")
                , new Station(3L, "강남역"), new Station(4L, "건대역")));
    }

}
