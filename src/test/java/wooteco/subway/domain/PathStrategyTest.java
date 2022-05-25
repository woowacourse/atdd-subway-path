package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.utils.TestFixture.역삼역;
import static wooteco.subway.utils.TestFixture.정자역;
import static wooteco.subway.utils.TestFixture.판교역;
import static wooteco.subway.utils.TestFixture.서현역;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.path.PathStrategy;
import wooteco.subway.domain.path.ShortestPathStrategy;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

class PathStrategyTest {

    private static final Long lineId1 = 1L;
    private static final Long lineId2 = 2L;

    private static final List<Section> sections = List.of(
            new Section(1L, lineId2, 역삼역, 서현역, 1),
            new Section(2L, lineId1, 역삼역, 서현역, 2),
            new Section(3L, lineId1, 서현역, 정자역, 2),
            new Section(4L, lineId1, 정자역, 판교역, 2)
    );

    @DisplayName("최단 경로 찾아주는 객체를 초기화한다.")
    @Test
    void create() {
        // given
        PathStrategy pathStrategy = new ShortestPathStrategy(sections);

        // when
        List<Station> result = pathStrategy.findPath(판교역, 역삼역);

        // then
        List<Station> expected = List.of(판교역, 정자역, 서현역, 역삼역);
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("거리 계산하기")
    @Test
    void calculateDistance() {
        // given
        PathStrategy pathStrategy = new ShortestPathStrategy(sections);

        // when
        int result = pathStrategy.calculateDistance(판교역, 역삼역);

        // then
        assertThat(result).isEqualTo(5);
    }

    @DisplayName("순회하는 노선 목록 찾기")
    @Test
    void findLines() {
        PathStrategy pathStrategy = new ShortestPathStrategy(sections);
        List<Long> ids = pathStrategy.findLineIds(역삼역, 정자역);

        assertThat(ids).contains(lineId1, lineId2);
    }
}
