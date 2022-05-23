package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FareByAgeTest {

    @DisplayName("FREE_AGE age 의 최종 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {5, 65})
    void calculateFreeAgeFare(int age) {
        int fare = FareByAge.findFare(age, 1250);

        assertThat(fare).isEqualTo(0);
    }

    @DisplayName("CHILDREN age 의 최종 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void calculateChildrenAgeFare(int age) {
        int fare = FareByAge.findFare(age, 1250);

        assertThat(fare).isEqualTo(450);
    }

    @DisplayName("TEENAGER age 의 최종 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void calculateTeenagerAgeFare(int age) {
        int fare = FareByAge.findFare(age, 1250);

        assertThat(fare).isEqualTo(720);
    }
}