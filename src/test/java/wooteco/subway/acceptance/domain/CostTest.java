package wooteco.subway.acceptance.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.Cost;

class CostTest {
    @DisplayName("거리를 전달받아 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"10,1250", "12,1350", "16,1450", "30,1650", "50,2050", "58,2150"})
    void calculate_cost_with_distance(int distance, int expectedCost) {
        // when
        Cost createdCost = Cost.from(distance);
        int actual = createdCost.getCost();

        // then
        assertThat(actual).isEqualTo(expectedCost);
    }
}
