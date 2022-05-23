package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LineTest {

    @Test
    @DisplayName("객체를 생성한다.")
    void create() {
        final String name = "신분당선";
        final String color = "bg-red-600";
        final int extraFare = 500;

        final Line line = new Line(name, color, extraFare);

        assertAll(() -> {
            assertThat(line.getName()).isEqualTo(name);
            assertThat(line.getColor()).isEqualTo(color);
        });
    }
}
