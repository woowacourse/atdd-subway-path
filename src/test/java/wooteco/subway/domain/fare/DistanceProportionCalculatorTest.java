package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceProportionCalculatorTest {

    @DisplayName("거리가 주어지면 거리 비레로 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({"9,1250", "10,1250", "11,1350", "15,1350", "16,1450",
            "49,2050", "50,2050", "51,2150", "57,2150", "58,2150"})
    void 거리_비례_요금_계산(int distance, int expected) {
        DistanceProportionCalculator distanceProportionCalculator = DistanceProportionCalculator.from(distance);

        assertThat(distanceProportionCalculator.calculateFare(distance)).isEqualTo(expected);
    }

    @DisplayName("계산이 불가능한 거리인 경우 예외를 던진다.")
    @ParameterizedTest
    @CsvSource({"-1", "-2", "-10"})
    void 잘못된_거리(int distance) {
        assertThatThrownBy(() -> DistanceProportionCalculator.from(distance))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
