package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.NotLinkPathException;

class SubwayGraphTest {

    @DisplayName("source 에서 target 이 연결되지 않는다면, 예외를 발생시킨다.")
    @Test
    void notLinkSourceToTargetException() {
        Section section1 = new Section(1L, 1L, new Station(1L, "잠실역"), new Station(2L, "선릉역"), 3);
        Section section2 = new Section(2L, 2L, new Station(2L, "선릉역"), new Station(3L, "강남역"), 3);
        Section section3 = new Section(3L, 3L, new Station(3L, "강남역"), new Station(1L, "건대역"), 4);
        Section section4 = new Section(4L, 4L, new Station(4L, "건대역"), new Station(5L, "사가정역"), 5);
        Sections sections = new Sections(List.of(section1, section2, section3, section4));

        SubwayGraph subwayGraph = new SubwayGraph();
        subwayGraph.init(sections);

        Station source = new Station(1L, "잠실역");
        Station target = new Station(4L, "건대역");

        assertThatThrownBy(() -> subwayGraph.graphResult(source, target))
                .isInstanceOf(NotLinkPathException.class)
                .hasMessage("출발역과 도착역이 연결되어 있지 않습니다.");
    }
}