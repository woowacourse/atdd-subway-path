package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.TestFixtures.STANDARD_DISTANCE;
import static wooteco.subway.TestFixtures.동묘앞역;
import static wooteco.subway.TestFixtures.신당역;
import static wooteco.subway.TestFixtures.창신역;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.SubwayException;

public class PathTest {

    @DisplayName("동일한 역으로 경로 조회시 예외를 발생한다.")
    @Test
    void createSameStationPathException() {
        assertThatThrownBy(() -> new Path(PathGraph.create(createSections()), 신당역, 신당역))
                .isInstanceOf(SubwayException.class);
    }

    @DisplayName("경로 중 최소 거리를 계산한다.")
    @Test
    void calculateMinDistance() {
        Path path = new Path(PathGraph.create(createSections()), 신당역, 창신역);
        assertThat(path.calculateMinDistance()).isEqualTo(20);
    }

    private Sections createSections() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 동묘앞역, 창신역, STANDARD_DISTANCE);
        return new Sections(List.of(section, section1));
    }
}
