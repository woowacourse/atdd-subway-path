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

public class PathTest {

    @DisplayName("최단 경로를 탐색한다.")
    @Test
    void calculateMinDistance() {
        Sections sections = createSections();
        Path path = Path.of(신당역, 창신역, sections);
        assertThat(path.calculateMinDistance()).isEqualTo(20);
    }

    @DisplayName("최단경로 탐색시 역이 존재하지 않을 경우 에러를 발생한다.")
    @Test
    void calculateMinDistanceException() {
        Sections sections = createSections();
        Path path = Path.of(보문역, 신당역, sections);
        assertThatThrownBy(path::calculateMinDistance)
                .isInstanceOf(SectionNotFoundException.class);
    }

    @DisplayName("구간이 존재하지 않을 때 경로를 조회하면 에러가 발생한다.")
    @Test
    void sectionNotFoundMinDistance() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 보문역, 창신역, STANDARD_DISTANCE);
        Sections sections = new Sections(List.of(section, section1));
        Path path = Path.of(신당역, 보문역, sections);
        assertThatThrownBy(path::calculateMinDistance)
                .isInstanceOf(SectionNotFoundException.class);
    }

    @DisplayName("최단경로의 모든 정점을 가져온다.")
    @Test
    void findShortestStations() {
        Sections sections = createSections();
        Path path = Path.of(신당역, 창신역, sections);
        assertThat(path.findShortestStations()).containsExactly(신당역, 동묘앞역, 창신역);
    }


    @DisplayName("구간이 존재하지 않을 때 최단 경로 역을 조회하면 에러가 발생한다.")
    @Test
    void sectionNotFoundShortestStations() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 보문역, 창신역, STANDARD_DISTANCE);
        Sections sections = new Sections(List.of(section, section1));
        Path path = Path.of(신당역, 보문역, sections);
        assertThatThrownBy(path::findShortestStations)
                .isInstanceOf(SectionNotFoundException.class);
    }

    @DisplayName("10km 이하의 요금을 계산한다.")
    @Test
    void calculateDefaultFare() {
        Sections sections = createSections();
        Path path = Path.of(신당역, 보문역, sections);
        assertThat(path.calculateFare(STANDARD_DISTANCE)).isEqualTo(1250);
    }

    @DisplayName("10km 1회 초과 요금을 계산한다.")
    @Test
    void calculateFareOverDefaultDistanceSingle() {
        Sections sections = createSections();
        Path path = Path.of(신당역, 보문역, sections);
        assertThat(path.calculateFare(STANDARD_DISTANCE + 1)).isEqualTo(1350);
    }

    @DisplayName("10km 2회 초과 요금을 계산한다.")
    @Test
    void calculateFareOverDefaultDistanceDouble() {
        Sections sections = createSections();
        Path path = Path.of(신당역, 보문역, sections);
        assertThat(path.calculateFare(STANDARD_DISTANCE + 6)).isEqualTo(1450);
    }

    @DisplayName("50km 1회 초과 요금을 계산한다.")
    @Test
    void calculateFareOverMaxDistanceSingle() {
        Sections sections = createSections();
        Path path = Path.of(신당역, 보문역, sections);
        assertThat(path.calculateFare(51)).isEqualTo(2150);
    }

    @DisplayName("50km 2회 초과 요금을 계산한다.")
    @Test
    void calculateFareOverMaxDistanceDouble() {
        Sections sections = createSections();
        Path path = Path.of(신당역, 보문역, sections);
        assertThat(path.calculateFare(59)).isEqualTo(2250);
    }

    private Sections createSections() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 동묘앞역, 창신역, STANDARD_DISTANCE);
        return new Sections(List.of(section, section1));
    }
}
