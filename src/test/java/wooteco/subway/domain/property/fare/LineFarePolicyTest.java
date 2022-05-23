package wooteco.subway.domain.property.fare;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.exception.UnexpectedException;

class LineFarePolicyTest {

    @Test
    @DisplayName("가장 비싼 추가 운임을 할증한다.")
    public void surchargeWithExtraFare() {
        // given
        final List<Fare> fares = List.of(new Fare(500), new Fare(200), new Fare(1000));
        LineFarePolicy policy = new LineFarePolicy(fares);
        // when
        final Fare surcharged = policy.apply(new Fare(1250));
        // then
        assertThat(surcharged.getAmount()).isEqualTo(2250);
    }

    @Test
    @DisplayName("추가운임이 없으면 알수 없는 예외가 발생한다.")
    public void throwsExceptionWithEmptyExtraFare() {
        // given
        final List<Fare> fares = List.of();
        // when
        final LineFarePolicy policy = new LineFarePolicy(fares);
        // then
        assertThatExceptionOfType(UnexpectedException.class).isThrownBy(() -> policy.apply(new Fare()));
    }
}