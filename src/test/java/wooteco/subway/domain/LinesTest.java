package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.domain.fixture.LineFixture.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LinesTest {
    @Test
    @DisplayName("노선들의 추가요금중 최대금액을 구해야 한다.")
    void findMaxExtraFare() {
        Lines lines = new Lines(List.of(LINE1, LINE2, LINE3));
        int actual = lines.findMaxExtraFare();

        assertThat(actual).isEqualTo(500);
    }
}