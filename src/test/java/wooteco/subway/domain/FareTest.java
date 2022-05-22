package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {

    @DisplayName("길이와 나이 및 추가 요금이 주어지면 요금을 계산하여 반환한다.")
    @ParameterizedTest
    @CsvSource({"9,24,0,1250", "10,24,0,1250", "14,24,0,1350",
            "15,24,0,1350", "50,24,0,2050", "58,24,0,2150", "59,24,0,2250",
            "8,24,900,2150", "12,24,900,2250"})
    void 요금_계산(int distance, int age, int extraFare, int expected) {
        Fare fare = new Fare(distance, age, extraFare);

        assertThat(fare.calculateFare()).isEqualTo(expected);
    }

    @DisplayName("길이와 청소년 나이 및 추가 요금이 주어지면 요금을 계산하여 반환한다.")
    @ParameterizedTest
    @CsvSource({"9,13,0,720", "10,18,0,720", "14,13,0,800",
            "15,18,0,800", "50,13,0,1360", "58,13,0,1440", "59,13,0,1520",
            "8,13,900,1440", "12,13,900,1520"})
    void 청소년_요금_계산(int distance, int age, int extraFare, int expected) {
        Fare fare = new Fare(distance, age, extraFare);

        assertThat(fare.calculateFare()).isEqualTo(expected);
    }

    @DisplayName("길이와 어린이 나이 및 추가 요금이 주어지면 요금을 계산하여 반환한다.")
    @ParameterizedTest
    @CsvSource({"9,6,0,450", "10,12,0,450", "14,6,0,500",
            "15,12,0,500", "50,6,0,850", "58,6,0,900", "59,6,0,950",
            "8,6,900,900", "12,6,900,950"})
    void 어린이_요금_계산(int distance, int age, int extraFare, int expected) {
        Fare fare = new Fare(distance, age, extraFare);

        assertThat(fare.calculateFare()).isEqualTo(expected);
    }
}
