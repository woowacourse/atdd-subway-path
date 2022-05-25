package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Line 도메인 객체 테스트")
class LineTest {

    @DisplayName("추가요금 정보가 0 보다 작을 경우 예외가 발생한다.")
    @Test
    void extraFareUnder0() {
        // when & then
        assertThatThrownBy(
                () -> Line.of("강남역", "bg-red-600", -1)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("추가요금은 0 이상이어야 합니다.");
    }

    @DisplayName("추가요금 정보가 돈의 단위가 아닐 경우 예외가 발생한다.")
    @Test
    void extraFareNotMoneyUnit() {
        // when & then
        assertThatThrownBy(
                () -> Line.of("강남역", "bg-red-600", 1234)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("추가요금은 10원 단위로 입력해주세요.");
    }
}
