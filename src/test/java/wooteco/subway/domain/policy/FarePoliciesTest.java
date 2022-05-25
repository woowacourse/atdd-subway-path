package wooteco.subway.domain.policy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class FarePoliciesTest {

    @DisplayName("calculate 메서드는 쟁책 순서에 따라 적용하며 요금을 계산한다.")
    @Nested
    class Calculate {

        @Test
        void 나이_거리_정책_순서로_적용하여_요금을_계산() {
            FarePolicies policies = new FarePolicies(List.of(
                new AgeDisCountFarePolicy(15),
                new DistanceExtraFarePolicy(50)
            ));

            assertThat(policies.calculate()).isEqualTo(1870);
        }

        @Test
        void 거리_노선_추가요금_정책_순서로_적용하여_요금을_계산() {
            FarePolicies policies = new FarePolicies(List.of(
                new DistanceExtraFarePolicy(50),
                new LineExtraFarePolicy(List.of(500, 1000))
            ));

            assertThat(policies.calculate()).isEqualTo(3050);
        }

        @Test
        void 노선_추가요금_나이_정책_순서로_적용하여_요금을_계산() {
            FarePolicies policies = new FarePolicies(List.of(
                new LineExtraFarePolicy(List.of(500, 1000)),
                new AgeDisCountFarePolicy(15)
            ));

            assertThat(policies.calculate()).isEqualTo(1870);
        }

        @Test
        void 거리_노선_추가요금_나이_정책을_일괄_적용하여_요금을_계산() {
            FarePolicies policies = new FarePolicies(List.of(
                new DistanceExtraFarePolicy(50),
                new LineExtraFarePolicy(List.of(500, 1000)),
                new AgeDisCountFarePolicy(15)
            ));

            assertThat(policies.calculate()).isEqualTo(2510);
        }

        @Test
        void 정책을_일괄_적용할때_노선_추가요금이_없는_경우_예외_발생() {
            FarePolicies policies = new FarePolicies(List.of(
                new DistanceExtraFarePolicy(50),
                new LineExtraFarePolicy(List.of()),
                new AgeDisCountFarePolicy(15)
            ));

            assertThatThrownBy(policies::calculate)
                .isInstanceOf(NoSuchElementException.class);
        }
    }
}
