package wooteco.subway.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.SearchPathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.LineStationRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
public class PathServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";
    private static final String STATION_NAME5 = "가깝고느린역";
    private static final String STATION_NAME6 = "연결되지 않은 역";
    private static final String STATION_NAME7 = "연결되지 않은 역2";

    private PathService pathService;

    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineStationRepository lineStationRepository;

    private Line line;
    private Line line2;
    private Line line3;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Station station6;
    private Station station7;

    @BeforeEach
    void setUp() {
        station1 = new Station(STATION_NAME1);
        station2 = new Station(STATION_NAME2);
        station3 = new Station(STATION_NAME3);
        station4 = new Station(STATION_NAME4);
        station5 = new Station(STATION_NAME5);
        station6 = new Station(STATION_NAME6);
        station7 = new Station(STATION_NAME7);
        stationRepository.save(station1);
        stationRepository.save(station2);
        stationRepository.save(station3);
        stationRepository.save(station4);
        stationRepository.save(station5);
        stationRepository.save(station6);
        stationRepository.save(station7);

        line = new Line("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2 = new Line("11호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line3 = new Line("21호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);

        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(2L, 3L, 10, 10));
        line.addLineStation(new LineStation(3L, 4L, 10, 10));
        line2.addLineStation(new LineStation(2L, 5L, 1, 100));
        line2.addLineStation(new LineStation(5L, 4L, 1, 100));
        line3.addLineStation(new LineStation(6L, 7L, 1, 1));

        lineRepository.save(line);
        lineRepository.save(line2);
        lineRepository.save(line3);

        pathService = new PathService(lineRepository, stationRepository, lineStationRepository);
    }

    @DisplayName("최소 거리 조회 테스트")
    @Test
    public void shortestDistance() {
        SearchPathResponse searchPathResponse = pathService.searchPath(STATION_NAME1, STATION_NAME4, "distance");
        assertThat(searchPathResponse.getPathStationNames()).contains("가깝고느린역");

        searchPathResponse = pathService.searchPath(STATION_NAME1, STATION_NAME4, "duration");
        assertThat(searchPathResponse.getPathStationNames()).contains("선릉역");

////    출발역과 도착역이 같은 경우
//        searchPathResponse = pathService.searchPath(STATION_NAME1, STATION_NAME1, "duration");
//        System.out.println("-----------------");
//        System.out.println(searchPathResponse.getPathStationNames());
//        System.out.println(searchPathResponse.getDistanceSum());
//        System.out.println(searchPathResponse.getDurationSum());

////    출발역과 도착역이 연결이 되어 있지 않은 경우
//        searchPathResponse = pathService.searchPath(STATION_NAME1, STATION_NAME7, "duration");
//        System.out.println("-----------------");
//        System.out.println(searchPathResponse.getPathStationNames());
//        System.out.println(searchPathResponse.getDistanceSum());
//        System.out.println(searchPathResponse.getDurationSum());
//
//////    존재하지 않은 출발역이나 도착역을 조회 할 경우
//        searchPathResponse = pathService.searchPath(STATION_NAME1, "x", "duration");
//        System.out.println("-----------------");
//        System.out.println(searchPathResponse.getPathStationNames());
//        System.out.println(searchPathResponse.getDistanceSum());
//        System.out.println(searchPathResponse.getDurationSum());
    }
}
