package wooteco.subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.FareByAge;

import static org.junit.jupiter.api.Assertions.assertAll;

class FareByAgeTest {

    @Test
    @DisplayName("[우대] 6세 미만과 65세 이상은 무료이다.")
    void checkForFree() {
        //given
        int 아이_가격 = FareByAge.calculatorFare(5, 1500);
        int 노인_가격 = FareByAge.calculatorFare(65, 1500);
        //when
        //then
        assertAll(
                () -> Assertions.assertThat(아이_가격).isEqualTo(0),
                () -> Assertions.assertThat(노인_가격).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("[어린이] 6세 이상, 13세 미만은 350원을 제한 후 50%를 할인한다.")
    void checkForChild() {
        //given
        int 어린이1_가격 = FareByAge.calculatorFare(6, 1500);
        int 어린이2_가격 = FareByAge.calculatorFare(12, 1500);
        //when
        //then
        assertAll(
                () -> Assertions.assertThat(어린이1_가격).isEqualTo(575),
                () -> Assertions.assertThat(어린이2_가격).isEqualTo(575)
        );
    }

    @Test
    @DisplayName("[청소년] 13세 이상, 18세 미만은 350원을 제한 후 20%를 할인한다.")
    void checkForTeenager() {
        //given
        int 청소년1_가격 = FareByAge.calculatorFare(13, 1500);
        int 청소년2_가격 = FareByAge.calculatorFare(17, 1500);
        //when
        //then
        assertAll(
                () -> Assertions.assertThat(청소년1_가격).isEqualTo(920),
                () -> Assertions.assertThat(청소년2_가격).isEqualTo(920)
        );
    }

    @Test
    @DisplayName("[일반] 18세 이상, 65세 미만은 요금 그대로이다.")
    void checkForGeneral() {
        //given
        int 일반1_가격 = FareByAge.calculatorFare(18, 1500);
        int 일반2_가격 = FareByAge.calculatorFare(64, 1500);
        //when
        //then
        assertAll(
                () -> Assertions.assertThat(일반1_가격).isEqualTo(1500),
                () -> Assertions.assertThat(일반2_가격).isEqualTo(1500)
        );
    }
}