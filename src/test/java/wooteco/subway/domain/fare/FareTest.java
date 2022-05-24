package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.line.LineInfo;

@SuppressWarnings("NonAsciiCharacters")
class FareTest {

    @Test
    void 기초요금은_1250원() {
        Fare fare = new BasicFare();

        int actual = fare.calculate();
        int expected = 1250;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 추가_요금_및_할인_정책의_순차적_적용() {
        BasicFare basicFare = new BasicFare();
        DistanceOverFare distanceOverFare = new DistanceOverFare(basicFare, 12);
        LineOverFare lineOverFare = new LineOverFare(distanceOverFare, List.of(
                new LineInfo(1L, "노선1", "색", 100)));
        AgeDiscountFare adolescentDiscountFare = new AgeDiscountFare(lineOverFare, 15);

        int actual = adolescentDiscountFare.calculate();
        int expected = (int) ((((1250 + 100) + 100) - 350) * 0.8);

        assertThat(actual).isEqualTo(expected);
    }
}
