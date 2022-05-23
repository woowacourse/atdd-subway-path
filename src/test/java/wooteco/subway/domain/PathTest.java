package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PathTest {

    @ParameterizedTest
    @CsvSource(value = {
            "9, 300, 5, 0",
            "15, 300, 12, 825",
            "16, 200, 15, 1320",
            "50, 300, 18, 1880",
            "59, 300, 19, 2550"
    })
    @DisplayName("지하철 요금 계산")
    void calcFare(int distance, int extraFare, Long age, int fare) {
        assertThat(new Fare(distance, extraFare, age).calculateFare()).isEqualTo(fare);
    }
}
