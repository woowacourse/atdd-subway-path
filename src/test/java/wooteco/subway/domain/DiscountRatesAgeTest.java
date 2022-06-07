package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DiscountRatesAgeTest {
    @ParameterizedTest(name = "{0}을 입력하면 어린이 요금제도를 생성한다.")
    @ValueSource(ints = {6, 10, 12})
    void DiscountRatesAgeChild(int age) {
        //when
        DiscountRatesAge discountRatesAge = DiscountRatesAge.valueOf(age);
        //then
        assertThat(discountRatesAge).isEqualTo(DiscountRatesAge.CHILD);
    }

    @ParameterizedTest(name = "{0}을 입력하면 청소년 요금제도를 생성한다.")
    @ValueSource(ints = {13, 18, 15})
    void DiscountRatesAgeTeenager(int age) {
        //when
        DiscountRatesAge discountRatesAge = DiscountRatesAge.valueOf(age);
        //then
        assertThat(discountRatesAge).isEqualTo(DiscountRatesAge.TEENAGER);
    }

    @ParameterizedTest(name = "{0}을 입력하면 성인 요금제도를 생성한다.")
    @ValueSource(ints = {19, 24})
    void DiscountRatesAgeAdult(int age){
        //when
        DiscountRatesAge discountRatesAge = DiscountRatesAge.valueOf(age);
        //then
        assertThat(discountRatesAge).isEqualTo(DiscountRatesAge.ADULT);
    }

    @ParameterizedTest(name = "{0}을 입력하면 에러를 발생시킨다.")
    @ValueSource(ints = {0, -1})
    void DiscountRatesAgeError(int age) {
        assertThatThrownBy(() -> DiscountRatesAge.valueOf(age))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("적절한 연령이 입력되지 않았습니다.");
    }
}