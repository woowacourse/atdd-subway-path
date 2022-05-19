package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static wooteco.subway.domain.factory.SectionFactory.AB3;
import static wooteco.subway.domain.factory.SectionFactory.BC3;

import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.factory.SectionFactory;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.service.dto.LineDto;

@DisplayName("PathService 는 ")
@JdbcTest
class PathServiceTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private StationDao stationDao;
    private LineRepository lineRepository;
    private PathService pathService;

    @BeforeEach
    void setUp() {
        final LineDao lineDao = new LineDao(namedParameterJdbcTemplate, dataSource);
        final SectionDao sectionDao = new SectionDao(namedParameterJdbcTemplate, dataSource);
        stationDao = new StationDao(namedParameterJdbcTemplate, dataSource);
        final StationRepository stationRepository = new StationRepository(stationDao);
        lineRepository = new LineRepository(lineDao, stationDao, sectionDao);
        pathService = new PathService(stationRepository, lineRepository);
    }

    @DisplayName("조회한 경로에 대해 최단 경로, 최단 거리, 요금 정보를 반환한다.")
    @Test
    void findShortestPath() {
        final Long upStationId = stationDao.save(new Station("a")).getId();
        final Long downStationId = stationDao.save(new Station("b")).getId();
        lineRepository.save(new LineDto("신분당선", "bg-red-600", upStationId,
                downStationId, 10));

        final PathResponse actualPathResponse = pathService.findShortestPath(upStationId, downStationId);
        final List<String> stationNames = actualPathResponse.getStationResponses().stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList());
        assertAll(
                () -> assertThat(actualPathResponse.getDistance()).isEqualTo(10),
                () -> assertThat(actualPathResponse.getFare()).isEqualTo(1250),
                () -> assertThat(stationNames).isEqualTo(List.of("a", "b"))
        );
    }
}
