package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.IllegalInputException;

class PathServiceTest extends ServiceTest {

    private final PathService pathService = new PathService();

    @Test
    @DisplayName("동일한 역의 경로를 조회할 경우 예외를 던진다.")
    void Find_SameStations_ExceptionThrown() {
        // when, then
        assertThatThrownBy(() -> pathService.find(1L, 1L))
                .isInstanceOf(IllegalInputException.class)
                .hasMessage("출발역과 도착역이 동일합니다.");
    }
}
