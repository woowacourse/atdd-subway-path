package wooteco.subway.domain.fare.strategy.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.domain.fare.strategy.discount.AllDiscountStrategy;
import wooteco.subway.domain.fare.strategy.discount.ChildrenDiscountStrategy;
import wooteco.subway.domain.fare.strategy.discount.DiscountStrategy;
import wooteco.subway.domain.fare.strategy.discount.NoDiscountStrategy;
import wooteco.subway.domain.fare.strategy.discount.TeenagerDiscountStrategy;

class AgeDiscountStrategyFinderTest {

    @DisplayName("6세 미만인 어린이는 100% 할인을 선택한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 5})
    void findStrategy_baby(int age) {
        DiscountStrategy strategy = AgeDiscountStrategyFinder.findStrategy(age);

        assertThat(strategy).isInstanceOf(AllDiscountStrategy.class);
    }

    @DisplayName("6세 이상 13세 미만의 어린이는 50% 할인을 선택한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void findStrategy_children(int age) {
        DiscountStrategy strategy = AgeDiscountStrategyFinder.findStrategy(age);

        assertThat(strategy).isInstanceOf(ChildrenDiscountStrategy.class);
    }

    @DisplayName("13세 이상 19세 미만의 어린이는 20% 할인을 선택한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void findStrategy_teenager(int age) {
        DiscountStrategy strategy = AgeDiscountStrategyFinder.findStrategy(age);

        assertThat(strategy).isInstanceOf(TeenagerDiscountStrategy.class);
    }

    @DisplayName("19세 이상의 성인은 할인 받지 못한다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 60, 100})
    void findStrategy_noDiscount(int age) {
        DiscountStrategy strategy = AgeDiscountStrategyFinder.findStrategy(age);

        assertThat(strategy).isInstanceOf(NoDiscountStrategy.class);
    }
}
