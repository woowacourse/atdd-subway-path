package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.TestFixtures.STANDARD_DISTANCE;
import static wooteco.subway.TestFixtures.동묘앞역;
import static wooteco.subway.TestFixtures.보문역;
import static wooteco.subway.TestFixtures.신당역;
import static wooteco.subway.TestFixtures.창신역;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.exception.SectionNotFoundException;

class ShortestPathStrategyTest {

    @DisplayName("최단 경로를 탐색한다.")
    @Test
    void calculateMinDistance() {
        Sections sections = createSections();
        ShortestPathStrategy pathStrategy = new ShortestPathStrategy();
        Path path = pathStrategy.findPath(신당역, 창신역, sections);
        assertThat(path.getDistance()).isEqualTo(20);
    }

    @DisplayName("최단경로 탐색시 역이 존재하지 않을 경우 에러를 발생한다.")
    @Test
    void calculateMinDistanceException() {
        Sections sections = createSections();
        ShortestPathStrategy pathStrategy = new ShortestPathStrategy();
        assertThatThrownBy(() -> pathStrategy.findPath(보문역, 신당역, sections))
                .isInstanceOf(SectionNotFoundException.class);
    }

    @DisplayName("구간이 존재하지 않을 때 경로를 조회하면 에러가 발생한다.")
    @Test
    void sectionNotFoundMinDistance() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 보문역, 창신역, STANDARD_DISTANCE);
        Sections sections = new Sections(List.of(section, section1));
        ShortestPathStrategy pathStrategy = new ShortestPathStrategy();
        assertThatThrownBy(() -> pathStrategy.findPath(신당역, 보문역, sections))
                .isInstanceOf(SectionNotFoundException.class);
    }

    @DisplayName("최단경로의 모든 정점을 가져온다.")
    @Test
    void findShortestStations() {
        Sections sections = createSections();
        ShortestPathStrategy pathStrategy = new ShortestPathStrategy();
        Path path = pathStrategy.findPath(신당역, 창신역, sections);
        assertThat(path.getStations()).containsExactly(신당역, 동묘앞역, 창신역);
    }


    @DisplayName("구간이 존재하지 않을 때 최단 경로 역을 조회하면 에러가 발생한다.")
    @Test
    void sectionNotFoundShortestStations() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 보문역, 창신역, STANDARD_DISTANCE);
        Sections sections = new Sections(List.of(section, section1));
        ShortestPathStrategy pathStrategy = new ShortestPathStrategy();
        assertThatThrownBy(() -> pathStrategy.findPath(신당역, 보문역, sections))
                .isInstanceOf(SectionNotFoundException.class);
    }

    private Sections createSections() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 동묘앞역, 창신역, STANDARD_DISTANCE);
        return new Sections(List.of(section, section1));
    }
}
