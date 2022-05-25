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
        // given & when
        final long actual = FarePolicy.calculateByDistance(distance, 0L);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest(name = "거리 : {0}, 노선추가요금 : {1} 기대요금 : {2}")
    @CsvSource(value = {"-1, 0, 0", "0, 0, 0",
            "1, 1000, 2250", "9, 2000, 3250", "10, 3000, 4250",
            "11, 4000, 5350", "15, 5000, 6350", "16, 7000, 8450", "50, 8000, 10050",
            "51, 9000, 11150", "58, 10000, 12150", "59, 11000, 13250", "66, 12000, 14250", "67, 13000, 15350"})
    @DisplayName("노선추가요금이 합산된 거리별 요금 테스트")
    void calculateFareWithExtraFare(long distance, long extraFare, long expected) {
        // given & when
        final long actual = FarePolicy.calculateByDistance(distance, extraFare);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
