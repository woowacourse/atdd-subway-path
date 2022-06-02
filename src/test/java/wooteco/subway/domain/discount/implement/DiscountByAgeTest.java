package wooteco.subway.domain.discount.implement;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.Age;
import wooteco.subway.domain.DiscountSpecification;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.discount.DiscountStrategy;

class DiscountByAgeTest {


    @DisplayName("할인이 적용된 금액을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"5,3400", "6,1525", "12,1525", "13,2440", "18,2440", "19,3400"}, delimiter = ',')
    void discountFee_children(int age, int expectedDiscountFare) {
        // given
        DiscountSpecification specification = new DiscountSpecification(new Age(age), new Fare(3400));
        DiscountStrategy strategy = new DiscountByAge();

        // when
        Fare discountFare = strategy.discount(specification);

        // then
        assertThat(discountFare.getValue()).isEqualTo(expectedDiscountFare);
    }

}
