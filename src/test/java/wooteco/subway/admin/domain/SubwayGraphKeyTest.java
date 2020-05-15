package wooteco.subway.admin.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayGraphKeyTest {

    @DisplayName("SubwayGraphKey의 String을 입력하면 해당 SubwayGraphKey 반환")
    @ParameterizedTest
    @CsvSource(value = {"distance,DISTANCE", "duration,DURATION"})
    void of(String symbol, SubwayGraphKey key) {
        assertThat(SubwayGraphKey.of(symbol)).isEqualTo(key);
    }

    @DisplayName("SubwayGraphKey에 없는 String을 입력하면 예외 발생")
    @Test
    void ofException() {
        assertThatThrownBy(() -> SubwayGraphKey.of("bibab"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void makeGraph() {
        //given

        //when

        //then
    }
}