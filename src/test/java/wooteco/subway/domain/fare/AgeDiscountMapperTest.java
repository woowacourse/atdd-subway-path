package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AgeDiscountMapperTest {

    @ParameterizedTest
    @DisplayName("나이 입력에 대해 우대 금액을 반환한다. - 유아")
    @ValueSource(ints = {0, 5})
    void discount_infant(int age) {
        assertThat(AgeDiscountMapper.discount(age, 1350)).isEqualTo(0);
    }

    @ParameterizedTest
    @DisplayName("나이 입력에 대해 할인된 금액을 반환한다. - 어린이")
    @ValueSource(ints = {6, 12})
    void discount_child(int age) {
        assertThat(AgeDiscountMapper.discount(age, 1350)).isEqualTo(500);
    }

    @ParameterizedTest
    @DisplayName("나이 입력에 대해 할인된 금액을 반환한다. - 청소년")
    @ValueSource(ints = {13, 18})
    void discount_adolescent(int age) {
        assertThat(AgeDiscountMapper.discount(age, 1350)).isEqualTo(800);
    }

    @ParameterizedTest
    @DisplayName("나이 입력에 대해 일반 금액을 반환한다. - 어른")
    @ValueSource(ints = {19, 64})
    void discount_adult(int age) {
        assertThat(AgeDiscountMapper.discount(age, 1350)).isEqualTo(1350);
    }

    @ParameterizedTest
    @DisplayName("나이 입력에 대해 우대 금액을 반환한다. - 노인")
    @ValueSource(ints = {65, Integer.MAX_VALUE})
    void discount_elder(int age) {
        assertThat(AgeDiscountMapper.discount(age, 1350)).isEqualTo(0);
    }

    @ParameterizedTest
    @DisplayName("존재하지 않는 나이 입력에 대해 예외를 반환한다.")
    @ValueSource(ints = {-1, -2})
    void discount_exception(int age) {
        assertThatThrownBy(() -> AgeDiscountMapper.discount(age, 1350))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올 수 없는 나이입니다.");
    }
}
