package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DiscountPolicyByAgeTest {

    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_DISCOUNT_FARE = 350;

    @DisplayName("18세 초과 65세 미만이라면, 일반인이다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 64})
    void generalAge(final int age) {
        assertThat(DiscountPolicyByAge.from(age)).isEqualTo(DiscountPolicyByAge.GENERAL);
    }

    @DisplayName("일반인의 요금 할인이 없습니다.")
    @Test
    void discountGeneralAge() {
        final int actual = DiscountPolicyByAge.discount(DEFAULT_FARE, DiscountPolicyByAge.GENERAL);
        assertThat(actual).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("13세 이상 18세 이하면, 청소년이다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void teenAge(final int age) {
        assertThat(DiscountPolicyByAge.from(age)).isEqualTo(DiscountPolicyByAge.TEEN);
    }

    @DisplayName("청소년의 요금은 350원을 공제하고, 20% 할인된다.")
    @Test
    void discountTeenAge() {
        final int actual = DiscountPolicyByAge.discount(DEFAULT_FARE, DiscountPolicyByAge.TEEN);
        assertThat(actual).isEqualTo((int) ((DEFAULT_FARE - DEFAULT_DISCOUNT_FARE) * 0.8));
    }

    @DisplayName("6세 이상 13세 미만이라면, 어린이이다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void childAge(final int age) {
        assertThat(DiscountPolicyByAge.from(age)).isEqualTo(DiscountPolicyByAge.CHILD);
    }

    @DisplayName("어린이의 요금은 350원을 공제하고, 50% 할인된다.")
    @Test
    void discountChildAge() {
        final int actual = DiscountPolicyByAge.discount(DEFAULT_FARE, DiscountPolicyByAge.CHILD);
        assertThat(actual).isEqualTo((int) ((DEFAULT_FARE - DEFAULT_DISCOUNT_FARE) * 0.5));
    }

    @DisplayName("65세 이상이라면, 노인이다.")
    @Test
    void seniorAge() {
        assertThat(DiscountPolicyByAge.from(65)).isEqualTo(DiscountPolicyByAge.SENIOR);
    }

    @DisplayName("노인의 요금은 무료이다.")
    @Test
    void discountSeniorAge() {
        final int actual = DiscountPolicyByAge.discount(DEFAULT_FARE, DiscountPolicyByAge.SENIOR);
        assertThat(actual).isEqualTo(0);
    }

    @DisplayName("6세 미만이라면, 유아이다.")
    @Test
    void toddlerAge() {
        assertThat(DiscountPolicyByAge.from(5)).isEqualTo(DiscountPolicyByAge.TODDLER);
    }

    @DisplayName("유아의 요금은 무료이다.")
    @Test
    void discountToddlerAge() {
        final int actual = DiscountPolicyByAge.discount(DEFAULT_FARE, DiscountPolicyByAge.TODDLER);
        assertThat(actual).isEqualTo(0);
    }
}
