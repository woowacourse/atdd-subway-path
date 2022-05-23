package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.DomainException;

class LineTest {

    private Line line;

    @BeforeEach
    void setUp() {
        line = Line.withoutIdOf("1호선", "red", 0L);
    }

    @ParameterizedTest
    @DisplayName("노선 이름이 공백이면 예외가 발생한다")
    @ValueSource(strings = {"", " ", "    "})
    void newLine_blankName(String name) {
        assertThatThrownBy(() -> Line.withoutIdOf(name, "bg-red-600", 0L))
                .isInstanceOf(DomainException.class)
                .hasMessage("노선의 이름이 공백이 되어서는 안됩니다.");
    }

    @Test
    @DisplayName("노선 객체 생성에 성공한다.")
    void newLine() {
        // when
        Line line = Line.withoutIdOf("7호선", "bg-red-600", 0L);

        // then
        assertThat(line).isNotNull();
    }
}
