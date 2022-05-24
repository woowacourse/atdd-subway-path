package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.line.Line;

@SuppressWarnings("NonAsciiCharacters")
class LineOverFareTest {

    @Test
    void 제공된_노선정보에_추가비용이_0원인_경우_그대로_0원_부과() {
        Fare fare = new LineOverFare(new BasicFare(), List.of(
                new Line(1L, "노선1", "색", 0)));

        int actual = fare.calculate();
        int expected = 1250 + 0;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 복수의_노선정보가_제공된_경우_가장_비싼_노선의_추가비용만_부과() {
        Fare fare = new LineOverFare(new BasicFare(), List.of(
                new Line(1L, "노선1", "색", 0),
                new Line(2L, "노선2", "색", 500),
                new Line(3L, "노선3", "색", 1200)));

        int actual = fare.calculate();
        int expected = 1250 + 1200;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 노선정보_목록으로_빈_리스트가_제공된_경우_예외발생() {
        Fare fare = new LineOverFare(new BasicFare(), List.of());

        assertThatThrownBy(fare::calculate)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
