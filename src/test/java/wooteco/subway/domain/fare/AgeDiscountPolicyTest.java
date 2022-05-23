package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AgeDiscountPolicyTest {

    @ParameterizedTest
    @DisplayName("어린이의 경우 할인을 적용한다.")
    @ValueSource(ints = {6, 12})
    void discountFareWithChild(int age) {
        DiscountPolicy discountPolicy = new AgeDiscountPolicy();
        Fare discountFare = discountPolicy.discountFare(new Fare(1250), age);

        assertThat(discountFare).isEqualTo(new Fare(800));
    }

    @ParameterizedTest
    @DisplayName("청소년의 경우 할인을 적용한다.")
    @ValueSource(ints = {13, 18})
    void discountFareWithTeenager(int age) {
        DiscountPolicy discountPolicy = new AgeDiscountPolicy();
        Fare discountFare = discountPolicy.discountFare(new Fare(1250), age);
        
        assertThat(discountFare).isEqualTo(new Fare(1070));
    }
}
