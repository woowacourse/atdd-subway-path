package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.exception.PositiveDigitException;

class FareByDistanceTest {

    @DisplayName("기본 요금이 청구되는 distance 일 때, 1250원을 부과한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    void calculateFreeAgeFare(int distance) {
        int fare = FareByDistance.findFare(distance);

        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("distance 가 11이상 50이하 일 때, 10km 초과인 distance 는 5km당 100원씩 부과한다.")
    @ParameterizedTest
    @CsvSource({"11,1350", "15,1350", "16,1450", "50,2050"})
    void calculateChildrenAgeFare(int distance, int expectFare) {
        int fare = FareByDistance.findFare(distance);

        assertThat(fare).isEqualTo(expectFare);
    }

    @DisplayName("distance 가 51이상 일 때, 10km 초과인 distance 는 5km당 100원씩, 50km 초과인 distance 는 8km당 100원씩 부과한다.")
    @ParameterizedTest
    @CsvSource({"51,2150", "58,2150", "59,2250"})
    void calculateTeenagerAgeFare(int distance, int expectFare) {
        int fare = FareByDistance.findFare(distance);

        assertThat(fare).isEqualTo(expectFare);
    }

    @DisplayName("distance 가 양수가 아닐 경우 예외를 발생시킨다.")
    @Test
    void calculateDistanceFareExceptionNotPositiveDistance() {
        assertThatThrownBy(() -> FareByDistance.findFare(0))
                .isInstanceOf(PositiveDigitException.class)
                .hasMessage("구간의 길이는 양수여야 합니다.");
    }

}
