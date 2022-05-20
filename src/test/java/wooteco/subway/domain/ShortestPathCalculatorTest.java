package wooteco.subway.domain;

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
import wooteco.subway.exception.SectionNotFoundException;

public class ShortestPathCalculatorTest {

    @DisplayName("모든 구간을 생성하여 최소거리의 역을 계산한다.")
    @Test
    void findShortestStations() {
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator();
        List<Station> shortestStations = shortestPathCalculator.calculateShortestStations(createSections(), 신당역, 창신역);
        assertThat(shortestStations).hasSize(3);
    }

    @DisplayName("모든 구간을 생성하여 최소거리를 계산한다.")
    @Test
    void calculateShortestDistance() {
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator();
        int distance = shortestPathCalculator.calculateShortestDistance(createSections(), 신당역, 창신역);
        assertThat(distance).isEqualTo(20);
    }

    @DisplayName("구간이 존재하지 않을 때 경로를 조회하면 에러가 발생한다.")
    @Test
    void sectionNotFoundMinDistance() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 보문역, 창신역, STANDARD_DISTANCE);
        Sections sections = new Sections(List.of(section, section1));
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator();
        assertThatThrownBy(() -> shortestPathCalculator.calculateShortestDistance(sections, 신당역, 보문역))
                .isInstanceOf(SectionNotFoundException.class);
    }

    @DisplayName("구간이 존재하지 않을 때 최단 경로 역을 조회하면 에러가 발생한다.")
    @Test
    void sectionNotFoundShortestStations() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 보문역, 창신역, STANDARD_DISTANCE);
        Sections sections = new Sections(List.of(section, section1));
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator();
        assertThatThrownBy(() -> shortestPathCalculator.calculateShortestDistance(sections, 신당역, 보문역))
                .isInstanceOf(SectionNotFoundException.class);
    }

    private Sections createSections() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 동묘앞역, 창신역, STANDARD_DISTANCE);
        return new Sections(List.of(section, section1));
    }

}
