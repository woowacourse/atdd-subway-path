package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class StationTest {

    @DisplayName("노선의 이름이 null 또는 빈 값이면 예외를 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void notAllowNullOrBlankName(String name) {
        assertThatThrownBy(() -> new Station(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역 이름은 빈 값일 수 없습니다.");
    }
    
}
