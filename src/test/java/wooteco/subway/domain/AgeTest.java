package wooteco.subway.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeTest {

    @ParameterizedTest(name = "{0} 살은 미취학 아동입니다." )
    @ValueSource(ints = {1, 5})
    void findPreschoolerAgePolicy(int age) {
        Age agePolicy = Age.findAgePolicy(age);
        assertThat(agePolicy).isEqualTo(Age.PRESCHOOLER);
    }

    @ParameterizedTest(name = "{0} 살은 어린이입니다." )
    @ValueSource(ints = {6, 12})
    void findChildAgePolicy(int age) {
        Age agePolicy = Age.findAgePolicy(age);
        assertThat(agePolicy).isEqualTo(Age.CHILD);
    }

    @ParameterizedTest(name = "{0} 살은 청소년입니다." )
    @ValueSource(ints = {13, 18})
    void findTeenagerAgePolicy(int age) {
        Age agePolicy = Age.findAgePolicy(age);
        assertThat(agePolicy).isEqualTo(Age.TEENAGER);
    }

    @ParameterizedTest(name = "{0} 살은 성인입니다." )
    @ValueSource(ints = {19, 50})
    void findAdultAgePolicy(int age) {
        Age agePolicy = Age.findAgePolicy(age);
        assertThat(agePolicy).isEqualTo(Age.ADULT);
    }
}
