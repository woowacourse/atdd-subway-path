package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.fare.FarePolicy;

class FarePolicyTest {

    @ParameterizedTest(name = "거리 : {0} 기대요금 : {1}")
    @CsvSource(value = {"-1, 0", "0, 0",
            "1, 1250", "9, 1250", "10,1250",
            "11,1350", "15,1350", "16,1450", "50,2050",
            "51,2150", "58,2150", "59,2250", "66, 2250", "67, 2350"})
    @DisplayName("거리별 요금 테스트")
    void calculateFare(long distance, long expected) {
        // given
        final long givenDistance = distance;
        final long noExtraFare = 0L;

        // when
        final long actual = FarePolicy.calculateByDistance(givenDistance, noExtraFare);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
