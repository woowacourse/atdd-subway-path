package wooteco.subway.domain.farepolicy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FarePolicyTest {

    @DisplayName("거리에 따라 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"10, 1250", "11, 1350", "50, 2050", "51, 2150"})
    void calculate(final int distance, final int expected) {
        FarePolicy farePolicy = FarePolicyFactory.from(distance);

        int actual = farePolicy.calculate(distance);

        assertThat(actual).isEqualTo(expected);
    }
}
