package wooteco.subway.admin.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.service.errors.PathException;

public class StationTest {
    @Test
    @DisplayName("지하철 이름이 한국어가 아니면 익셉션이 발생한다")
    void StationNameTest() {
        Assertions.assertThatThrownBy(() -> new Station("testName"))
                .isInstanceOf(PathException.class);
    }
}
