package wooteco.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.path.DijkstraStrategy;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    private Path path;
    private Long 강남 = 1L;
    private Long 선릉 = 2L;
    private Long 잠실 = 3L;
    private Long 지하철_2호선 = 1L;

    @BeforeEach
    void setPath() {

        Station 강남역 = new Station(강남, "강남역");
        Station 선릉역 = new Station(선릉, "선릉역");
        Station 잠실역 = new Station(잠실, "잠실역");
        Section 강남_선릉_10 = new Section(1L, 지하철_2호선, 강남, 선릉, 10);
        Section 선릉_잠실_10 = new Section(2L, 지하철_2호선, 선릉, 잠실, 10);

        List<Station> stationList = List.of(강남역, 선릉역, 잠실역);
        List<Section> sectionList = List.of(강남_선릉_10, 선릉_잠실_10);

        path = new Path(stationList, sectionList, new DijkstraStrategy());
    }

    @Test
    @DisplayName("최단 경로를 올바르게 찾는다.")
    void findShortestPath() {
        assertThat(path.getShortestPath(강남, 잠실)).containsExactly(강남, 선릉, 잠실);
    }

    @Test
    @DisplayName("최단 거리를 올바르게 계산한다.")
    void calculateShortestDistance() {
        assertThat(path.calculateShortestDistance(강남, 잠실)).isEqualTo(20);
    }

}
