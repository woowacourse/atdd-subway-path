package wooteco.subway.domain.strategy.fare.discount;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DiscountManagerTest {

    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    @DisplayName("청소년의 할인 금액을 계산한다")
    void calculateTeenagerDiscount(int age) {
        DiscountManager discountManager = DiscountManagerFactory.createDiscountManager();

        int discountPrice = discountManager.calculateDiscount(age, 1350);

        assertThat(discountPrice).isEqualTo(200);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    @DisplayName("어린이의 할인 금액을 계산한다")
    void calculateChildDiscount(int age) {
        DiscountManager discountManager = DiscountManagerFactory.createDiscountManager();

        int discountPrice = discountManager.calculateDiscount(age, 1350);

        assertThat(discountPrice).isEqualTo(500);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 19})
    @DisplayName("할인 대상이 아닌 인원의 할인 금액을 계산한다")
    void calculateOtherDiscount(int age) {
        DiscountManager discountManager = DiscountManagerFactory.createDiscountManager();

        int discountPrice = discountManager.calculateDiscount(age, 1350);

        assertThat(discountPrice).isEqualTo(0);
    }
}
