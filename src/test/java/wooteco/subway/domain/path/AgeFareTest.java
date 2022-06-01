package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AgeFareTest {

    @DisplayName("6세 미만이면 요금이 무료이다")
    @Test
    void calculate_free_5() {
        Fare fare = new Fare(1250);
        Fare discountedFare = AgeFare.calculate(fare, new Age(5));

        assertThat(discountedFare.getValue()).isEqualTo(0);
    }

    @DisplayName("65세 이상이면 요금이 무료이다")
    @Test
    void calculate_free_65() {
        Fare fare = new Fare(1250);
        Fare discountedFare = AgeFare.calculate(fare, new Age(65));

        assertThat(discountedFare.getValue()).isEqualTo(0);
    }

    @ParameterizedTest(name = "19세 이상 65세 미만이면 할인이 적용되지 않는다 : {0}")
    @ValueSource(ints = {19, 64})
    void calculate_other_1250(int age) {
        Fare fare = new Fare(1250);
        Fare discountedFare = AgeFare.calculate(fare, new Age(age));

        assertThat(discountedFare.getValue()).isEqualTo(1250);
    }

    @DisplayName("1250원에 청소년 운임 할인을 적용하면 720원이다.")
    @Test
    void calculate_teenager_1250() {
        Fare fare = new Fare(1250);
        Fare discountedFare = AgeFare.calculate(fare, new Age(13));

        assertThat(discountedFare.getValue()).isEqualTo(720);
    }

    @DisplayName("1250원에 어린이 운임 할인을 적용하면 450원이다.")
    @Test
    void calculate_kid() {
        Fare fare = new Fare(1250);
        Fare discountedFare = AgeFare.calculate(fare, new Age(6));

        assertThat(discountedFare.getValue()).isEqualTo(450);
    }

    @DisplayName("2150원에 청소년 운임 할인을 적용하면 1440원이다.")
    @Test
    void calculate_teenager_2150() {
        Fare fare = new Fare(2150);
        Fare discountedFare = AgeFare.calculate(fare, new Age(18));

        assertThat(discountedFare.getValue()).isEqualTo(1440);
    }

    @DisplayName("2150원에 어린이 운임 할인을 적용하면 900원이다.")
    @Test
    void calculate_kid_2150() {
        Fare fare = new Fare(2150);
        Fare discountedFare = AgeFare.calculate(fare, new Age(12));

        assertThat(discountedFare.getValue()).isEqualTo(900);
    }
}