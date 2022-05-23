package wooteco.subway.domain.fare;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Section;

class ExtraFareCalculatorTest {

    private ExtraFareCalculator extraFareCalculator;

    private Long 강남 = 1L;
    private Long 선릉 = 2L;
    private Long 잠실 = 3L;
    private Long 사당 = 4L;
    private Long 아현 = 5L;
    private Long 신촌 = 6L;

    private Line 지하철_2호선 = new Line(1L, "지하철2호선", "green", 300);
    private Line 지하철_3호선 = new Line(2L, "지하철3호선", "orange", 400);
    private Line 지하철_4호선 = new Line(3L, "지하철4호선", "blue", 500);

    private Section 강남_선릉_10 = new Section(1L, 1L, 강남, 선릉, 10);
    private Section 선릉_잠실_10 = new Section(2L, 1L, 선릉, 잠실, 10);
    private Section 잠실_사당_10 = new Section(3L, 2L, 잠실, 사당, 10);
    private Section 사당_아현_10 = new Section(4L, 2L, 사당, 아현, 10);
    private Section 아현_신촌_10 = new Section(5L, 3L, 아현, 신촌, 10);

    private List<Section> sections = List.of(강남_선릉_10, 선릉_잠실_10, 잠실_사당_10, 사당_아현_10, 아현_신촌_10);
    private List<Line> lines = List.of(지하철_2호선, 지하철_3호선, 지하철_4호선);

    @Test
    @DisplayName("경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용한다.")
    void getMostExpensiveExtraFare() {
        extraFareCalculator = new ExtraFareCalculator(sections, lines);
        List<Long> path = List.of(강남, 선릉, 잠실, 사당, 아현, 신촌);

        int mostExpensiveExtraFare = extraFareCalculator.getMostExpensiveExtraFare(path);

        assertThat(mostExpensiveExtraFare).isEqualTo(500);
    }
}