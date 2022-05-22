package wooteco.subway.domain.policy;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.domain.fixtures.TestFixtures.분당선;
import static wooteco.subway.domain.fixtures.TestFixtures.이호선;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.policy.line.LineExtraFeePolicy;

class LineExtraFeePolicyTest {

    @Test
    @DisplayName("추가요금이 있는 노선을 탈 경우 요금이 추가된다.")
    void calculate_oneLine() {
        LineExtraFeePolicy lineExtraFeePolicy = new LineExtraFeePolicy(List.of(이호선));
        assertThat(lineExtraFeePolicy.calculate(1250))
                .isEqualTo(1350);
    }

    @Test
    @DisplayName("추가요금이 있는 노선을 탈 경우 요금이 추가된다.")
    void calculate_twoLine() {
        LineExtraFeePolicy lineExtraFeePolicy = new LineExtraFeePolicy(List.of(이호선, 분당선));
        assertThat(lineExtraFeePolicy.calculate(1250))
                .isEqualTo(1450);
    }
}
