package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FarePolicyTest {

    @ParameterizedTest(name = "거리가 {0}이고 추가금액이 {1}일 때, 요금이 {2}이다.")
    @CsvSource({"5, 0, 1250", "10, 1000, 2250", "30, 500, 2150", "50, 0, 2050", "90, 1000, 3550"})
    void calculateFare(int distance, int extraFare, int expected) {
        int actual = FarePolicy.calculateFare(distance, extraFare);
        assertThat(actual).isEqualTo(expected);
    }
}
