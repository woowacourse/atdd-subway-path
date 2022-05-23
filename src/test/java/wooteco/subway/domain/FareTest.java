package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {

    private final int DEFAULT_FARE = 1250;
    private final int ADDITIONAL_FARE = 100;

    @Test
    @DisplayName("거리가 10km 이하 경우 기본요금")
    void calculateFare() {
        assertThat(new Fare(10).getFare()).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("거리가 11km 이상 50km 이하인 경우 5km 마다 추가요금")
    @ParameterizedTest
    @CsvSource(value = {"11,1", "50,8"})
    void calculateFare2(int distance, int additionalFareCount) {
        assertThat(new Fare(distance).getFare()).isEqualTo(DEFAULT_FARE + ADDITIONAL_FARE * additionalFareCount);
    }

    @Test
    @DisplayName("거리가 50km 초과하는 경우 8km 마다 추가요금")
    void calculateFare3() {
        assertThat(new Fare(58).getFare()).isEqualTo(DEFAULT_FARE + ADDITIONAL_FARE * 9);
    }
}
