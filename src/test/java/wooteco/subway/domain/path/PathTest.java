package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.domain.fixture.StationFixture.강남;
import static wooteco.subway.domain.fixture.StationFixture.선릉;
import static wooteco.subway.domain.fixture.StationFixture.이호선;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.distance.Kilometer;

@DisplayName("경로")
public class PathTest {

    @Test
    @DisplayName("거리를 알려준다.")
    void getDistance() {
        Path path = Path.of(List.of(이호선), 강남, 선릉);
        assertThat(path.getDistance()).isEqualTo(Kilometer.from(20));
    }
}
