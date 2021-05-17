package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.path.domain.SubwayMap;
import wooteco.subway.section.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayMapTest {

    private SubwayMap subwayMap;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;

    @BeforeEach
    void setUp() {
        subwayMap = new SubwayMap(new WeightedMultigraph<>(DefaultWeightedEdge.class));
        station1 = new Station(1L, "잠원역");
        station2 = new Station(2L, "잠실역");
        station3 = new Station(3L, "강원대역");
        station4 = new Station(4L, "선릉역");
    }

    @DisplayName("최단 거리 경로 및 거리를 조회한다.")
    @Test
    void findShortestPath() {
        Section firstSection = new Section(station1, station2, 5);
        Section secondSection = new Section(station2, station4, 6);
        Section thirdSection = new Section(station1, station3, 4);
        Section fourthSection = new Section(station3, station4, 3);

        subwayMap.addSection(firstSection);
        subwayMap.addSection(secondSection);
        subwayMap.addSection(thirdSection);
        subwayMap.addSection(fourthSection);
        List<Station> shortestPath = subwayMap.findShortestPath(station1, station4);
        int shortestDistance = subwayMap.findShortestDistance(station1, station4);

        assertThat(shortestPath).containsExactly(station1, station3, station4);
        assertThat(shortestDistance).isEqualTo(7);
    }
}
