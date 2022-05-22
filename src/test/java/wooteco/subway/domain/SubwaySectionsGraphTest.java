package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.domain.SectionFixtures.부천역_역곡역_5;
import static wooteco.subway.domain.SectionFixtures.부천역_중동역_5;
import static wooteco.subway.domain.SectionFixtures.신도림역_온수역_5;
import static wooteco.subway.domain.SectionFixtures.역곡역_부천역_5;
import static wooteco.subway.domain.SectionFixtures.중동역_역곡역_5;
import static wooteco.subway.domain.StationFixtures.부천역;
import static wooteco.subway.domain.StationFixtures.역곡역;
import static wooteco.subway.domain.StationFixtures.온수역;
import static wooteco.subway.domain.StationFixtures.중동역;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubwaySectionsGraphTest {

    @DisplayName("하나의 경로만 있는 경우 가장 짧은 경로를 구한다")
    @Test
    void getShortestPathWithOnePath() {
        List<Section> sections = List.of(역곡역_부천역_5, 부천역_중동역_5);
        SubwaySectionsGraph subwaySectionsGraph = new SubwaySectionsGraph(sections);

        Path path = subwaySectionsGraph.getShortestPath(중동역, 역곡역);

        assertAll(
                () -> assertThat(path.getDistance()).isEqualTo(10),
                () -> assertThat(path.getStations()).containsExactly(중동역, 부천역, 역곡역)
        );
    }

    @DisplayName("두개의 경로가 있는 경우 가장 짧은 경로를 구한다")
    @Test
    void getShortestPathWithTwoPath() {
        List<Section> sections = List.of(역곡역_부천역_5, 부천역_중동역_5, 중동역_역곡역_5);
        SubwaySectionsGraph subwaySectionsGraph = new SubwaySectionsGraph(sections);

        Path path = subwaySectionsGraph.getShortestPath(중동역, 역곡역);

        assertAll(
                () -> assertThat(path.getDistance()).isEqualTo(5),
                () -> assertThat(path.getStations()).containsExactly(중동역, 역곡역)
        );
    }

    @DisplayName("다른 노선의 갈 수 없는 경로를 조회하면 예외를 던진다.")
    @Test
    void findPathCanNotGo() {
        List<Section> sections = List.of(신도림역_온수역_5, 부천역_역곡역_5);
        SubwaySectionsGraph subwaySectionsGraph = new SubwaySectionsGraph(sections);

        assertThatThrownBy(() -> subwaySectionsGraph.getShortestPath(온수역, 역곡역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("경로가 존재하지 않습니다.");
    }

    @DisplayName("경로에 존재하지 않는 지하철역을 조회하면 예외를 던진다.")
    @Test
    void findPathNotExistsStation() {
        List<Section> sections = List.of(신도림역_온수역_5, 부천역_역곡역_5);
        SubwaySectionsGraph subwaySectionsGraph = new SubwaySectionsGraph(sections);

        assertThatThrownBy(() -> subwaySectionsGraph.getShortestPath(중동역, 역곡역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선에 등록되지 않은 지하철역입니다.");
    }
}
