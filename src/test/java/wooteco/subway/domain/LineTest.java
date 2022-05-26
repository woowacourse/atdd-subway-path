package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.Fixtures.LINE_4;
import static wooteco.subway.Fixtures.SKY_BLUE;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LineTest {

    @Test
    @DisplayName("객체를 생성한다.")
    void create() {
        final Line line = new Line(LINE_4, SKY_BLUE, 900);

        assertAll(
                () -> assertThat(line.getName()).isEqualTo(LINE_4),
                () -> assertThat(line.getColor()).isEqualTo(SKY_BLUE),
                () -> assertThat(line.getExtraFare()).isEqualTo(900)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("노선 이름이 공백인 경우, 예외를 발생한다.")
    void createEmptyName(final String name) {
        assertThatThrownBy(() -> new Line(name, SKY_BLUE, 900))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 이름은 공백일 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("색상이 공백인 경우, 예외를 발생한다.")
    void createEmptyColor(final String color) {
        assertThatThrownBy(() -> new Line(LINE_4, color, 900))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("색상이 공백일 수 없습니다.");
    }

    @Test
    @DisplayName("추가 요금이 0 미만인 경우, 예외를 발생한다.")
    void createNegativeExtraFare() {
        assertThatThrownBy(() -> new Line(LINE_4, SKY_BLUE, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("추가 요금은 0 이상의 정수여야합니다.");
    }
}
