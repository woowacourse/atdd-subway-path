package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.domain.fixtures.TestFixtures.강남;
import static wooteco.subway.domain.fixtures.TestFixtures.강남_삼성;
import static wooteco.subway.domain.fixtures.TestFixtures.건대;
import static wooteco.subway.domain.fixtures.TestFixtures.건대_성수;
import static wooteco.subway.domain.fixtures.TestFixtures.삼성;
import static wooteco.subway.domain.fixtures.TestFixtures.삼성_건대;
import static wooteco.subway.domain.fixtures.TestFixtures.성수;
import static wooteco.subway.domain.fixtures.TestFixtures.왕십리_합정;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    @Test
    @DisplayName("그래프를 활용해 출발역과 도착역까지의 패스 정보를 구한다.")
    void create() {
        SubwayGraph graph = new SubwayGraph(List.of(강남_삼성, 삼성_건대, 건대_성수, 왕십리_합정));
        Path path = Path.create(graph, 강남, 성수, 20);

        assertThat(path.getDistance()).isEqualTo(38);
        assertThat(path.getStations()).containsOnly(강남,삼성,건대,성수);
        assertThat(path.getFare()).isEqualTo(1250+100+600);
    }
}
