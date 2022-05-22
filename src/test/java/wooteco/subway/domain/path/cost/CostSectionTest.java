package wooteco.subway.domain.path.cost;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class CostSectionTest {

    @Test
    void 거리가_10km_이상일_때_초과운임_계산() {
        CostSection costSection = new CostSection(10, 5, 100);
        CostSection nextSection = new CostSection(50, 8, 100);
        assertThat(costSection.calculateFareWithBound(nextSection, 23)).isEqualTo(300);
    }

    @Test
    void 거리가_0km_이상일_때_초과운임_계산() {
        CostSection costSection = new CostSection(0, 5, 100);
        CostSection nextSection = new CostSection(50, 8, 100);
        assertThat(costSection.calculateFareWithBound(nextSection,23)).isEqualTo(500);
    }

    @Test
    void 거리가_50km_이상일_때_초과운임_계산() {
        CostSection costSection = new CostSection(50, 6, 100);
        CostSection nextSection = new CostSection(200, 8, 100);
        assertThat(costSection.calculateFareWithBound(nextSection, 120)).isEqualTo(1200);
    }

    @Test
    void 요금_구간이_10km_이상_50km_이하일_때_최대_초과요금_계산() {
        CostSection costSection = new CostSection(10, 5, 100);
        CostSection nextSection = new CostSection(50, 8, 100);
        assertThat(costSection.calculateFareWithBound(nextSection, 51)).isEqualTo(800);
    }

    @Test
    void 요금_구간이_0km_이상_50km_이하일_때_최대_초과요금_계산() {
        CostSection costSection = new CostSection(0, 5, 100);
        CostSection nextSection = new CostSection(50, 8, 100);
        assertThat(costSection.calculateFareWithBound(nextSection, 51)).isEqualTo(1000);
    }
}
