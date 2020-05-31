package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {
    @DisplayName("역 업데이트")
    @Test
    void update() {
        Line line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5,
            "bg-green-600");
        Line updatedLine = new Line("3호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5,
            "bg-green-600");
        line.update(updatedLine);

        assertThat(line.getName()).isEqualTo("3호선");
    }
}
