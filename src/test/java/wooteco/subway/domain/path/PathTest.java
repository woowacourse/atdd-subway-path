package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.TestFixture.강남_역삼;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import wooteco.subway.domain.line.Fare;
import wooteco.subway.domain.line.Line;

class PathTest {

    @DisplayName("거리에 따른 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"10,1250", "11,1350", "15,1350", "16,1450", "50,2050", "51,2150", "58,2150", "59,2250"})
    void calculateFareByDistance(long distance, long expected) {
        Path path = new Path(Collections.emptyList(), Collections.emptyList(), distance);

        Fare actual = path.calculateFare();
        assertThat(actual.getFare()).isEqualTo(expected);
    }

    @DisplayName("거쳐온 노선 중 가장 높은 금액의 추가 요금을 구한다.")
    @Test
    void calculateMaximumExtraFareByLines() {
        Path path = new Path(Collections.emptyList(), List.of(
                new Line(List.of(강남_역삼), "2호선", "green", 10000),
                new Line(List.of(강남_역삼), "2호선", "green", 100)
        ), 10);

        Fare actual = path.calculateFare();
        assertThat(actual.getFare()).isEqualTo(10000 + 1250);
    }
}
