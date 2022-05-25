package wooteco.subway.domain.policy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class LineExtraFarePolicyTest {

    @DisplayName("apply메서드는 노선별 추가요금 정책을 적용한다.")
    @Nested
    class Apply {

        @Test
        void 추가요금이_있는_노선을_환승할_경우_가장_높은_금액을_적용() {
            List<Integer> extraFares = List.of(500, 600, 1200);
            LineExtraFarePolicy policy = new LineExtraFarePolicy(extraFares);

            assertThat(policy.apply(1250)).isEqualTo(2450);
        }

        @Test
        void 추가요금이_없는_경우_예외발생() {
            List<Integer> extraFares = List.of();
            LineExtraFarePolicy policy = new LineExtraFarePolicy(extraFares);

            assertThatThrownBy(() -> policy.apply(1250))
                .isInstanceOf(NoSuchElementException.class);
        }
    }
}
