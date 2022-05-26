package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DiscountRatesAgeTest {
    @ParameterizedTest
    @ValueSource(ints = {6, 10, 12})
    @DisplayName("6세 이상, 13세 미만이면 어린이 요금제도를 생성한다")
    void DiscountRatesAgeChild(int age){
        //when
        DiscountRatesAge discountRatesAge = DiscountRatesAge.valueOf(age);
        //then
        assertThat(discountRatesAge).isEqualTo(DiscountRatesAge.CHILD);
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 18, 15})
    @DisplayName("13세 이상, 19세 미만이면 청소년 요금제도를 생성한다")
    void DiscountRatesAgeTeenager(int age){
        //when
        DiscountRatesAge discountRatesAge = DiscountRatesAge.valueOf(age);
        //then
        assertThat(discountRatesAge).isEqualTo(DiscountRatesAge.TEENAGER);
    }

    @ParameterizedTest
    @ValueSource(ints = {19, 24})
    @DisplayName("19세 이상이면 성인 요금제도를 생성한다")
    void DiscountRatesAgeAdult(int age){
        //when
        DiscountRatesAge discountRatesAge = DiscountRatesAge.valueOf(age);
        //then
        assertThat(discountRatesAge).isEqualTo(DiscountRatesAge.ADULT);
    }
}