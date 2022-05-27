package wooteco.subway.service.path;

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
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;

class ShortestPathFindableTest {

    @DisplayName("주어진 Section List를 활용하여 최단 경로를 조회한다.")
    @Test
    void 최단경로_조회() {
        List<Section> sections = List.of(
                신도림역_온수역_5, 온수역_역곡역_5, 역곡역_부천역_5, 부천역_중동역_5, 신도림역_신대방역_10, 신대방역_서울대입구역_15);

        PathFindable pathFindable = new ShortestPathFindable();
        Path path = pathFindable.findPath(sections, 온수역, 서울대입구역);

        assertAll(
                () -> assertThat(path.getStations().size()).isEqualTo(4),
                () -> assertThat(path.getDistance()).isEqualTo(30)
        );
    }

    @DisplayName("최단 경로를 조회할 때 경로에 없는 지하철역을 조회할 경우 예외를 던진다.")
    @Test
    void 경로에_존재하지_않는_지하철_조회_예외발생() {
        List<Section> sections = List.of(
                신도림역_온수역_5, 온수역_역곡역_5, 역곡역_부천역_5, 부천역_중동역_5, 신도림역_신대방역_10, 신대방역_서울대입구역_15);

        PathFindable pathFindable = new ShortestPathFindable();

        assertThatThrownBy(() -> pathFindable.findPath(sections, 온수역, 센트럴파크역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("최단 경로 조회 중 source와 target이 동일한 지하철역인 경우 거리는 0이다.")
    @Test
    void 동일한_지하철역_조회() {
        List<Section> sections = List.of(
                신도림역_온수역_5, 온수역_역곡역_5, 역곡역_부천역_5, 부천역_중동역_5, 신도림역_신대방역_10, 신대방역_서울대입구역_15);

        PathFindable pathFindable = new ShortestPathFindable();
        Path path = pathFindable.findPath(sections, 온수역, 온수역);

        assertAll(
                () -> assertThat(path.getStations().size()).isEqualTo(1),
                () -> assertThat(path.getDistance()).isEqualTo(0)
        );
    }
}
