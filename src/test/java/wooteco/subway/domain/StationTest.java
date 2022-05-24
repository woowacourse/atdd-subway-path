package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StationTest {
    @Test
    @DisplayName("역 이름이 빈 값일 때 예외를 발생시킨다.")
    void invalidStationName() {
        assertThatThrownBy(() -> new Station(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 빈 값일 수 없습니다.");
    }
}
