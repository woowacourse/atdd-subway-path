package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.exception.NegativeFareException;

class FareTest {
    @DisplayName("금액이 음수라면 예외가 발생한다.")
    @ValueSource(ints = {-1, -100})
    @ParameterizedTest
    void constructor_throwsExceptionIfValueIsNegative(int value) {
        // when & then
        assertThatThrownBy(() -> new Fare(value))
                .isInstanceOf(NegativeFareException.class);
    }

    @DisplayName("거리를 전달받아 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"10,1250", "12,1350", "16,1450", "30,1650", "40,1850", "41,1950", "50,2050", "58,2150"})
    void calculate_fare_with_distance(int distanceValue, int expectedFare) {
        // when
        Distance distance = new Distance(distanceValue);
        Fare createdFare = Fare.from(distance);
        int actual = createdFare.getValue();

        // then
        assertThat(actual).isEqualTo(expectedFare);
    }

    @DisplayName("나이에 따라 해당하는 할인율을 운임에 적용한다.")
    @ParameterizedTest(name = "[{index}] 나이: {1}살, 기존 운임: {2}원, 할인된 운임: {3}원")
    @CsvSource(value = {"5,1250,0", "6,1250,450", "12,1250,450", "13,1250,720", "18,1250,720", "19,1250,1250"})
    void discountWithAge(int ageValue, int originalFareValue, int discountedFareValue) {
        // given
        Age age = new Age(ageValue);
        Fare originalFare = new Fare(originalFareValue);

        // when
        Fare actual = originalFare.discountWithAge(age);
        Fare expected = new Fare(discountedFareValue);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
