package wooteco.subway.domain.policy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class AgeDisCountFarePolicyTest {

    @DisplayName("apply 메서드는 할인을 적용한다.")
    @Nested
    class Apply {

        @Test
        void 유아일_경우_무료() {
            AgeDisCountFarePolicy policy = new AgeDisCountFarePolicy(5);

            assertThat(policy.apply(1250)).isZero();
        }

        @Test
        void 어린이일_경우_350을_공제한_후_5할_할인() {
            AgeDisCountFarePolicy policy = new AgeDisCountFarePolicy(6);

            assertThat(policy.apply(1250)).isEqualTo(800);
        }

        @Test
        void 청소년일_경우_350을_공제한_후_2할_할인() {
            AgeDisCountFarePolicy policy = new AgeDisCountFarePolicy(13);

            assertThat(policy.apply(1250)).isEqualTo(1070);
        }
    }
}
