package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {

    @DisplayName("0 미만의 값으로 요금을 생성 시 예외를 발생시킨다.")
    @Test
    void newException() {
        assertThatThrownBy(() -> new Fare(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("0 미만의 값으로 요금이 될 수 없습니다.");
    }

    @DisplayName("10000원일 때 퍼센트에 맞게 할인한다.")
    @ParameterizedTest(name = "{0}% 할인: {1}원")
    @CsvSource({"10,9000", "100,0", "0,10000", "101,0"})
    void discountPercent(int percent, int expectedValue) {
        Fare fare = new Fare(10000);

        assertThat(fare.discountPercent(percent)).isEqualTo(new Fare(expectedValue));
    }

    @DisplayName("현재 요금보다 비싼 가격을 할인할 수 없습니다.")
    @Test
    void minusException() {
        Fare fare = new Fare(1000);
        assertThatThrownBy(() -> fare.discount(1001))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현재 요금보다 비싼 가격을 할인할 수 없습니다.");
    }
}
