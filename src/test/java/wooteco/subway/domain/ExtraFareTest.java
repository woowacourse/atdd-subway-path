package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.exception.IllegalInputException;

public class ExtraFareTest {


    @ParameterizedTest
    @DisplayName("추가 요금의 금액이 0보다 작으면 예외를 던진다.")
    @ValueSource(ints = {-10, -1})
    void NewFare_LessThan0_ExceptionThrown(final int fareValue) {
        // when, then
        assertThatThrownBy(() -> new ExtraFare(fareValue))
                .isInstanceOf(IllegalInputException.class)
                .hasMessage("추가 요금은 0보다 작을 수 없습니다.");
    }
}
