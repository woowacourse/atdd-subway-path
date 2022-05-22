package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeTest {

    @DisplayName("범위에 해당하는 연령을 불러온다.")
    @ParameterizedTest
    @CsvSource(value = {"0,BABIES", "5,BABIES", "6,CHILDREN", "12,CHILDREN", "13,TEENAGERS", "18,TEENAGERS", "19,ADULTS", "25,ADULTS"})
    void findAge(int age, Age ageGroup) {
        assertThat(Age.findAge(age)).isEqualTo(ageGroup);
    }

    @DisplayName("연령에 따라 요금이 할인된다.")
    @ParameterizedTest
    @CsvSource(value = {"BABIES,0", "CHILDREN,450", "TEENAGERS,720", "ADULTS,1250"})
    void discountFareByAge(Age ageGroup, int expectedFare) {
        assertThat(ageGroup.discountFare(1250)).isEqualTo(expectedFare);
    }
}
