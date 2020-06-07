package wooteco.subway.admin.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PathTypeTest {

    @Test
    void valueOfSucceed() {
        PathType distance = PathType.valueOf("DISTANCE");
        assertThat(distance).isEqualTo(PathType.DISTANCE);
    }

    @Test
    void valueOfFail() {
        assertThatThrownBy(() -> PathType.valueOf("wrongName"))
                .isInstanceOf(IllegalArgumentException.class);

    }
}