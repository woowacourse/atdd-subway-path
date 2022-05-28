package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.exception.SubwayException;

class FareTest {

    @DisplayName("요금이 음수인 경우 예외가 발생한다.")
    @Test
    void createNegativeFare() {
        assertThatThrownBy(() -> new Fare(-1)).isInstanceOf(SubwayException.class)
                .hasMessage("요금은 음수가 될 수 없습니다.");
    }

    @DisplayName("노선별 추가 요금에 거리별 기본 요금을 더한 값을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"0:1350", "10:1350", "11:1450", "50:2150", "51:2250", "58:2250", "59:2350"},
            delimiter = ':')
    void calculateExtraFareByDistanceAddDefault(int distance, int expected) {
        Fare extraFareByDistance = new Fare(100);

        assertThat(extraFareByDistance.addDefault(distance).getFare()).isEqualTo(expected);
    }

    @DisplayName("노선별 추가 요금에 거리별 기본 요금을 더한 값을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"0:0", "5:1000", "13:1600", "19:2350"}, delimiter = ':')
    void discountFareByAge(int age, int expected) {
        Fare defaultFareByDistance = new Fare(2350);
        AgeDiscountStrategy strategy = AgeDiscountStrategy.from(age);

        assertThat(defaultFareByDistance.discountByAge(strategy).getFare()).isEqualTo(expected);
    }

}
