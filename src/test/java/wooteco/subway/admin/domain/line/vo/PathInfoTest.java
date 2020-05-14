package wooteco.subway.admin.domain.line.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PathInfoTest {
    @Test
    void constructorWithException() {
        String name = "a";
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new PathInfo(name, name))
            .withMessage("출발역과 도착역은 같을 수 없습니다.");
    }
}