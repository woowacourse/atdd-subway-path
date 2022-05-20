package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.path.DijkstraStrategy;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

class PathTest {

    private Path path;
    private Long 강남 = 1L;
    private Long 선릉 = 2L;
    private Long 잠실 = 3L;
    private Long 사당 = 4L;
    private Long 지하철_2호선 = 1L;

    private Station 강남역 = new Station(강남, "강남역");
    private Station 선릉역 = new Station(선릉, "선릉역");
    private Station 잠실역 = new Station(잠실, "잠실역");

    private Section 강남_선릉_10 = new Section(1L, 지하철_2호선, 강남, 선릉, 10);
    private Section 선릉_잠실_10 = new Section(2L, 지하철_2호선, 선릉, 잠실, 10);

    private List<Station> stationList = new ArrayList<>(List.of(강남역, 선릉역, 잠실역));
    private List<Section> sectionList = new ArrayList<>(List.of(강남_선릉_10, 선릉_잠실_10));

    @BeforeEach
    void setPath() {
        강남역 = new Station(강남, "강남역");
        선릉역 = new Station(선릉, "선릉역");
        잠실역 = new Station(잠실, "잠실역");

        강남_선릉_10 = new Section(1L, 지하철_2호선, 강남, 선릉, 10);
        선릉_잠실_10 = new Section(2L, 지하철_2호선, 선릉, 잠실, 10);

        stationList = new ArrayList<>(List.of(강남역, 선릉역, 잠실역));
        sectionList = new ArrayList<>(List.of(강남_선릉_10, 선릉_잠실_10));

        path = new Path(stationList, sectionList, new DijkstraStrategy());
    }

    @Test
    @DisplayName("경로가 존재하지 않는 경우에 대한 예외처리.")
    void notExistPath() {
        assertThatThrownBy(() -> path.getShortestPath(사당, 잠실))
                .isInstanceOf(IllegalArgumentException.class);
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

    @Test
    @DisplayName("최단 거리를 올바르게 계산한다. - 거리 변경 시")
    void calculateShortestDistance2() {
        Station 사당역 = new Station(사당, "사당역");
        Section 사당_잠실_5 = new Section(3L, 지하철_2호선, 사당, 잠실, 5);
        stationList.add(사당역);
        sectionList.add(사당_잠실_5);

        path = new Path(stationList, sectionList, new DijkstraStrategy());

        assertThat(path.calculateShortestDistance(강남, 잠실)).isEqualTo(20);
    }

    @Test
    @DisplayName("최단 거리를 올바르게 계산한다. - 환승 시")
    void calculateShortestDistance3() {
        Station 사당역 = new Station(사당, "사당역");
        Section 사당_잠실_5 = new Section(3L, 지하철_2호선, 사당, 잠실, 5);
        Long 지하철_3호선 = 2L;
        Section 선릉_잠실_3 = new Section(4L, 지하철_3호선, 선릉, 잠실, 3);

        stationList.add(사당역);
        sectionList.add(사당_잠실_5);
        sectionList.add(선릉_잠실_3);

        path = new Path(stationList, sectionList, new DijkstraStrategy());

        assertThat(path.calculateShortestDistance(강남, 잠실)).isEqualTo(13);
    }
}
