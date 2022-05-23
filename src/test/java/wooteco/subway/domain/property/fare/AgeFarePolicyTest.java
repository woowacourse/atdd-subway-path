package wooteco.subway.domain.property.fare;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.property.Age;

class AgeFarePolicyTest {

    @Test
    @DisplayName("어린이는 운임에서 350원을 공제한 후 0.5를 곱한다.")
    public void discountWithChild() {
        // given
        AgeFarePolicy policy = new AgeFarePolicy(new Age(8));
        Fare fare = new Fare(1250);
        // when
        final Fare discounted = policy.apply(fare);
        // then
        assertThat(discounted.getAmount()).isEqualTo(450);
    }

    @Test
    @DisplayName("청소년은 운임에서 350원을 공제한 후 0.8을 곱한다")
    public void discountWithTeenager() {
        // given
        AgeFarePolicy policy = new AgeFarePolicy(new Age(15));
        Fare fare = new Fare(1250);
        // when
        final Fare discounted = policy.apply(fare);
        // then
        assertThat(discounted.getAmount()).isEqualTo(720);
    }

    @Test
    @DisplayName("성인은 나이에 따른 할인이 적용되지 않는다.")
    public void discountWithAdult() {
        // given
        AgeFarePolicy policy = new AgeFarePolicy(new Age(20));
        Fare fare = new Fare(1250);
        // when
        final Fare discounted = policy.apply(fare);
        // then
        assertThat(discounted.getAmount()).isEqualTo(1250);
    }
}