package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {

    @DisplayName("거리에 따른 요금 정책에 따라 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(
        value = {"10:1250", "11:1350", "16:1450", "40:1850", "50:2050", "58:2150"},
        delimiter = ':')
    void fare(int distance, int fare) {
        // when
        int actual = Fare.of(distance, 0, 19).calculate();

        // then
        assertThat(actual).isEqualTo(fare);
    }

    @DisplayName("노선별 추가 요금이 포함된 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"58:900:3050", "58:500:2650"}, delimiter = ':')
    void fareWithOverFare(int distance, int overFare, int expected) {
        //when
        int actual = Fare.of(distance, overFare, 19).calculate();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("연령별 할인 요금이 적용된 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"58:5:0", "58:6:900", "58:12:900", "58:13:1440", "58:18:1440", "58:19:2150"},
        delimiter = ':')
    void fareWithAgeDiscount(int distance, int age, int expected) {
        //when
        int actual = Fare.of(distance, 0, age).calculate();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("노선별 추가 요금과 연령별 할인 요금이 적용된 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"58:500:5:0", "58:500:6:1150", "58:500:12:1150",
        "58:500:13:1840", "58:500:18:1840", "58:500:19:2650"},
        delimiter = ':')
    void fareWithOverFareAndAgeDiscount(int distance, int overFare, int age, int expected) {
        //when
        int actual = Fare.of(distance, overFare, age).calculate();

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
