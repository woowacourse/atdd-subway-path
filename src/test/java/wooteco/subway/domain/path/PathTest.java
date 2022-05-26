package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.domain.fixture.StationFixture.강남;
import static wooteco.subway.domain.fixture.StationFixture.강남_역삼_10;
import static wooteco.subway.domain.fixture.StationFixture.선릉;
import static wooteco.subway.domain.fixture.StationFixture.선릉_강남_300;
import static wooteco.subway.domain.fixture.StationFixture.역삼_선릉_10;
import static wooteco.subway.domain.fixture.StationFixture.이호선;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.distance.Kilometer;
import wooteco.subway.domain.fare.Fare;

public class PathTest {

    @Test
    @DisplayName("거리를 알려준다.")
    void getDistance() {
        Map<Section, Fare> sectionWithExtraFare = Map.of(
                강남_역삼_10, new Fare(100),
                역삼_선릉_10, new Fare(100),
                선릉_강남_300, new Fare(200)
        );
        Path path = Path.of(sectionWithExtraFare, 강남, 선릉);
        assertThat(path.getDistance()).isEqualTo(Kilometer.from(20));
    }
}
