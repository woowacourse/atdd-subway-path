package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.repository.JdbcLineRepository;
import wooteco.subway.repository.JdbcSectionRepository;
import wooteco.subway.repository.JdbcStationRepository;

@JdbcTest
class PathServiceTest {

    private final StationService stationService;
    private final LineService lineService;
    private final PathService pathService;

    private StationResponse createdStation1;
    private StationResponse createdStation2;

    @Autowired
    public PathServiceTest(JdbcTemplate jdbcTemplate) {
        StationDao stationDao = new StationDao(jdbcTemplate);
        SectionDao sectionDao = new SectionDao(jdbcTemplate);
        LineDao lineDao = new LineDao(jdbcTemplate);
        JdbcStationRepository stationRepository = new JdbcStationRepository(stationDao);
        JdbcSectionRepository sectionRepository = new JdbcSectionRepository(sectionDao, stationRepository);
        JdbcLineRepository lineRepository = new JdbcLineRepository(lineDao, sectionRepository);

        stationService = new StationService(stationRepository);
        lineService = new LineService(lineRepository, stationRepository, sectionRepository);
        pathService = new PathService(sectionRepository, stationRepository, lineRepository);
    }

    @BeforeEach
    void setUp() {
        createdStation1 = stationService.createStation(new StationRequest("강남역"));
        createdStation2 = stationService.createStation(new StationRequest("역삼역"));
    }

    @DisplayName("노선의 추가 요금이 없을 경우 경로 거리 별 요금을 계산한다.")
    @ParameterizedTest(name = "[{index}] {0}km 일 때, 운임은 {1}원")
    @CsvSource(value = {"10,1250", "15,1350", "58,2150"})
    void getPath(int distance, int expectedFare) {
        // given
        lineService.createLine(
                new LineRequest("2호선", "bg-green-600", 0, createdStation1.getId(), createdStation2.getId(), distance));

        // when
        PathResponse pathResponse = pathService.getPath(
                new PathRequest(createdStation1.getId(), createdStation2.getId(), 20));

        // then
        assertThat(pathResponse.getFare()).isEqualTo(expectedFare);
    }
}
