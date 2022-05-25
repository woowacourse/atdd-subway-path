package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.line.LineExtraFare;

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
        ExtraFare lineExtraFare = new ExtraFare(distanceOverFare, List.of(new LineExtraFare(100)));
        AgeDiscountFare adolescentDiscountFare = new AgeDiscountFare(lineExtraFare, 15);

        int actual = adolescentDiscountFare.calculate();
        int expected = (int) ((((1250 + 100) + 100) - 350) * 0.8);

        assertThat(actual).isEqualTo(expected);
    }
}
