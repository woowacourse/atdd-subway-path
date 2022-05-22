package wooteco.subway.domain.policy;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.domain.fixtures.TestFixtures.분당선;
import static wooteco.subway.domain.fixtures.TestFixtures.이호선;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LinePolicyTest {

    @Test
    @DisplayName("추가요금이 있는 노선을 탈 경우 요금이 추가된다.")
    void calculate_oneLine() {
        LinePolicy linePolicy = new LinePolicy(List.of(이호선));
        assertThat(linePolicy.calculate(1250))
                .isEqualTo(1350);
    }

    @Test
    @DisplayName("추가요금이 있는 노선을 탈 경우 요금이 추가된다.")
    void calculate_twoLine() {
        LinePolicy linePolicy = new LinePolicy(List.of(이호선, 분당선));
        assertThat(linePolicy.calculate(1250))
                .isEqualTo(1450);
    }
}
