package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.policy.distance.OverFiftyKMPolicy;
import wooteco.subway.domain.fare.policy.distance.TenToFiftyKMPolicy;
import wooteco.subway.domain.fare.policy.distance.UnderTenKMPolicy;

class DistancePolicyGeneratorTest {

    @Test
    @DisplayName("10카로 이하일 때 거리정책을 확인한다.")
    void of_under_ten() {
        assertThat(DistancePolicyGenerator.of(0))
                .isInstanceOf(UnderTenKMPolicy.class);
        assertThat(DistancePolicyGenerator.of(9))
                .isInstanceOf(UnderTenKMPolicy.class);
    }

    @Test
    @DisplayName("10카로 이상 50키로 미만일 때 거리정책을 확인한다.")
    void of_ten_to_fifty() {
        assertThat(DistancePolicyGenerator.of(10))
                .isInstanceOf(TenToFiftyKMPolicy.class);
        assertThat(DistancePolicyGenerator.of(49))
                .isInstanceOf(TenToFiftyKMPolicy.class);
    }

    @Test
    @DisplayName("50키로 이상일 때 거리정책을 확인한다.")
    void of_over_fifty() {
        assertThat(DistancePolicyGenerator.of(50))
                .isInstanceOf(OverFiftyKMPolicy.class);
    }

    @Test
    @DisplayName("거리가 음수일 때 오류가 난다.")
    void of_error() {
        assertThatThrownBy(() -> DistancePolicyGenerator.of(-1))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당되는 거리 정책이 없습니다.");
    }
}
