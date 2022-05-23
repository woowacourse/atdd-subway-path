package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceExtraFareTest {

    @DisplayName("10과 같거나 작으면 추가 요금이 없다.")
    @ParameterizedTest
    @ValueSource(ints = {9, 10})
    void valueOfUnder10(int distance) {
        assertThat(DistanceExtraFare.valueOf(distance)).isEqualTo(0);
    }

    @DisplayName("10km 초과 50km 이하이면 5km마다 100원이 추가된다.")
    @ParameterizedTest
    @CsvSource({"12, 100", "16, 200"})
    void valueOfOver10Under50(int distance, int expected) {
        assertThat(DistanceExtraFare.valueOf(distance)).isEqualTo(expected);
    }

    @DisplayName("50km 초과이면 8km마다 100원이 추가된다.")
    @ParameterizedTest
    @ValueSource(ints = {58, 57})
    void valueOfOver50(int distance) {
        assertThat(DistanceExtraFare.valueOf(distance)).isEqualTo(900);
    }

}
