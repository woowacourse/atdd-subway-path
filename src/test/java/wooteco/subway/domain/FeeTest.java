package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.domain.FeeException;

public class FeeTest {

    @Test
    @DisplayName("10km 이내라면 기본 요금 1,250원이다.")
    void defaultFee() {
        Fee fee = Fee.from(8);
        Long calFee = fee.getValue();

        // then
        assertThat(calFee).isEqualTo(1250L);
    }

    @Test
    @DisplayName("10km ~ 50km 사이의 거리는 5km 마다 100원 추가")
    void additionalFee() {
        // given
        Fee fee = Fee.from(12);

        // then
        assertThat(fee.getValue()).isEqualTo(1350L);
    }

    @Test
    @DisplayName("50km를 넘는 거리는 8km 마다 100원 추가")
    void additionalFee_over_fifth() {
        // given
        Fee fee = Fee.from(58);

        // then
        assertThat(fee.getValue()).isEqualTo(2150L);
    }
    
    @Test
    @DisplayName("거리가 0 이하인 경우 예외 발생")
    void fee_underMinimum() {
        // given
        assertThatThrownBy(() -> Fee.from(0))
                .isInstanceOf(FeeException.class);
    }
}
