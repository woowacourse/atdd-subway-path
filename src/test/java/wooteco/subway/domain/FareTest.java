package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @ParameterizedTest
    @CsvSource(value = {"9, 1250", "10, 1250", "11, 1350", "15, 1350", "50, 2050", "55, 2150",
        "58, 2150", "59, 2250"})
    void calculateFare(int distance, int expectedFare) {
        Fare fare = Fare.from(distance, 0);

        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"8, 900, 2150", "12, 900, 2250"})
    @DisplayName("추가 요금이 있는 노선을 이용 할 경우, 거리를 기준으로 책정된 요금에 추가된다.")
    void extraFareTest(int distance, int extraFare, int expectedFare) {
        Fare fare = Fare.from(distance, extraFare);

        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }
}
