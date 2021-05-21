package wooteco.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.path.SubwayMapException;
import wooteco.subway.path.domain.ShortestPathStrategy;
import wooteco.subway.path.domain.SubwayMap;
import wooteco.subway.section.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class SubwayMapTest {

    private SubwayMap subwayMap;

    @BeforeEach
    void setUp() {
        Station station1 = new Station(1L, "잠원역");
        Station station2 = new Station(2L, "잠실역");
        Station station3 = new Station(3L, "강원대역");
        Station station4 = new Station(4L, "선릉역");

        Section firstSection = new Section(station1, station2, 5);
        Section secondSection = new Section(station2, station4, 6);
        Section thirdSection = new Section(station1, station3, 4);
        Section fourthSection = new Section(station3, station4, 3);

        subwayMap = new SubwayMap(ShortestPathStrategy.DIJKSTRA, Arrays.asList(firstSection, secondSection, thirdSection, fourthSection));
    }

    @DisplayName("최단 거리 경로 및 거리를 조회한다.")
    @Test
    void findShortestPath() {
        List<Long> shortestPath = subwayMap.findShortestPath(1L, 4L);
        int shortestDistance = subwayMap.findShortestDistance(1L, 4L);

        assertThat(shortestPath).containsExactly(1L, 3L, 4L);
        assertThat(shortestDistance).isEqualTo(7);
    }

    @DisplayName("없는 경로를 조회한다.")
    @Test
    void cannotFindShortestPath() {
        assertThatCode(() -> subwayMap.findShortestPath(6L, 1L))
                .isInstanceOf(SubwayMapException.class)
                .hasMessage("존재하지 않는 역에 대해 경로를 조회할 수 없습니다.");
    }
}
