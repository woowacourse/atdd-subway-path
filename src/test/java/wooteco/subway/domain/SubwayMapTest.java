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

    @BeforeEach
    void setUp() {
        subwayMap = new SubwayMap(new WeightedMultigraph<>(DefaultWeightedEdge.class));
        station1 = new Station("잠원역");
        station2 = new Station("잠실역");
        station3 = new Station("강원대역");
    }

    @DisplayName("최단 거리 경로를 조회한다.")
    @Test
    void findShortestPath() {
        Section firstSection = new Section(station1, station2, 5);
        Section secondSection = new Section(station2, station3, 6);
        Section longSection = new Section(station1, station3, 100);

        subwayMap.addSection(firstSection);
        subwayMap.addSection(secondSection);
        subwayMap.addSection(longSection);

        List<Station> shortestPath = subwayMap.findShortestPath(station1, station3);
        assertThat(shortestPath).containsExactly(station1, station2, station3);
    }

    @DisplayName("두 지하철 역 사이의 최단 거리 값을 확인한다.")
    @Test
    void findShortestDistance() {
        Section firstSection = new Section(station1, station2, 5);
        Section secondSection = new Section(station2, station3, 19);
        Section longSection = new Section(station1, station3, 33);

        subwayMap.addSection(firstSection);
        subwayMap.addSection(secondSection);
        subwayMap.addSection(longSection);
        int shortestDistance = subwayMap.findShortestDistance(station1, station3);

        assertThat(shortestDistance).isEqualTo(24);
    }
}
