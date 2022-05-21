package wooteco.subway.domain;

import org.jgrapht.Graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.path.PathEdge;
import wooteco.subway.domain.path.factory.JgraphtPathFactory;
import wooteco.subway.domain.path.factory.PathFactory;
import wooteco.subway.domain.path.strategy.DijkstraStrategy;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathTest {

    private PathFactory pathFactory;
    private Station 강남역;
    private Station 선릉역;
    private Station 잠실역;
    private Station 사당역;
    private final Long 지하철_2호선 = 1L;

    @BeforeEach
    void setPath() {
        강남역 = new Station("강남역");
        선릉역 = new Station("선릉역");
        잠실역 = new Station("잠실역");
        사당역 = new Station("사당역");
        Section 강남_선릉_10 = new Section(1L, 지하철_2호선, 강남역, 선릉역, 10);
        Section 선릉_잠실_10 = new Section(2L, 지하철_2호선, 선릉역, 잠실역, 10);

        List<Section> sectionList = List.of(강남_선릉_10, 선릉_잠실_10);
        pathFactory = JgraphtPathFactory.of(sectionList, new DijkstraStrategy());
    }

    @Test
    @DisplayName("경로가 존재하지 않는 경우에 대한 예외처리.")
    void notExistPath() {
        assertThatThrownBy(() -> Path.of(pathFactory, 사당역, 잠실역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("최단 경로를 올바르게 찾는다.")
    void findShortestPath() {
        assertThat(Path.of(pathFactory, 강남역, 잠실역).getStations()).containsExactly(강남역, 선릉역, 잠실역);
    }

    @Test
    @DisplayName("최단 거리를 올바르게 계산한다.")
    void calculateShortestDistance() {
        assertThat(Path.of(pathFactory, 강남역, 잠실역).calculateDistance()).isEqualTo(20);
    }
}
