package wooteco.subway.domain.path.fare2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class FareTest {

    @Test
    void 기초요금은_1250원() {
        Fare fare = new BasicFare();

        int actual = fare.calculate();
        int expected = 1250;

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("기초요금 부과 후 이용거리 초과에 따른 추가운임 부과")
    @Nested
    class DistanceOverFareTest {

        @Test
        void 거리가_10_이하인_경우_추가비용은_0원() {
            Fare fare = new DistanceOverFare(new BasicFare(), 5);

            int actual = fare.calculate();
            int expected = 1250 + 0;

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 거리가_10_초과인_경우_5km_단위마다_최대_100원씩_추가비용() {
            Fare fare = new DistanceOverFare(new BasicFare(), 18);

            int actual = fare.calculate();
            int expected = 1250 + 200;

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 거리가_50_초과인_경우_50을_초과한_거리에_대해_8km_단위마다_최대_100원씩_추가비용() {
            Fare fare = new DistanceOverFare(new BasicFare(), 60);

            int actual = fare.calculate();
            int expected = 1250 + 1000;

            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({"10,0", "11,100", "15,100", "16,200", "50,800", "51,900", "58,900", "59,1000"})
        void 경계값_검증(int input, int output) {
            Fare fare = new DistanceOverFare(new BasicFare(), input);

            int actual = fare.calculate();
            int expected = 1250 + output;

            assertThat(actual).isEqualTo(expected);
        }
    }
}
