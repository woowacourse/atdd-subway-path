package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.dao.SectionTable;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {

    @DisplayName("가장 짧은 거리 목록과 거리 구하기")
    @Test
    public void getShortestStations() {
        Station 강남역 = new Station(1L, "강남역");
        Station 잠실역 = new Station(2L, "잠실역");
        Station 몽촌역 = new Station(3L, "몽촌역");
        Station 역삼역 = new Station(4L, "역삼역");

        // 강남-잠실-몽촌:20 & 강남 - 역삼 - 몽촌:15 & 강남 - 몽촌: 40
        List<SectionTable> sectionTables = Arrays.asList(
                new SectionTable(1L, 1L, 강남역.getId(), 잠실역.getId(), 10),
                new SectionTable(2L, 1L, 잠실역.getId(), 몽촌역.getId(), 10),
                new SectionTable(3L, 1L, 강남역.getId(), 역삼역.getId(), 10),
                new SectionTable(4L, 1L, 역삼역.getId(), 몽촌역.getId(), 5),
                new SectionTable(5L, 1L, 강남역.getId(), 몽촌역.getId(), 40)
        );
        PathFinder pathFinder = new PathFinder(sectionTables);
        List<Long> shortestStationIds = pathFinder.getShortestStations(강남역.getId(), 몽촌역.getId());

        int shortestDistance = pathFinder.getShortestDistance(강남역.getId(), 몽촌역.getId());

        assertThat(shortestStationIds.size()).isEqualTo(3);
        assertThat(shortestStationIds).containsExactly(강남역.getId(), 역삼역.getId(), 몽촌역.getId());
        assertThat(shortestDistance).isEqualTo(15);
    }

}