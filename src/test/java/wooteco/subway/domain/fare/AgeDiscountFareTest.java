package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class AgeDiscountFareTest {

    @DisplayName("6세 이상, 13세 미만의 아동은 현재 금액에서 350원을 빼고 50프로 할인")
    @Test
    void childDiscount() {
        Fare fare = new AgeDiscountFare(new BasicFare(), 10);

        int actual = fare.calculate();
        int expected = (int) ((1250 - 350) * 0.5);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("13세 이상, 19세 미만의 청소년은 현재 금액에서 350원을 빼고 20프로 할인")
    @Test
    void adolescentDiscount() {
        Fare fare = new AgeDiscountFare(new BasicFare(), 15);

        int actual = fare.calculate();
        int expected = (int) ((1250 - 350) * 0.8);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("6세 미만의 유아는 무료")
    @Test
    void babyFree() {
        Fare fare = new AgeDiscountFare(new BasicFare(), 4);

        int actual = fare.calculate();
        int expected = 0;

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("65세 이상의 노인도 무료")
    @Test
    void elderlyFree() {
        Fare fare = new AgeDiscountFare(new BasicFare(), 70);

        int actual = fare.calculate();
        int expected = 0;

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"5,0", "6,450", "12,450", "13,720", "18,720", "19,1250", "64,1250", "65,0"})
    void 경계값_검증(int age, int expected) {
        Fare fare = new AgeDiscountFare(new BasicFare(), age);

        int actual = fare.calculate();

        assertThat(actual).isEqualTo(expected);
    }
}
