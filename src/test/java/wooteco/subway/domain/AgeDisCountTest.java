package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.domain.AgeDisCount.ADULT;
import static wooteco.subway.domain.AgeDisCount.BABY;
import static wooteco.subway.domain.AgeDisCount.CHILDREN;
import static wooteco.subway.domain.AgeDisCount.TEENAGER;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AgeDisCountTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 5})
    @DisplayName("0살부터 5살까지는 BABY다")
    void ageDiscountBaby(final int age) {
        assertThat(AgeDisCount.from(age)).isEqualTo(BABY);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 10, 12})
    @DisplayName("6살부터 12살까지는 CHILDREN이다")
    void ageDiscountChildren(final int age) {
        assertThat(AgeDisCount.from(age)).isEqualTo(CHILDREN);
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 15, 18})
    @DisplayName("13살부터 18살까지는 TEENAGER다")
    void ageDiscountTeenager(final int age) {
        assertThat(AgeDisCount.from(age)).isEqualTo(TEENAGER);
    }

    @ParameterizedTest
    @ValueSource(ints = {19, 25})
    @DisplayName("19살부터는 ADULT다")
    void ageDiscountAdult(final int age) {
        assertThat(AgeDisCount.from(age)).isEqualTo(ADULT);
    }

    @Test
    @DisplayName("BABY는 할인된 가격이 0원이다.")
    void babyDiscountedMoney() {
        assertThat(BABY.discountedMoney(1_250)).isEqualTo(0);
    }

    @Test
    @DisplayName("CHILDREN은 할인된 가격이 350원 공제 후 50%다.")
    void childrenDiscountedMoney() {
        assertThat(CHILDREN.discountedMoney(1_250)).isEqualTo(450);
    }

    @Test
    @DisplayName("TEENAGER은 할인된 가격이 350원 공제 후 50%다.")
    void teenagerDiscountedMoney() {
        assertThat(TEENAGER.discountedMoney(1_250)).isEqualTo(720);
    }

    @Test
    @DisplayName("ADULT는 할인된 가격이 그대로다.")
    void adultDiscountedMoney() {
        assertThat(ADULT.discountedMoney(1_250)).isEqualTo(1_250);
    }
}
