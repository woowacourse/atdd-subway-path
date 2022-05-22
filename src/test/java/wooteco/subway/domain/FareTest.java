package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @ParameterizedTest
    @CsvSource(value = {"9, 1250", "10, 1250", "11, 1350", "15, 1350", "50, 2050", "55, 2150", "58, 2150", "59, 2250"})
    @DisplayName("노선 추가 요금이 없을 경우 거리에 따라 요금을 계산한다.")
    void calculateFare(final int distance, final int expectedFare) {
        final Fare fare = Fare.of(distance, 0, DiscountPolicy.OTHER);

        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"9, 900, 2150", "10, 300, 1550", "11, 400, 1750", "15, 1000, 2350",
            "50, 0, 2050", "55, 200, 2350", "58, 50, 2200", "59, 350, 2600"})
    @DisplayName("노선 추가 요금이 있을 경우 거리에 따라 요금을 계산 후에 노선 추가 요금을 더해서 요금을 계산한다.")
    void calculateFareWithLineExtraFare(final int distance, final int lineExtraFare, final int expectedFare) {
        final Fare fare = Fare.of(distance, lineExtraFare, DiscountPolicy.OTHER);

        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"9, 900, 1250", "10, 300, 950", "11, 400, 1100", "15, 1000, 1400", "50, 0, 1600"})
    @DisplayName("노선 추가 요금이 있을 경우 거리에 따라 요금을 계산 후에 노선 추가 요금을 더해서 요금을 계산한다.")
    void calculateFareWithLineExtraFareChildren(final int distance, final int lineExtraFare, final int expectedFare) {
        final Fare fare = Fare.of(distance, lineExtraFare, DiscountPolicy.CHILDREN);

        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"9, 900, 1790", "10, 300, 1310", "11, 400, 1490", "15, 1000, 1970", "50, 0, 1870"})
    @DisplayName("노선 추가 요금이 있을 경우 거리에 따라 요금을 계산 후에 노선 추가 요금을 더해서 요금을 계산한다.")
    void calculateFareWithLineExtraFareTeenager(final int distance, final int lineExtraFare, final int expectedFare) {
        final Fare fare = Fare.of(distance, lineExtraFare, DiscountPolicy.TEENAGER);

        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }
}
