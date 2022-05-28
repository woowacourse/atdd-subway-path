package wooteco.subway.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PathAdapterTest {

    private Station station1;
    private Station station2;
    private Station station5;
    private Line line1;
    private Line line2;

    @BeforeEach
    void setUp() {
        initStations();
        line1 = new Line(1L, "line1", "color1", 10);
        line2 = new Line(2L, "line2", "color2", 5);
    }

    private void initStations() {
        station1 = new Station(1L, "station1");
        station2 = new Station(2L, "station2");
        station5 = new Station(5L, "station5");
    }

    @DisplayName("출발역에서 도착역까지 최단 경로의 역들을 찾는다.")
    @Test
    void getShortestPath() {
        //given
        Section section1To2 = new Section(1L, line1.getId(), station1, station2, 4);
        Section section2To5 = new Section(3L, line1.getId(), station2, station5, 4);

        Section section1To5 = new Section(2L, line2.getId(), station1, station5, 10);
        line1.addSection(section1To2);
        line1.addSection(section2To5);

        line2.addSection(section1To5);
        //when
        PathFinder pathFinder = new SubwayGraph(List.of(section1To2, section1To5, section2To5));
        PathAdapter pathAdapter = new PathAdapter(pathFinder);
        Path path = pathAdapter.getShortestPath(station1, station5, 0, 50);
        //then
        assertThat(path.getStations()).containsExactly(station1, station2, station5);
        assertThat(path.getDistance()).isEqualTo(8);
    }

    @DisplayName("출발역에서 도착역까지 가는 노선들 중 가장 비싼 요금을 가진 노선의 id를 얻는다.")
    @Test
    void getExpensiveLineId() {
        //given
        Line line3 = new Line(3L, "line3", "color3", 1000);
        Station station3 = new Station(3L, "station3");
        Section section1To3 = new Section(line3.getId(), station1, station3, 3);
        Section section3To5 = new Section(line3.getId(), station3, station5, 3);
        Section section1To2 = new Section(1L, line1.getId(), station1, station2, 4);
        Section section2To5 = new Section(3L, line1.getId(), station2, station5, 4);
        Section section1To5 = new Section(2L, line2.getId(), station1, station5, 10);
        //when
        PathFinder pathFinder = new SubwayGraph(List.of(section1To2, section1To5, section2To5, section1To3, section3To5));
        PathAdapter pathAdapter = new PathAdapter(pathFinder);
        Long expensiveLineId = pathAdapter.getExpensiveLineId(station1, station5);
        //then
        assertThat(expensiveLineId).isEqualTo(line3.getId());
    }
}
