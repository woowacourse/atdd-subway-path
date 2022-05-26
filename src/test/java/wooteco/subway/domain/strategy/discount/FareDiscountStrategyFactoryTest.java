package wooteco.subway.domain.strategy.discount;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareDiscountStrategyFactoryTest {

    @DisplayName("6세 미만이면 BabyDiscountStrategy를 반환한다.")
    @Test
    void getBabyDiscount() {
        FareDiscountStrategy actual = FareDiscountStrategyFactory.getFareDiscountStrategy(5);

        assertThat(actual).isInstanceOf(BabyDiscountStrategy.class);
    }

    @DisplayName("6세 이상 13세 미만이면 ChildrenDiscountStrategy를 반환한다.")
    @Test
    void getChildrenDiscount() {
        FareDiscountStrategy strategyForLowerBound = FareDiscountStrategyFactory.getFareDiscountStrategy(6);
        FareDiscountStrategy strategyForUpperBound = FareDiscountStrategyFactory.getFareDiscountStrategy(12);

        assertThat(strategyForLowerBound).isEqualTo(strategyForUpperBound)
                .isInstanceOf(ChildrenDiscountStrategy.class);
    }

    @DisplayName("13세 이상 19세 미만이면 ChildrenDiscountStrategy를 반환한다.")
    @Test
    void getTeenagerDiscount() {
        FareDiscountStrategy strategyForLowerBound = FareDiscountStrategyFactory.getFareDiscountStrategy(13);
        FareDiscountStrategy strategyForUpperBound = FareDiscountStrategyFactory.getFareDiscountStrategy(18);

        assertThat(strategyForLowerBound).isEqualTo(strategyForUpperBound)
                .isInstanceOf(TeenagerDiscountStrategy.class);
    }

    @DisplayName("19세 이상이면 AdultDiscountStrategy를 반환한다.")
    @Test
    void getAdultDiscount() {
        FareDiscountStrategy actual = FareDiscountStrategyFactory.getFareDiscountStrategy(19);

        assertThat(actual).isInstanceOf(AdultDiscountStrategy.class);
    }

}
