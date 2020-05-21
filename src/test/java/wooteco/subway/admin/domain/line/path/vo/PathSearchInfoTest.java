package wooteco.subway.admin.domain.line.path.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PathSearchInfoTest {
    @Test
    void constructorWithException() {
        String name = "a";
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new PathSearchInfo(name, name))
            .withMessage("출발역과 도착역은 같을 수 없습니다.");
    }
}