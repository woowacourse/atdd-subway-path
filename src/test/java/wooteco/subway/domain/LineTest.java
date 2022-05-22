package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LineTest {

    @DisplayName("추가 요금이 더 비싼 노선을 반환하는 지 확인한다.")
    @Test
    void comparesMoreExpensiveExtraFare() {
        final Line lineExtraFare100 = new Line("1호선", "red", 100);
        final Line lineExtraFare200 = new Line("2호선", "red", 200);

        final Line resultLine = lineExtraFare100.comparesMoreExpensiveExtraFare(lineExtraFare200);
        final String result = resultLine.getName();

        assertThat(result).isEqualTo("2호선");
    }
}
