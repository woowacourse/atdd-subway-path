package wooteco.subway.acceptance.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.Cost;

class CostTest {
    @DisplayName("거리에 비례해 계산된 요금이 정확한지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"10,1250", "11,1350", "16,1450", "50,2050", "51,2150", "59, 2250"})
    void calculate_cost_with_distance(int distance, int expectedCost) {
        // when
        Cost createdCost = Cost.from(distance);
        int actual = createdCost.getCost();

        // then
        assertThat(actual).isEqualTo(expectedCost);
    }
}
