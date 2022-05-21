package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    @ParameterizedTest
    @CsvSource(value = {"9,1250", "10,1350", "12,1350", "15,1350", "16,1450", "50,2050", "51,2150", "58,2150", "59,2250", "66,2250"})
    @DisplayName("지하철 요금 계산 - 지하철 노선 추가 금액 X")
    void calcNoExtraFare(int distance, int fare) {
        assertThat(new Path(List.of(new SectionWeightEdge(1L, 1L, 2L, distance)), distance, 20L)
                .calculateFare(List.of(new Line(1L, "2호선", "green", 0)))).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource(value = {"9,3250,1000,2000", "10,1450,100,0", "12,1650,200,300", "15,4350,3000,1000", "50,2060,10,0"})
    @DisplayName("지하철 요금 계산 - 지하철 노선 추가 금액 O")
    void calcExtraFare(int distance, int fare, int firstExtra, int secondExtra) {
        assertThat(new Path(List.of(new SectionWeightEdge(1L, 1L, 2L, distance),
                new SectionWeightEdge(2L, 2L, 3L, distance)), distance, 20L)
                .calculateFare(List.of(new Line(1L, "2호선", "green", firstExtra),
                                new Line(2L, "3호선", "green", secondExtra)))).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource(value = {"10,500,6", "10,800,13", "9,450,12", "12,800,13", "16,880,18", "50,2050,19", "59,0,0"})
    @DisplayName("지하철 요금 계산 - 지하철 연령병 요금 할인")
    void calcAgeFare(int distance, int fare, Long age) {
        assertThat(new Path(List.of(new SectionWeightEdge(1L, 1L, 2L, distance)), distance, age)
                .calculateFare(List.of(new Line(1L, "2호선", "green", 0)))).isEqualTo(fare);
    }
}
