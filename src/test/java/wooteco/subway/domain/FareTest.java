package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {
    
    @ParameterizedTest
    @CsvSource(value = {"10:1250", "11:1350", "15:1350", "50:2050", "51:2150", "58:2150", "59:2250"}, delimiter = ':')
    @DisplayName("거리에 따른 요금을 계산한다.")
    void getFare(int distance, int expectFare) {
        Fare fare = new Fare();
        assertThat(fare.calculateFare(distance)).isEqualTo(expectFare);
    }
}
