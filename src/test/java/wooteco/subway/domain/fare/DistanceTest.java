package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceTest {

    @DisplayName("거리별 추가 요금 계산")
    @ParameterizedTest(name = "{0} 구간 -> {1}km 이동 시 요금 {2}원")
    @CsvSource(value = {"BASIC,0,0", "BASIC,10,1250", "BASIC,11,1250", "BASIC,50,1250", "BASIC,51,1250",
            "MIDDLE,10,0", "MIDDLE,11,100", "MIDDLE,50,800", "MIDDLE,51,800",
            "FAR,10,0", "FAR,11,0", "FAR,50,0", "FAR,51,100"})
    void calculateCostByDistance(Distance distance, int userDistance, int expected) {
        // given

        // when
        int sum = distance.calculateAdditionalFare(userDistance);

        // then
        assertThat(sum).isEqualTo(expected);
    }
}