package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.exception.DataNotExistException;

class AgeBoundaryTest {

    @DisplayName("나이에 맞는 AgeBoundary를 가져온다.")
    @ParameterizedTest
    @CsvSource(value = {"0:BABY", "4:BABY", "5:CHILD", "12:CHILD", "13:YOUTH", "18:YOUTH", "19:ADULT"}, delimiter = ':')
    void getAgeDiscountStrategyTest(int age, AgeBoundary expected) {
        assertThat(AgeBoundary.from(age)).isEqualTo(expected);
    }

    @DisplayName("AgeBoundary애 벗어나는 나이를 입력한 경우 예외가 발생한다.")
    @Test
    void invalidAge() {
        assertThatThrownBy(() -> AgeBoundary.from(-1))
                .isInstanceOf(DataNotExistException.class)
                .hasMessage("존재하지 않는 나이입니다.");
    }

    @DisplayName("나이별로 할인된 요금을 계산하여 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"BABY:0", "CHILD:450", "YOUTH:720", "ADULT:1250"}, delimiter = ':')
    void calculateDiscountFare(AgeBoundary ageBoundary, int expected) {
        assertThat(ageBoundary.discountFare(1250)).isEqualTo(expected);
    }
}
