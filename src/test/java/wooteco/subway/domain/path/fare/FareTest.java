package wooteco.subway.domain.path.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class FareTest {

    @Test
    void 거리가_10_이하인_경우_기본비용_1250원을_그대로_반환() {
        Fare actual = Fare.of(9);
        Fare expected = new Fare(1250);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 거리가_10초과인_경우_5km마다_100원씩_증가() {
        Fare actual = Fare.of(12);
        Fare expected = new Fare(1350);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 거리가_50초과인_경우_8km마다_100원씩_증가() {
        Fare actual = Fare.of(58);
        Fare expected =  new Fare(2150);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"9,1250", "10,1250", "11,1350", "49,2050", "50,2050", "51,2150"})
    void 경계값_유효성_검증(int input, int output) {
        Fare actual = Fare.of(input);
        Fare expected =  new Fare(output);

        assertThat(actual).isEqualTo(expected);
    }
}
