package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareTest {

    private final int DEFAULT_FARE = 1250;
    private final int ADDITIONAL_FARE = 100;

    @Test
    @DisplayName("거리가 10km 이하 경우 기본요금")
    void calculateFare() {
        assertAll(
                () -> assertThat(new Fare(10).getFare()).isEqualTo(DEFAULT_FARE),
                () -> assertThat(new Fare(11).getFare()).isNotEqualTo(DEFAULT_FARE)
        );
    }

    @Test
    @DisplayName("거리가 11km 이상 50km 이하인 경우 5km 마다 추가요금")
    void calculateFare2() {
        assertAll(
                () -> assertThat(new Fare(11).getFare()).isEqualTo(DEFAULT_FARE + ADDITIONAL_FARE * 1),
                () -> assertThat(new Fare(15).getFare()).isEqualTo(DEFAULT_FARE + ADDITIONAL_FARE * 1),
                () -> assertThat(new Fare(46).getFare()).isEqualTo(DEFAULT_FARE + ADDITIONAL_FARE * 8),
                () -> assertThat(new Fare(50).getFare()).isEqualTo(DEFAULT_FARE + ADDITIONAL_FARE * 8)
        );
    }

    @Test
    @DisplayName("거리가 50km 초과하는 경우 8km 마다 추가요금")
    void calculateFare3() {
        assertAll(
                () -> assertThat(new Fare(51).getFare()).isEqualTo(DEFAULT_FARE + ADDITIONAL_FARE * 9),
                () -> assertThat(new Fare(58).getFare()).isEqualTo(DEFAULT_FARE + ADDITIONAL_FARE * 9),
                () -> assertThat(new Fare(59).getFare()).isEqualTo(DEFAULT_FARE + ADDITIONAL_FARE * 10)
        );
    }
}
