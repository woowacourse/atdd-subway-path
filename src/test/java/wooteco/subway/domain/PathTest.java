package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.common.TestFixtures.신당역;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.SubwayException;

class PathTest {

    @DisplayName("동일한 역이 들어올 시 예외가 발생한다.")
    @Test
    void validateSameStations() {
        assertThatThrownBy(() -> new Path(신당역, 신당역))
                .isInstanceOf(SubwayException.class);
    }
}
