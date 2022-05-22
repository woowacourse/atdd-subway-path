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
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

@JdbcTest
class PathServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PathService pathService;
    private LineService lineService;

    private StationResponse createdStation1;
    private StationResponse createdStation2;

    @BeforeEach
    void setUp() {
        pathService = new PathService(new SectionDao(jdbcTemplate), new StationDao(jdbcTemplate));
        lineService = new LineService(new LineDao(jdbcTemplate), new StationDao(jdbcTemplate),
                new SectionDao(jdbcTemplate));

        StationService stationService = new StationService(new StationDao(jdbcTemplate));
        createdStation1 = stationService.createStation(new StationRequest("강남역"));
        createdStation2 = stationService.createStation(new StationRequest("역삼역"));
    }

    @DisplayName("경로 거리 별 요금을 계산한다.")
    @ParameterizedTest(name = "[{index}] {0}km 일 때, 운임은 {1}원")
    @CsvSource(value = {"10,1250", "15,1350", "58,2150"})
    void getPath(int distance, int expectedFare) {
        // given
        lineService.createLine(
                new LineRequest("2호선", "bg-green-600", 500, createdStation1.getId(), createdStation2.getId(),
                        distance));

        // when
        PathResponse pathResponse = pathService.getPath(createdStation1.getId(), createdStation2.getId());

        // then
        assertThat(pathResponse.getFare()).isEqualTo(expectedFare);
    }
}
