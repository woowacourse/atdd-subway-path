package wooteco.subway.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PathServiceTest {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    private final PathService pathService;
    private List<Station> stations;

    PathServiceTest(LineDao lineDao, StationDao stationDao, SectionDao sectionDao,
        PathService pathService) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.pathService = pathService;
    }

    @BeforeEach
    public void setUp() {
        stations = createStations();
        createLine1();
        createLine2();
        createLine3();
        createLine4();
    }

    private void createLine1() {
        Long lineId1 = lineDao.save(new Line("1", "red", 20));
        sectionDao.save(new Section(stations.get(0), stations.get(1), 5), lineId1);
        sectionDao.save(new Section(stations.get(1), stations.get(2), 15), lineId1);
        sectionDao.save(new Section(stations.get(2), stations.get(3), 10), lineId1);
    }

    private void createLine2() {
        Long lineId2 = lineDao.save(new Line("2", "greed", 20));
        sectionDao.save(new Section(stations.get(1), stations.get(4), 4), lineId2);
        sectionDao.save(new Section(stations.get(4), stations.get(5), 7), lineId2);
        sectionDao.save(new Section(stations.get(5), stations.get(6), 4), lineId2);
    }

    private void createLine3() {
        Long lineId3 = lineDao.save(new Line("3", "orange", 20));
        sectionDao.save(new Section(stations.get(6), stations.get(2), 10), lineId3);
        sectionDao.save(new Section(stations.get(2), stations.get(7), 15), lineId3);
        sectionDao.save(new Section(stations.get(7), stations.get(8), 23), lineId3);
    }

    private void createLine4() {
        Long lineId4 = lineDao.save(new Line("4", "blue", 20));
        sectionDao.save(new Section(stations.get(9), stations.get(10), 10), lineId4);
    }

    private List<Station> createStations() {
        List<Station> stations = new ArrayList<>();
        for (char c = 'a';  c <= 'k' ; c++) {
            Station station = new Station(String.valueOf(c));
            stations.add(stationDao.save(station));
        }
        return stations;
    }

    @Test
    @DisplayName("최단거리 경로를 반환한다.")
    void findShortestPath(){
        PathRequest pathRequest = new PathRequest(stations.get(0).getId(), stations.get(8).getId(), 20);
        PathResponse pathResponse = pathService.findShortestPath(pathRequest);
        assertThat(pathResponse.getStations()).containsExactly(
            StationResponse.from(stations.get(0)),
            StationResponse.from(stations.get(1)),
            StationResponse.from(stations.get(2)),
            StationResponse.from(stations.get(7)),
            StationResponse.from(stations.get(8))
        );
        assertThat(pathResponse.getDistance()).isEqualTo(58);
        assertThat(pathResponse.getFare()).isEqualTo(2150);
    }

    @Test
    @DisplayName("출발역과 도착역이 같으면 예외를 던져야 한다.")
    void findSameStationsPath() {
        PathRequest pathRequest = new PathRequest(stations.get(0).getId(), stations.get(0).getId(), 0);
        assertThatThrownBy(() -> pathService.findShortestPath(pathRequest))
            .hasMessage("출발역과 도착역이 동일합니다.")
            .isInstanceOf(IllegalArgumentException.class);
    }
}
