package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FarePolicyTest {

    @DisplayName("거리가 10km 미만이면 기본 요금으로 계산한다")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 9})
    void calculateFare1(int distance) {
        assertThat(FarePolicy.calculateFare(distance, 0)).isEqualTo(FarePolicy.BASE_FARE);
    }

    @DisplayName("거리가 10km ~ 50km 사이인 경우 추가로 5km마다 100원을 추가한다")
    @ParameterizedTest
    @CsvSource(value = {"10,1350", "15,1350", "16,1450", "45,1950", "46,2050", "50,2050"})
    void calculateFare2(int distance, int expectedFare) {
        assertThat(FarePolicy.calculateFare(distance, 0)).isEqualTo(expectedFare);
    }

    @DisplayName("거리가 50km를 초과하는 경우 추가로 8km마다 100원을 추가한다")
    @ParameterizedTest
    @CsvSource(value = {"51,2150", "58,2150", "59,2250"})
    void calculateFare3(int distance, int expectedFare) {
        assertThat(FarePolicy.calculateFare(distance, 0)).isEqualTo(expectedFare);
    }

    @DisplayName("추가 요금을 최종 요금에 추가한다")
    @Test
    void addExtraFare() {
        int extraFare = 100;
        assertThat(FarePolicy.calculateFare(9, extraFare)).isEqualTo(FarePolicy.BASE_FARE + extraFare);
    }
}
