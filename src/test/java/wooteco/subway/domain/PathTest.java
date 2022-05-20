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
import wooteco.subway.exception.SubwayException;

class PathTest {

    @DisplayName("최소 거리를 계산한다.")
    @Test
    void calculateMinDistance() {
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator();
        Path path = new Path(shortestPathCalculator, 신당역, 창신역);
        assertThat(path.calculateMinDistance(createSections())).isEqualTo(20);
    }

    @DisplayName("최소 거리에 속한 역을 반환한다.")
    @Test
    void findShortestStations() {
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator();
        Path path = new Path(shortestPathCalculator, 신당역, 창신역);
        assertThat(path.findShortestStations(createSections())).hasSize(3);
    }

    @DisplayName("동일한 역이 들어올 시 예외가 발생한다.")
    @Test
    void validateSameStations() {
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator();
        assertThatThrownBy(() -> new Path(shortestPathCalculator, 신당역, 신당역))
                .isInstanceOf(SubwayException.class);
    }


    @DisplayName("10km 이하의 요금을 계산한다.")
    @Test
    void calculateDefaultFare() {
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator();
        Path path = new Path(shortestPathCalculator, 신당역, 동묘앞역);
        assertThat(path.calculateFare(createSections())).isEqualTo(1250);
    }

    @DisplayName("10km 1회 초과 요금을 계산한다.")
    @Test
    void calculateFareOverDefaultDistanceSingle() {
        Sections sections = createSections();
        sections.add(new Section(3L, 1L, 창신역, 보문역, 1));
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator();
        Path path = new Path(shortestPathCalculator, 동묘앞역, 보문역);
        assertThat(path.calculateFare(sections)).isEqualTo(1350);
    }

    @DisplayName("10km 2회 초과 요금을 계산한다.")
    @Test
    void calculateFareOverDefaultDistanceDouble() {
        Sections sections = createSections();
        sections.add(new Section(3L, 1L, 창신역, 보문역, 6));
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator();
        Path path = new Path(shortestPathCalculator, 동묘앞역, 보문역);
        assertThat(path.calculateFare(sections)).isEqualTo(1450);
    }

    @DisplayName("50km 1회 초과 요금을 계산한다.")
    @Test
    void calculateFareOverMaxDistanceSingle() {
        Sections sections = createSections();
        sections.add(new Section(3L, 1L, 창신역, 보문역, 41));
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator();
        Path path = new Path(shortestPathCalculator, 동묘앞역, 보문역);
        assertThat(path.calculateFare(sections)).isEqualTo(2150);
    }

    @DisplayName("50km 2회 초과 요금을 계산한다.")
    @Test
    void calculateFareOverMaxDistanceDouble() {
        Sections sections = createSections();
        sections.add(new Section(3L, 1L, 창신역, 보문역, 49));
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator();
        Path path = new Path(shortestPathCalculator, 동묘앞역, 보문역);
        assertThat(path.calculateFare(sections)).isEqualTo(2250);
    }

    private Sections createSections() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 동묘앞역, 창신역, STANDARD_DISTANCE);
        return new Sections(List.of(section, section1));
    }
}
