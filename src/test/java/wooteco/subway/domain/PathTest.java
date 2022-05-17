package wooteco.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    private Path path;

    @BeforeEach
    void setPath() {
        List<Station> stationList = new ArrayList<>(List.of(new Station(1L, "강남역"), new Station(2L, "선릉역"), new Station(3L, "잠실역")));

        List<Section> sectionList = new ArrayList<>(List.of(new Section(1L, 1L, 1L, 2L, 10), new Section(2L, 1L, 2L, 3L, 10)));

        path = new Path(stationList, sectionList);
    }

    @Test
    @DisplayName("최단 경로를 올바르게 찾는다.")
    void findShortestPath() {
        assertThat(path.getShortestPath(1L, 3L)).containsExactly(1L, 2L, 3L);
    }

    @Test
    @DisplayName("최단 거리를 올바르게 계산한다.")
    void calculateShortestDistance() {
        assertThat(path.calculateShortestDistance(1L, 3L)).isEqualTo(20);
    }

}
