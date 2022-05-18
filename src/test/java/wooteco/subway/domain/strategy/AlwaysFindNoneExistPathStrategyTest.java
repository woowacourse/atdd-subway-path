package wooteco.subway.domain.strategy;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.NotFoundException;

class AlwaysFindNoneExistPathStrategyTest {

    private final FindPathStrategy findPathStrategy = new AlwaysFindNoneExistPathStrategy();

    @Test
    @DisplayName("항상 경로를 찾을 수 없어 예외 발생")
    void findPathException() {
        Station source = new Station(1L, "강남역");
        Station target = new Station(2L, "선릉역");
        Sections sections = new Sections(List.of(new Section(1L, 1L, source, target, 10)));

        assertThatThrownBy(() -> findPathStrategy.findPath(source, target, sections))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("갈 수 있는 경로를 찾을 수 없습니다.");
    }
}
