package wooteco.subway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeTest {

    @ParameterizedTest
    @CsvSource(value = {"1,INFANTS", "5,INFANTS", "6,CHILDREN", "12,CHILDREN", "13,TEENAGERS", "18,TEENAGERS", "19,ADULTS", "64,ADULTS", "65,SENIORS"})
    @DisplayName("나이에 따라 연령별 그룹을 판단한다.")
    void getAge(int age, Age calculatedAge) {
        //given

        //when

        //then
        assertThat(Age.calculateAge(age)).isEqualTo(calculatedAge);
    }

    @ParameterizedTest
    @CsvSource(value = {"INFANTS,0", "CHILDREN,450", "TEENAGERS,720", "ADULTS,1250", "SENIORS,0"})
    @DisplayName("나이에 따라 연령별 그룹을 판단한다.")
    void calculateDiscount(Age age, int fare) {
        //given

        //when

        //then
        int defaultFare = 1250;
        assertThat(age.calculateDiscount(defaultFare)).isEqualTo(fare);
    }
}
