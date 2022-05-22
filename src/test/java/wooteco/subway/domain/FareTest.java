package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.domain.strategy.BasicFareStrategy;

class FareTest {

    private final Fare fare = new Fare(new BasicFareStrategy());

    @DisplayName("1Km 이상 10Km 이하이면 1250원 기본 요금이다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 7, 8, 9})
    void baseUnderDistanceFare(int distance) {
        assertThat(fare.calculateFare(distance)).isEqualTo(1250);
    }

    @DisplayName("10Km 초과 50Km 이하이면 1250원 기본 요금 + 5km 초과당 100원씩 추가요금")
    @ParameterizedTest
    @CsvSource({"10,1250", "15,1350", "16,1450",
            "25,1550", "50,2050"})
    void baseOverFirstRoleUnderDistanceFare(int distance, int actualFare) {
        assertThat(fare.calculateFare(distance)).isEqualTo(actualFare);
    }

    @DisplayName("50Km 초과이면 50kM 이후로 8km 초과당 100원씩 추가요금")
    @ParameterizedTest
    @CsvSource({"51,2150", "56,2150", "59,2250"})
    void FirstRoleRoleOverDistanceFare(int distance, int actualFare) {
        assertThat(fare.calculateFare(distance)).isEqualTo(actualFare);
    }
}
