package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.line.Line;

import java.util.Set;

class LineTest {

    @Test
    @DisplayName("이름이 같은지 확인한다.")
    public void hasSameNameWith() {
        // given
        final Line line = new Line("신분당선", "bg-red-600", 0);
        // when
        final boolean hasSameName = line.hasSameNameWith(new Line("신분당선", "bg-red-600", 0));
        // then
        assertThat(hasSameName).isTrue();
    }

    @Test
    @DisplayName("주어진 id 집합중에 포함되어있는지 확인한다.")
    void isIncludedWhenTrue() {
        //given
        final Line line = new Line(1L, "신분당선", "bg-red-600", 0);
        //when
        final boolean isIncluded = line.isIncluded(Set.of(1L, 2L, 3L));
        //then
        assertThat(isIncluded).isTrue();
    }

    @Test
    @DisplayName("주어진 id 집합중에 포함되어있는지 확인한다.")
    void isIncludedWhenFalse() {
        //given
        final Line line = new Line(5L, "신분당선", "bg-red-600", 0);
        //when
        final boolean isIncluded = line.isIncluded(Set.of(1L, 2L, 3L));
        //then
        assertThat(isIncluded).isFalse();
    }
}
