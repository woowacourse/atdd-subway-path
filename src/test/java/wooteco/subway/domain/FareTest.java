package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @Test
    @DisplayName("10km 이하일 때는 기본운임을 반환한다.")
    void calculateDefaultFare() {
        Fare fare = Fare.from(10);

        assertThat(fare.getValue()).isEqualTo(1250);
    }

    @Test
    @DisplayName("10km 초과, 50km 이하일 때는 5km마다 100원을 추가한다.")
    void calculateLongFare() {
        Fare fare = Fare.from(23);

        assertThat(fare.getValue()).isEqualTo(1550);
    }

    @Test
    @DisplayName("50km 초과일 때는 8km마다 100원을 추가한다.")
    void calculateTooLongFare() {
        Fare fare = Fare.from(67);

        assertThat(fare.getValue()).isEqualTo(2350);
    }

    @Test
    @DisplayName("추가 요금이 있는 노선을 이용할 경우 측정된 요금에 추가된다.")
    void calculateFareWithExtraFare() {
        Fare fare = Fare.of(50, 900, 20);

        assertThat(fare.getValue()).isEqualTo(2950);
    }

    @ParameterizedTest
    @DisplayName("6세 이상 13세 미만은 운임에서 350원을 공제한 금액의 50%를 할인한다.")
    @CsvSource(value = {"10, 0, 6, 800", "10, 0, 12, 800", "10, 400, 12, 1000"})
    void calculateFareWithChildren(int distance, int extraFare, int age, int expected) {
        Fare fare = Fare.of(distance, extraFare, age);

        assertThat(fare.getValue()).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("13세 이상 19세 미만은 운임에서 350원을 공제한 금액의 20%를 할인한다.")
    @CsvSource(value = {"10, 0, 13, 1070", "10, 0, 18, 1070", "10, 400, 18, 1390"})
    void calculateFareWithTeenager(int distance, int extraFare, int age, int expected) {
        Fare fare = Fare.of(distance, extraFare, age);

        assertThat(fare.getValue()).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("6세 미만 또는 19세 이상은 나이 할인이 적용되지 않는다.")
    @CsvSource(value = {"10, 0, 5, 1250", "10, 0, 19, 1250", "10, 400, 19, 1650"})
    void calculateFareWithNormalAge(int distance, int extraFare, int age, int expected) {
        Fare fare = Fare.of(distance, extraFare, age);

        assertThat(fare.getValue()).isEqualTo(expected);
    }
}