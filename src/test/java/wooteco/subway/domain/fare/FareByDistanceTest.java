package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FareByDistanceTest {

    @DisplayName("BASIC_FARE distance 일 때, 최종 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    void calculateFreeAgeFare(int distance) {
        int fare = FareByDistance.findFare(distance);

        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("FIRST_EXTRA_FARE distance 일 때, 최종 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({"11,1350", "15,1350", "16,1450", "50,2050"})
    void calculateChildrenAgeFare(int distance, int expectFare) {
        int fare = FareByDistance.findFare(distance);

        assertThat(fare).isEqualTo(expectFare);
    }

    @DisplayName("SECOND_EXTRA_FARE distance 일 때, 최종 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({"51,2150", "58,2150", "59,2250"})
    void calculateTeenagerAgeFare(int distance, int expectFare) {
        int fare = FareByDistance.findFare(distance);

        assertThat(fare).isEqualTo(expectFare);
    }

}