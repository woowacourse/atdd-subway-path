package wooteco.subway.domain.path.fare2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @DisplayName("현재 요금에 이용거리 초과에 따른 추가운임 부과")
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

    @DisplayName("현재 요금에 노선 추가 비용 부과")
    @Nested
    class LineOverFareTest {

        @Test
        void 제공된_노선정보에_추가비용이_0원인_경우_그대로_0원_부과() {
            Fare fare = new LineOverFare(new BasicFare(), List.of(
                    new LineInfo(1L, "노선1", "색", 0)));

            int actual = fare.calculate();
            int expected = 1250 + 0;

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 복수의_노선정보가_제공된_경우_가장_비싼_노선의_추가비용만_부과() {
            Fare fare = new LineOverFare(new BasicFare(), List.of(
                    new LineInfo(1L, "노선1", "색", 0),
                    new LineInfo(2L, "노선2", "색", 500),
                    new LineInfo(3L, "노선3", "색", 1200)));

            int actual = fare.calculate();
            int expected = 1250 + 1200;

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 노선정보_목록으로_빈_리스트가_제공된_경우_예외발생() {
            Fare fare = new LineOverFare(new BasicFare(), List.of());

            assertThatThrownBy(fare::calculate)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("현재 요금에 노선 추가 비용 부과")
    @Nested
    class AgeDiscountFareTest {

        @DisplayName("6세 이상, 13세 미만의 아동은 현재 금액에서 350원을 빼고 50프로 할인")
        @Test
        void childDiscount() {
            Fare fare = new AgeDiscountFare(new BasicFare(), 10);

            int actual = fare.calculate();
            int expected = (int) ((1250 - 350) * 0.5);

            assertThat(actual).isEqualTo(expected);
        }

        @DisplayName("13세 이상, 19세 미만의 청소년은 현재 금액에서 350원을 빼고 20프로 할인")
        @Test
        void adolescentDiscount() {
            Fare fare = new AgeDiscountFare(new BasicFare(), 15);

            int actual = fare.calculate();
            int expected = (int) ((1250 - 350) * 0.8);

            assertThat(actual).isEqualTo(expected);
        }

        @DisplayName("6세 미만의 유아는 무료")
        @Test
        void babyFree() {
            Fare fare = new AgeDiscountFare(new BasicFare(), 4);

            int actual = fare.calculate();
            int expected = 0;

            assertThat(actual).isEqualTo(expected);
        }

        @DisplayName("65세 이상의 노인도 무료")
        @Test
        void elderlyFree() {
            Fare fare = new AgeDiscountFare(new BasicFare(), 70);

            int actual = fare.calculate();
            int expected = 0;

            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({"5,0", "6,450", "12,450", "13,720", "18,720", "19,1250", "64,1250", "65,0"})
        void 경계값_검증(int age, int expected) {
            Fare fare = new AgeDiscountFare(new BasicFare(), age);

            int actual = fare.calculate();

            assertThat(actual).isEqualTo(expected);
        }
    }
}
