package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.domain.SectionFixtures.부천역_중동역_5;
import static wooteco.subway.domain.SectionFixtures.역곡역_부천역_5;
import static wooteco.subway.domain.SectionFixtures.중동역_역곡역_5;
import static wooteco.subway.domain.StationFixtures.부천역;
import static wooteco.subway.domain.StationFixtures.역곡역;
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
}
