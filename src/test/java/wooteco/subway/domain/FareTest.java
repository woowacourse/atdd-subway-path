package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @ParameterizedTest
    @CsvSource(value = {"9,1250", "10,1350", "12,1350", "15,1350", "16,1450", "50,2050", "51,2150", "58,2150", "59,2250", "66,2250"})
    @DisplayName("지하철 요금 계산")
    void calcFare(int distance, int fare) {

        assertThat(new Fare(distance).getFare()).isEqualTo(fare);
    }
}
