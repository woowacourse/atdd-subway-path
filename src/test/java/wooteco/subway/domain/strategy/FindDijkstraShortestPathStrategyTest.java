package wooteco.subway.domain.strategy;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

class FindDijkstraShortestPathStrategyTest {

    @Test
    @DisplayName("source와 target이 존재하지 않으면 예외 발생한다.")
    void findPathExceptionByNotExistStations() {
        // given
        Station station1 = new Station(1L, "오리");
        Station station2 = new Station(2L, "배카라");
        Station station3 = new Station(3L, "오카라");
        Station station4 = new Station(3L, "레넌");

        Section section = new Section(1L, 1L, station1, station2, 3);
        Sections sections = new Sections(List.of(section));

        FindPathStrategy findPathStrategy = new FindDijkstraShortestPathStrategy();

        // when & then
        assertThatThrownBy(() -> findPathStrategy.findPath(station3, station4, sections))
                .isInstanceOf(IllegalStateException.class);
    }

}
