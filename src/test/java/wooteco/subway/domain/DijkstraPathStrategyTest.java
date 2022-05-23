package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.ClientException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DijkstraPathStrategyTest {

    @Test
    @DisplayName("예외 - 가고자 하는 역이 구간에 존재하지 않은 경우")
    void notExistPath() {
        Section section1 = new Section(1L, 1L, 1L, 2L, 10);
        Section section2 = new Section(2L, 1L, 2L, 3L, 20);

        assertThatThrownBy(() -> new DijkstraPathStrategy().findPath(List.of(section1, section2), 1L, 4L))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("구간에 등록되지 않은 역입니다.");
    }

    @Test
    @DisplayName("예외 - 가고자 하는 역이 구간에 존재하지만, 갈 방법이 없는 경우")
    void impossiblePath() {
        Section section1 = new Section(1L, 1L, 1L, 2L, 10);
        Section section2 = new Section(3L, 1L, 3L,4L, 20);

        assertThatThrownBy(() -> new DijkstraPathStrategy().findPath(List.of(section1, section2), 1L, 4L))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("갈 수 없는 경로입니다.");
    }
}
