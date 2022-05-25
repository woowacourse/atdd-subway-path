package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.Fixtures.강남_역삼_구간;
import static wooteco.subway.Fixtures.강남역;
import static wooteco.subway.Fixtures.삼성_종합운동장_구간;
import static wooteco.subway.Fixtures.선릉_삼성_구간;
import static wooteco.subway.Fixtures.역삼_선릉_구간;
import static wooteco.subway.Fixtures.이호선;
import static wooteco.subway.Fixtures.잠실새내_잠실_구간;
import static wooteco.subway.Fixtures.잠실역;
import static wooteco.subway.Fixtures.종합운동장_잠실새내_구간;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.Age;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.path.pathfinder.DijkstraShortestPathFinder;
import wooteco.subway.domain.section.Section;

class PathTest {

    private final DijkstraShortestPathFinder dijkstraShortestPathFinder = new DijkstraShortestPathFinder();

    private Path path;

    @BeforeEach
    void setUp() {
        List<Section> sections = List.of(강남_역삼_구간, 역삼_선릉_구간, 선릉_삼성_구간, 삼성_종합운동장_구간, 종합운동장_잠실새내_구간, 잠실새내_잠실_구간);
        List<Line> lines = List.of(이호선);
        path = dijkstraShortestPathFinder.getPath(sections, lines, 강남역, 잠실역);
    }

    @DisplayName("경로의 운임을 가져올 수 있다.")
    @Test
    void getFare_withExtraFareLine() {
        // given
        Age age = new Age(20);

        // when
        Fare actual = path.getFare(age);
        Fare expected = new Fare(3350);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
