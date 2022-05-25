package wooteco.subway.domain.fare2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.line.LineExtraFare;

@SuppressWarnings("NonAsciiCharacters")
class FareTest {

    private Fare fare;

    @BeforeEach
    void setup() {
        fare = new Fare();
    }

    @DisplayName("applyDistanceOverFarePolicies 메서드는 이동거리에 따라 추가 비용을 더한 값 객체를 반환한다")
    @Nested
    class ApplyDistanceOverFarePoliciesTest {

        @Test
        void 이동거리가_10_이하인_경우_추가비용은_0원() {
            Fare actual = fare.applyDistanceOverFarePolicies(5);
            Fare expected = new Fare(1250);

            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({"10,0", "11,100", "15,100", "16,200", "50,800", "51,900", "58,900", "59,1000"})
        void 경계값_검증(int input, int output) {
            Fare actual = fare.applyDistanceOverFarePolicies(input);
            Fare expected = new Fare(1250 + output);

            assertThat(actual).isEqualTo(expected);
        }
    }

    @DisplayName("applyMaximumLineExtraFare 메서드는 노선별 추가 비용 중 가장 큰 값을 더한 값 객체를 반환한다")
    @Nested
    class ApplyMaximumLineExtraFareTest {

        @Test
        void 노선들의_추가비용이_전부_0원인_경우_그대로_0원_부과() {
            Fare actual = fare.applyMaximumLineExtraFare(List.of(
                    new LineExtraFare(0), new LineExtraFare(0)));
            Fare expected = new Fare(1250);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 복수의_추가비용_중_가장_비싼_노선의_추가비용_하나만_부과() {
            Fare actual = fare.applyMaximumLineExtraFare(List.of(new LineExtraFare(300),
                    new LineExtraFare(500), new LineExtraFare(1200)));
            Fare expected = new Fare(1250 + 1200);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 추가비용_목록이_빈_리스트인_경우_예외발생() {
            assertThatThrownBy(() -> fare.applyMaximumLineExtraFare(List.of()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void applyAgeDiscountPolicy는_나이에_부합하는_할인정책을_적용하여_값_객체_반환() {
        Fare actual = fare.applyAgeDiscountPolicy(10);
        Fare expected = new Fare((int) ((1250 - 350) * 0.5));

        assertThat(actual).isEqualTo(expected);
    }
}
