package wooteco.subway.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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


    private PathService pathService;

    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineStationRepository lineStationRepository;

    private Line line;
    private Line line2;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;

    @BeforeEach
    void setUp() {
        station1 = new Station(STATION_NAME1);
        station2 = new Station(STATION_NAME2);
        station3 = new Station(STATION_NAME3);
        station4 = new Station(STATION_NAME4);
        station5 = new Station(STATION_NAME5);
        stationRepository.save(station1);
        stationRepository.save(station2);
        stationRepository.save(station3);
        stationRepository.save(station4);
        stationRepository.save(station5);

        line = new Line("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2 = new Line("21호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);

        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(2L, 3L, 10, 10));
        line.addLineStation(new LineStation(3L, 4L, 10, 10));
        line2.addLineStation(new LineStation(2L, 5L, 1, 100));
        line2.addLineStation(new LineStation(5L, 4L, 1, 100));

        lineRepository.save(line);
        lineRepository.save(line2);

        pathService = new PathService(lineRepository, stationRepository, lineStationRepository);
    }

    @DisplayName("최소 시간 조회 테스트")
    @Test
    public void shortestTime() {
        SearchPathResponse searchPathResponse = pathService.searchPath(STATION_NAME1, STATION_NAME4, "duration");
        assertThat(searchPathResponse.getPathStationNames()).contains("선릉역");
    }
}
