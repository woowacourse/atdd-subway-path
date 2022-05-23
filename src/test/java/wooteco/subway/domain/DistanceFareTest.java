package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DistanceFareTest {

    @DisplayName("라인의 기본 요금이 있고 거리가 10과 같거나 작으면 1250원에 라인의 기본 요금이 더해진다.")
    @ParameterizedTest
    @ValueSource(ints = {9, 10})
    void valueOfUnder10(int distance) {
        int fare = DistanceFare.valueOf(500, distance);
        assertThat(fare).isEqualTo(1750);
    }

    @DisplayName("라인의 기본 요금이 있고 거리가 10km 초가 50km 이하이면 5km마다 100원이 추가되고 라인의 기본 요금이 더해진다.")
    @ParameterizedTest
    @CsvSource({"12, 1850", "16, 1950"})
    void valueOfOver10Under50(int distance, int expected) {
        int fare = DistanceFare.valueOf(500, distance);
        assertThat(fare).isEqualTo(expected);
    }

    @DisplayName("라인의 기본 요금이 있고 거리가 50km 초과이면 8km마다 100원이 추가되고 라인의 기본 요금이 더해진다.")
    @ParameterizedTest
    @ValueSource(ints = {58, 57})
    void valueOfOver50(int distance) {
        int fare = DistanceFare.valueOf(500, distance);
        assertThat(fare).isEqualTo(2650);
    }

}
