package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.discount.DiscountCondition;
import wooteco.subway.domain.discount.DiscountPolicy;
import wooteco.subway.domain.discount.age.ChildrenDiscountPolicy;
import wooteco.subway.domain.discount.age.TeenagerDiscountPolicy;

class FareTest {

    private final List<DiscountPolicy> discountPolicies
            = List.of(new ChildrenDiscountPolicy(), new TeenagerDiscountPolicy());

    @ParameterizedTest
    @CsvSource(value = {"9, 1250", "10, 1250", "11, 1350", "15, 1350", "50, 2050", "55, 2150", "58, 2150", "59, 2250"})
    @DisplayName("노선 추가 요금이 없을 경우 거리에 따라 요금을 계산한다.")
    void calculateFare(final int distance, final int expectedFare) {
        final Fare fare = Fare.of(distance, 0, discountPolicies, new DiscountCondition(27, distance));

        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"9, 900, 2150", "10, 300, 1550", "11, 400, 1750", "15, 1000, 2350",
            "50, 0, 2050", "55, 200, 2350", "58, 50, 2200", "59, 350, 2600"})
    @DisplayName("노선 추가 요금이 있을 경우 거리에 따라 요금을 계산 후에 노선 추가 요금을 더해서 요금을 계산한다.")
    void calculateFareWithLineExtraFare(final int distance, final int lineExtraFare, final int expectedFare) {
        final Fare fare = Fare.of(distance, lineExtraFare, discountPolicies, new DiscountCondition(27, distance));

        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"9, 900, 900", "10, 300, 600", "11, 400, 700", "15, 1000, 1000", "50, 0, 850"})
    @DisplayName("노선 추가 요금이 있을 경우 거리에 따라 요금을 계산 후에 노선 추가 요금을 더해서 요금을 계산한다.")
    void calculateFareWithLineExtraFareChildren(final int distance, final int lineExtraFare, final int expectedFare) {
        final Fare fare = Fare.of(
                distance, lineExtraFare, discountPolicies, new DiscountCondition(6, distance)
        );

        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"9, 900, 1440", "10, 300, 960", "11, 400, 1120", "15, 1000, 1600", "50, 0, 1360"})
    @DisplayName("노선 추가 요금이 있을 경우 거리에 따라 요금을 계산 후에 노선 추가 요금을 더해서 요금을 계산한다.")
    void calculateFareWithLineExtraFareTeenager(final int distance, final int lineExtraFare, final int expectedFare) {
        final Fare fare = Fare.of(
                distance, lineExtraFare, discountPolicies, new DiscountCondition(13, distance)
        );

        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }
}
