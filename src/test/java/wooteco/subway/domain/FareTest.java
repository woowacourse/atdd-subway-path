package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.Age;

class FareTest {

    @ParameterizedTest
    @CsvSource(value = {"10,1250", "11,1350", "15,1350", "50,2050", "51,2150", "58,2150", "59,2250"})
    @DisplayName("거리에 따른 요금을 계산한다.")
    void getFare(int distance, int expectFare) {
        Fare fare = new Fare(0);
        Age age = Age.ADULTS; 
        assertThat(fare.calculateFare(distance, age)).isEqualTo(expectFare);
    }

    @Test
    @DisplayName("추가 요금을 계산한다.")
    void calculateExtraFare() {
        //given
        Fare fare = new Fare(200);
        Age age = Age.ADULTS;
        //when
        int calculatedFare = fare.calculateFare(5, age);
        //then
        assertThat(calculatedFare).isEqualTo(1450);
    }




    @ParameterizedTest
    @CsvSource(value = {"INFANTS,0", "CHILDREN,450", "TEENAGERS,720", "ADULTS,1250", "SENIORS,0"})
    @DisplayName("연령별로 부과되는 요금을 계산한다.")
    void calculateFareByAge(Age age, int expectedFare) {
        //given
        Fare fare = new Fare(0);
        //when
        int calculatedFare = fare.calculateFare(5, age);
        //then
        assertThat(calculatedFare).isEqualTo(expectedFare);
    }
}
