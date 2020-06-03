package wooteco.subway.admin.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PathTypeTest {
    @Test
    void valueOf() {
        PathType distance = PathType.valueOf("DISTANCE");
        assertThat(distance).isEqualTo(PathType.DISTANCE);
    }

    @Test
    void valueOfDuration() {
        PathType duration = PathType.valueOf("DURATION");
        assertThat(duration).isEqualTo(PathType.DURATION);
    }
}