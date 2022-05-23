package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.service.ServiceTestFixture.deleteAllLine;
import static wooteco.subway.service.ServiceTestFixture.deleteAllStation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.repository.JdbcLineRepository;
import wooteco.subway.repository.JdbcSectionRepository;
import wooteco.subway.repository.JdbcStationRepository;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.repository.dao.JdbcLineDao;
import wooteco.subway.repository.dao.JdbcSectionDao;
import wooteco.subway.repository.dao.JdbcStationDao;
import wooteco.subway.repository.dao.LineDao;
import wooteco.subway.repository.dao.SectionDao;
import wooteco.subway.repository.dao.StationDao;
import wooteco.subway.service.dto.PathResponse;
import wooteco.subway.service.dto.PathServiceRequest;

@JdbcTest
class PathServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionRepository sectionRepository;
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private PathService pathService;

    
    
    @BeforeEach
    void setUp() {
        StationDao stationDao = new JdbcStationDao(jdbcTemplate);
        SectionDao sectionDao = new JdbcSectionDao(jdbcTemplate);
        LineDao lineDao = new JdbcLineDao(jdbcTemplate);
        stationRepository = new JdbcStationRepository(stationDao);
        sectionRepository = new JdbcSectionRepository(sectionDao, lineDao);
        lineRepository = new JdbcLineRepository(lineDao);

        pathService = new PathService(sectionRepository, stationRepository);

        deleteAllLine(lineRepository);
        deleteAllStation(stationRepository);
    }

    @Test
    void findShortestPath() {
        // given
        Long savedId1 = stationRepository.save(new Station("교대역"));
        Long savedId2 = stationRepository.save(new Station("강남역"));
        Long savedId3 = stationRepository.save(new Station("역삼역"));

        Line line = new Line("2호선", "green", 0);
        Long savedLineId = lineRepository.save(line);
        Line insertLine = new Line(savedLineId, line.getName(), line.getColor(), line.getExtraFare());

        Section section1 = new Section(insertLine, savedId1, savedId2, 3);
        Section section2 = new Section(insertLine, savedId2, savedId3, 4);

        sectionRepository.save(section1);
        sectionRepository.save(section2);
        PathServiceRequest pathServiceRequest = new PathServiceRequest(savedId1, savedId3, 20);

        // when
        PathResponse result = pathService.findShortestPath(pathServiceRequest);

        // then
        assertThat(result.getStations().size()).isEqualTo(3);
        assertThat(result.getFare()).isEqualTo(1250);
        assertThat(result.getDistance()).isEqualTo(7);
    }
}
