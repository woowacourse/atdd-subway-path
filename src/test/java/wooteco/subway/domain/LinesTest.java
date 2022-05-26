package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.Collections;
import java.util.List;

class LinesTest {

    @DisplayName("노선들을 반환한다.")
    @Test
    void getValues() {
        final Line line1 = new Line(1L, "2호선", "bg-green-600", 0);
        final Line line2 = new Line(2L, "3호선", "bg-orange-600", 200);
        final Line line3 = new Line(3L, "4호선", "bg-skyblue-600", 300);
        final Line line4 = new Line(4L, "분당선", "bg-yellow-600", 500);
        final Line line5 = new Line(5L, "신분당선", "bg-red-600", 1000);
        final Lines lines = new Lines(List.of(line1, line2, line3, line4, line5));

        assertThat(lines.getValues()).isEqualTo(List.of(line1, line2, line3, line4, line5));
    }

    @DisplayName("노선들 중 가장 높은 금액의 추가요금을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"200,300,500,800,100", "1000,300,200,500,400", "3000,2500,100,400,2000"})
    void getHighestOverFare(final int extraFare1,
                            final int extraFare2,
                            final int extraFare3,
                            final int extraFare4,
                            final int extraFare5) {
        final Line line1 = new Line(1L, "2호선", "bg-green-600", extraFare1);
        final Line line2 = new Line(2L, "3호선", "bg-orange-600", extraFare2);
        final Line line3 = new Line(3L, "4호선", "bg-skyblue-600", extraFare3);
        final Line line4 = new Line(4L, "분당선", "bg-yellow-600", extraFare4);
        final Line line5 = new Line(5L, "신분당선", "bg-red-600", extraFare5);
        final Lines lines = new Lines(List.of(line1, line2, line3, line4, line5));

        final Integer expected = Collections.max(List.of(extraFare1, extraFare2, extraFare3, extraFare4, extraFare5));

        assertThat(lines.getHighestOverFare()).isEqualTo(expected);
    }
}
