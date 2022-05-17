package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FareTest {

    @DisplayName("경로의 요금을 구한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 6, 10})
    void getFare(final int distance) {
        final Fare fare = Fare.from(distance);
        assertThat(fare.getPrice()).isEqualTo(1250);
    }

    @DisplayName("이용 거리 10km 초과 50km 이하시 추가운임을 부과한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "11, 1350",
            "16, 1450",
            "21, 1550",
            "31, 1750",
            "41, 1950",
            "50, 2050"
    })
    void calculateOverFare_below50km(final int distance, final int price) {
        final Fare fare = Fare.from(distance);
        assertThat(fare.getPrice()).isEqualTo(price);
    }

    @DisplayName("이용 거리 50km 초과시 추가운임을 부과한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "51, 2150",
            "75, 2450",
            "100, 2750"
    })
    void calculateOverFare_over50km(final int distance, final int price) {
        final Fare fare = Fare.from(distance);
        assertThat(fare.getPrice()).isEqualTo(price);
    }
}
