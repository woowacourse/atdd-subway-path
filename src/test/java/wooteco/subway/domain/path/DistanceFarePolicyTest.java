package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceFarePolicyTest {

    @DisplayName("거리에 따른 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"10,1250", "11,1350", "15,1350", "16,1450", "50,2050", "51,2150", "58,2150", "59,2250"})
    void calculateByPolicy(long distance, long expected) {
        long actual = DistanceFarePolicy.calculateByPolicy(distance);
        assertThat(actual).isEqualTo(expected);
    }
}