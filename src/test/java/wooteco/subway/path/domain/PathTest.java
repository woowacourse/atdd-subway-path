package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    @DisplayName("가장 짧은 거리 목록과 거리 구하기")
    @Test
    public void getShortestStations() {
        Station 강남역 = new Station(1L, "강남역");
        Station 잠실역 = new Station(2L, "잠실역");
        Station 몽촌역 = new Station(3L, "몽촌역");
        Station 역삼역 = new Station(4L, "역삼역");

        // 강남-잠실-몽촌:20 & 강남 - 역삼 - 몽촌:15 & 강남 - 몽촌: 40
        List<Section> sections = Arrays.asList(
                new Section(1L, 강남역, 잠실역, 10),
                new Section(2L, 잠실역, 몽촌역, 10),
                new Section(3L, 강남역, 역삼역, 10),
                new Section(4L, 역삼역, 몽촌역, 5),
                new Section(5L, 강남역, 몽촌역, 40)
        );
        Path path = new Path(강남역, 몽촌역, sections);
        List<Station> shortestPath = path.getShortestStations();
        int shortestDistance = path.getShortestDistance();

        assertThat(shortestPath.size()).isEqualTo(3);
        assertThat(shortestPath).containsExactly(강남역, 역삼역, 몽촌역);
        assertThat(shortestDistance).isEqualTo(15);
    }

}