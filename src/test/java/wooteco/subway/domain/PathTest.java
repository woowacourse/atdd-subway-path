package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.domain.SectionFixtures.부천역_중동역_5;
import static wooteco.subway.domain.SectionFixtures.신대방역_서울대입구역_15;
import static wooteco.subway.domain.SectionFixtures.신도림역_신대방역_10;
import static wooteco.subway.domain.SectionFixtures.신도림역_온수역_5;
import static wooteco.subway.domain.SectionFixtures.역곡역_부천역_5;
import static wooteco.subway.domain.SectionFixtures.온수역_역곡역_5;
import static wooteco.subway.domain.StationFixtures.서울대입구역;
import static wooteco.subway.domain.StationFixtures.센트럴파크역;
import static wooteco.subway.domain.StationFixtures.온수역;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void getShortestPath() {
        List<Section> sections = List.of(
                신도림역_온수역_5, 온수역_역곡역_5, 역곡역_부천역_5, 부천역_중동역_5, 신도림역_신대방역_10, 신대방역_서울대입구역_15);

        Path path = new Path(sections);

        assertAll(
                () -> assertThat(path.getStations(온수역, 서울대입구역).size()).isEqualTo(4),
                () -> assertThat(path.getDistance(온수역, 서울대입구역)).isEqualTo(30)
        );
    }

    @DisplayName("최단 경로를 조회할 때 경로에 없는 지하철역을 조회할 경우 예외를 던진다.")
    @Test
    void getShortestPathNotExistsStation() {
        List<Section> sections = List.of(
                신도림역_온수역_5, 온수역_역곡역_5, 역곡역_부천역_5, 부천역_중동역_5, 신도림역_신대방역_10, 신대방역_서울대입구역_15);

        Path path = new Path(sections);

        assertThatThrownBy(() -> path.getStations(온수역, 센트럴파크역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("최단 경로 조회 중 source와 target이 동일한 지하철역인 경우 거리는 0이다.")
    @Test
    void getShortestPathSameStation() {
        List<Section> sections = List.of(
                신도림역_온수역_5, 온수역_역곡역_5, 역곡역_부천역_5, 부천역_중동역_5, 신도림역_신대방역_10, 신대방역_서울대입구역_15);

        Path path = new Path(sections);

        assertAll(
                () -> assertThat(path.getStations(온수역, 온수역).size()).isEqualTo(1),
                () -> assertThat(path.getDistance(온수역, 온수역)).isEqualTo(0)
        );
    }
}
