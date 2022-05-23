package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.ParameterizedTest.ARGUMENTS_WITH_NAMES_PLACEHOLDER;
import static org.junit.jupiter.params.ParameterizedTest.DISPLAY_NAME_PLACEHOLDER;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.fare.strategy.discount.AllDiscountStrategy;
import wooteco.subway.domain.fare.strategy.discount.ChildrenDiscountStrategy;
import wooteco.subway.domain.fare.strategy.discount.NoDiscountStrategy;
import wooteco.subway.domain.fare.strategy.discount.TeenagerDiscountStrategy;
import wooteco.subway.domain.fare.strategy.fare.DefaultFareStrategy;

class FareCalculatorTest {

    private static final String PARAMETERIZED_NAME =
            DISPLAY_NAME_PLACEHOLDER + "[" + ARGUMENTS_WITH_NAMES_PLACEHOLDER + "]";

    @DisplayName("거리로 요금을 계산한다.")
    @ParameterizedTest(name = PARAMETERIZED_NAME)
    @CsvSource(value = {"10, 1250", "11, 1350", "50, 2050", "51, 2150"})
    void calculateFareByDistance(int distance, int resultFare) {
        FareCalculator fareCalculator = new FareCalculator(new DefaultFareStrategy(), new NoDiscountStrategy());
        double fare = fareCalculator.calculate(distance, 0);
        assertThat(fare).isEqualTo(resultFare);
    }

    @DisplayName("거리와 추가요금을 적용하여 요금을 계산한다.")
    @ParameterizedTest(name = PARAMETERIZED_NAME)
    @CsvSource(value = {"10, 1000, 2250", "11, 1000, 2350", "50, 1000, 3050", "51, 1000, 3150"})
    void calculateFareByDistanceAndExtraFare(int distance, int extraFare, int resultFare) {
        FareCalculator fareCalculator = new FareCalculator(new DefaultFareStrategy(), new NoDiscountStrategy());
        double fare = fareCalculator.calculate(distance, extraFare);
        assertThat(fare).isEqualTo(resultFare);
    }

    @DisplayName("유아 요금 할인을 적용하여 요금을 계산한다.")
    @ParameterizedTest(name = PARAMETERIZED_NAME)
    @CsvSource(value = {"10, 1000, 0", "11, 1000, 0", "50, 1000, 0", "51, 1000, 0"})
    void calculateFareWithBabyDiscount(int distance, int extraFare, int resultFare) {
        FareCalculator fareCalculator = new FareCalculator(new DefaultFareStrategy(), new AllDiscountStrategy());
        double fare = fareCalculator.calculate(distance, extraFare);
        assertThat(fare).isEqualTo(resultFare);
    }

    @DisplayName("어린이 요금 할인을 적용하여 요금을 계산한다.")
    @ParameterizedTest(name = PARAMETERIZED_NAME)
    @CsvSource(value = {"10, 1000, 950", "11, 1000, 1000", "50, 1000, 1350", "51, 1000, 1400"})
    void calculateFareWithChildrenDiscount(int distance, int extraFare, int resultFare) {
        FareCalculator fareCalculator = new FareCalculator(new DefaultFareStrategy(), new ChildrenDiscountStrategy());
        double fare = fareCalculator.calculate(distance, extraFare);
        assertThat(fare).isEqualTo(resultFare);
    }

    @DisplayName("청소년 요금 할인을 적용하여 요금을 계산한다.")
    @ParameterizedTest(name = PARAMETERIZED_NAME)
    @CsvSource(value = {"10, 1000, 1520", "11, 1000, 1600", "50, 1000, 2160", "51, 1000, 2240"})
    void calculateFareWithTeenagerDiscount(int distance, int extraFare, int resultFare) {
        FareCalculator fareCalculator = new FareCalculator(new DefaultFareStrategy(), new TeenagerDiscountStrategy());
        double fare = fareCalculator.calculate(distance, extraFare);
        assertThat(fare).isEqualTo(resultFare);
    }
}
