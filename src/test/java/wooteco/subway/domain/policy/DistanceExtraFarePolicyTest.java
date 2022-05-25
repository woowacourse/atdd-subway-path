package wooteco.subway.domain.policy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class DistanceExtraFarePolicyTest {

    @ParameterizedTest(name = "거리가 {0}일 때, 요금이 {1}원이다.")
    @CsvSource(value = {"10,1250", "11,1350", "50,2050", "51, 2150", "90,2550"})
    void 거리에_따라_요금정책을_적용한다(int distance, int fare) {
        DistanceExtraFarePolicy policy = new DistanceExtraFarePolicy(distance);

        assertThat(policy.apply(1250)).isEqualTo(fare);
    }
}
