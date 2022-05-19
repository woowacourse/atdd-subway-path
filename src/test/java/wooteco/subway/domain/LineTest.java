package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.ClientException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    @Test
    @DisplayName("지하철 노선 - 공백 예외")
    void checkNull() {
        assertThatThrownBy(() -> new Line("2호선", "", 10))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("지하철 노선의 이름과 색을 모두 입력해주세요.");
    }

    @Test
    @DisplayName("지하철 노선 - 음수 추가요금 예외")
    void checkNegativeExtraFare() {
        assertThatThrownBy(() -> new Line("2호선", "yellow", -1))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("추가요금은 음수가 될 수 없습니다.");
    }
}
