package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.discountpolicy.DiscountPolicyFactory;
import wooteco.subway.domain.farepolicy.FarePolicyFactory;

class FareTest {

    @DisplayName("거리에 따른 기본 운임 비용을 반환한다.")
    @ParameterizedTest
    @CsvSource({"10, 1250", "11, 1350", "16, 1450", "51, 2150"})
    void calculateBasicFare(final int distance, final int expected) {
        final Fare fare = new Fare(FarePolicyFactory.from(distance), DiscountPolicyFactory.from(22));

        final int actual = fare.calculate(distance, 0);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("추가 요금이 존재하는 경로의 거리에 따른 기본 운임 비용을 반환한다.")
    @ParameterizedTest
    @CsvSource({"10, 2150", "11, 2250", "16, 2350", "51, 3050"})
    void calculateBasicFareWithExtraFare(final int distance, final int expected) {
        final Fare fare = new Fare(FarePolicyFactory.from(distance), DiscountPolicyFactory.from(22));

        final int actual = fare.calculate(distance, 900);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("경로의 거리에 따른 어린이 운임 비용을 반환한다.")
    @ParameterizedTest
    @CsvSource({"10, 450", "11, 500", "16, 550", "51, 900"})
    void calculateChildrenFare(final int distance, final int expected) {
        final Fare fare = new Fare(FarePolicyFactory.from(distance), DiscountPolicyFactory.from(10));

        final int actual = fare.calculate(distance, 0);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("추가 요금이 존재하는 경로의 거리에 따른 어린이 운임 비용을 반환한다.")
    @ParameterizedTest
    @CsvSource({"10, 900", "11, 950", "16, 1000", "51, 1350"})
    void calculateChildrenFareWithExtraFare(final int distance, final int expected) {
        final Fare fare = new Fare(FarePolicyFactory.from(distance), DiscountPolicyFactory.from(10));

        final int actual = fare.calculate(distance, 900);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("경로의 거리에 따른 청소년 운임 비용을 반환한다.")
    @ParameterizedTest
    @CsvSource({"10, 720", "11, 800", "16, 880", "51, 1440"})
    void calculateTeenagerFare(final int distance, final int expected) {
        final Fare fare = new Fare(FarePolicyFactory.from(distance), DiscountPolicyFactory.from(15));

        final int actual = fare.calculate(distance, 0);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("추가 요금이 존재하는 경로의 거리에 따른 청소년 운임 비용을 반환한다.")
    @ParameterizedTest
    @CsvSource({"10, 1440", "11, 1520", "16, 1600", "51, 2160"})
    void calculateTeenagerFareWithExtraFare(final int distance, final int expected) {
        final Fare fare = new Fare(FarePolicyFactory.from(distance), DiscountPolicyFactory.from(15));

        final int actual = fare.calculate(distance, 900);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("경로의 거리에 따른 우대 운임 비용을 반환한다.")
    @ParameterizedTest
    @CsvSource({"10, 0", "11, 0", "16, 0", "51, 0"})
    void calculatePreferentialFare(final int distance, final int expected) {
        final Fare fare = new Fare(FarePolicyFactory.from(distance), DiscountPolicyFactory.from(65));

        final int actual = fare.calculate(distance, 0);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("추가 요금이 존재하는 경로의 거리에 따른 우대 운임 비용을 반환한다.")
    @ParameterizedTest
    @CsvSource({"10, 0", "11, 0", "16, 0", "51, 0"})
    void calculatePreferentialFareWithExtraFare(final int distance, final int expected) {
        final Fare fare = new Fare(FarePolicyFactory.from(distance), DiscountPolicyFactory.from(65));

        final int actual = fare.calculate(distance, 900);

        assertThat(actual).isEqualTo(expected);
    }
}
