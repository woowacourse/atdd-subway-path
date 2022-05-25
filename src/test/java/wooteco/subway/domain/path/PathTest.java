package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PathTest {

    @DisplayName("추가 요금이 없는 경우의 운임 요금을 계산한다.")
    @CsvSource(value = {"9:1250", "10:1250", "11:1350", "33:1750", "50:2050", "58:2150"}, delimiter = ':')
    @ParameterizedTest
    void calculate(int distance, int expected) {
        Path path = new Path(distance, Set.of(1L, 2L), List.of(1L, 2L, 3L));
        int fare = path.calculateFare(20, 0);

        assertThat(fare).isEqualTo(expected);
    }

    @DisplayName("추가 요금이 있는 경우, 운임 요금에 추가하여 계산한다.")
    @CsvSource(value = {"9:1750", "10:1750", "11:1850", "33:2250", "50:2550", "58:2650"}, delimiter = ':')
    @ParameterizedTest
    void calculateWithExtraCharge(int distance, int expected) {
        Path path = new Path(distance, Set.of(1L, 2L), List.of(1L, 2L, 3L));
        int fare = path.calculateFare(20, 500);

        assertThat(fare).isEqualTo(expected);
    }

    @DisplayName("6세 이상, 13세 미만의 어린이는 운임에서 350원을 공제한 금액의 50%가 할인된다.")
    @CsvSource(value = {"6:700", "7:700", "12:700"}, delimiter = ':')
    @ParameterizedTest
    void calculateChildCharge(int age, int expected) {
        Path path = new Path(9, Set.of(1L, 2L), List.of(1L, 2L, 3L));
        int fare = path.calculateFare(age, 500);

        assertThat(fare).isEqualTo(expected);
    }

    @DisplayName("13세 이상, 19세 미만의 청소년은 운임에서 350원을 공제한 금액의 20%가 할인된다.")
    @CsvSource(value = {"13:1120", "14:1120", "18:1120", "19:1750"}, delimiter = ':')
    @ParameterizedTest
    void calculateAdolescentCharge(int age, int expected) {
        Path path = new Path(9, Set.of(1L, 2L), List.of(1L, 2L, 3L));
        int fare = path.calculateFare(age, 500);

        assertThat(fare).isEqualTo(expected);
    }
}
