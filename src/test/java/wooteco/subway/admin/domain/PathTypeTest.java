package wooteco.subway.admin.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PathTypeTest {
    @Test
    void valueOf() {
        PathType distance = PathType.valueOf("DISTANCE");
        assertThat(distance).isEqualTo(PathType.DISTANCE);
    }
}