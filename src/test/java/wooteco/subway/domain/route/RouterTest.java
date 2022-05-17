package wooteco.subway.domain.route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.domain.TestFixture.SECTIONS;
import static wooteco.subway.domain.TestFixture.강남역;
import static wooteco.subway.domain.TestFixture.교대역;
import static wooteco.subway.domain.TestFixture.선릉역;
import static wooteco.subway.domain.TestFixture.역삼역;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.section.Section;

class RouterTest {

    @DisplayName("구간 정보와 출발지 도착지를 이용해 최단 경로 및 거리를 계산한다")
    @Test
    void findShortestRoute() {
        // given
        List<Section> sections = new ArrayList<>(SECTIONS);
        Collections.shuffle(sections);

        // when
        Router router = new Router();
        Route shortestRoute = router.findShortestRoute(sections, 교대역, 선릉역);

        // then
        assertAll(
                () -> assertThat(shortestRoute.getRoute()).containsExactly(교대역, 강남역, 역삼역, 선릉역),
                () -> assertThat(shortestRoute.getDistance()).isEqualTo(18)
        );
    }
}
