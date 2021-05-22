package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

class PathTest {

    @DisplayName("구간과 출발/도착역을 이용해 경로를 만든다.")
    @Test
    public void createPathMap() {
        Station station1 = new Station("잠실역");
        Station station2 = new Station("일원역");
        Station station3 = new Station("수서역");

        Section section1 = new Section(station1, station2, 1);
        Section section2 = new Section(station2, station3, 2);
        Sections sections = new Sections(Arrays.asList(section1, section2));
        Path path = new Path(station1, station3, sections);

        assertThat(path.makePath()).containsExactly(station1, station2, station3);
        assertThat(path.totalDistance()).isEqualTo(3);
    }
}
