package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LinesTest {

    @DisplayName("노선들 중 제일 비싼 요금을 가진 노선 중 하나를 반환하는 지 확인한다.")
    @Test
    void getMaxExtraFare() {
        final Line lineExtraFare100 = new Line("1호선", "red", 100);
        final Line lineExtraFare200 = new Line("2호선", "blue", 200);
        final Line lineExtraFare300 = new Line("3호선", "orange", 200);
        final Lines lines = new Lines(List.of(lineExtraFare100, lineExtraFare200, lineExtraFare300));
        final Line line = lines.getMaxExtraFare();

        assertThat(line.getExtraFare()).isEqualTo(200);
    }
}
