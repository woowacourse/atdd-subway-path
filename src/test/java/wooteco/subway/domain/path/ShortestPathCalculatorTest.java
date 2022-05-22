package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.common.TestFixtures.STANDARD_DISTANCE;
import static wooteco.subway.common.TestFixtures.동묘앞역;
import static wooteco.subway.common.TestFixtures.보문역;
import static wooteco.subway.common.TestFixtures.신당역;
import static wooteco.subway.common.TestFixtures.창신역;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.SectionNotFoundException;

@SpringBootTest
public class ShortestPathCalculatorTest {

    @Autowired
    private ShortestPathCalculator shortestPathCalculator;

    @DisplayName("모든 구간을 생성하여 최소거리의 역을 계산한다.")
    @Test
    void findShortestStations() {
        List<Station> shortestStations = shortestPathCalculator.calculateShortestStations(createSections(),
                new Path(신당역, 창신역));
        assertThat(shortestStations).hasSize(3);
    }

    @DisplayName("모든 구간을 생성하여 최소거리를 계산한다.")
    @Test
    void calculateShortestDistance() {
        int distance = shortestPathCalculator.calculateShortestDistance(createSections(), new Path(신당역, 창신역));
        assertThat(distance).isEqualTo(20);
    }

    @DisplayName("구간이 존재하지 않을 때 경로를 조회하면 에러가 발생한다.")
    @Test
    void sectionNotFoundMinDistance() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 보문역, 창신역, STANDARD_DISTANCE);
        Sections sections = new Sections(List.of(section, section1));
        assertThatThrownBy(() -> shortestPathCalculator.calculateShortestDistance(sections, new Path(신당역, 보문역)))
                .isInstanceOf(SectionNotFoundException.class);
    }

    @DisplayName("구간이 존재하지 않을 때 최단 경로 역을 조회하면 에러가 발생한다.")
    @Test
    void sectionNotFoundShortestStations() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 보문역, 창신역, STANDARD_DISTANCE);
        Sections sections = new Sections(List.of(section, section1));
        assertThatThrownBy(() -> shortestPathCalculator.calculateShortestDistance(sections, new Path(신당역, 보문역)))
                .isInstanceOf(SectionNotFoundException.class);
    }

    @DisplayName("지나온 모든 가중치를 조회한다.")
    @Test
    void findPassedEdges() {
        Sections sections = createSections();
        sections.add(new Section(3L, 2L, 창신역, 보문역, STANDARD_DISTANCE));

        List<ShortestPathEdge> edges = shortestPathCalculator.findPassedEdges(sections, new Path(신당역, 보문역));

        assertAll(
                () -> assertThat(edges).extracting("lineId").containsExactly(1L, 1L, 2L),
                () -> assertThat(edges).hasSize(3)
        );
    }

    private Sections createSections() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 동묘앞역, 창신역, STANDARD_DISTANCE);
        return new Sections(List.of(section, section1));
    }

}
