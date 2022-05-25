package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ExtraFareTest {

    @Test
    void 추가비용이_0원인_경우_그대로_0원_부과() {
        Fare fare = new ExtraFare(new BasicFare(), List.of(0));

        int actual = fare.calculate();
        int expected = 1250 + 0;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 복수의_노선정보가_제공된_경우_가장_비싼_노선의_추가비용만_부과() {
        Fare fare = new ExtraFare(new BasicFare(), List.of(0, 500, 1200));

        int actual = fare.calculate();
        int expected = 1250 + 1200;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 노선정보_목록으로_빈_리스트가_제공된_경우_예외발생() {
        Fare fare = new ExtraFare(new BasicFare(), List.of());

        assertThatThrownBy(fare::calculate)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
