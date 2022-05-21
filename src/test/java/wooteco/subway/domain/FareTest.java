package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {

    @DisplayName("길이가 주어지면 요금을 계산하여 반환한다.")
    @ParameterizedTest
    @CsvSource({"9,1250", "10,1250", "14,1350", "15,1350", "50,2050", "58,2150", "59,2250"})
    void 요금_계산(int distance, int expected) {
        Fare fare = new Fare(distance);

        assertThat(fare.calculateFare()).isEqualTo(expected);
    }
}
