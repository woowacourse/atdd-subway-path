package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.response.PathResponse;
import wooteco.subway.service.dto.response.StationResponse;

@DisplayName("지하철 경로 관련 service 테스트")
@JdbcTest
class PathServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;
    private SectionDao sectionDao;
    private PathService pathService;

    @BeforeEach
    void sepUp() {
        stationDao = new StationDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
        pathService = new PathService(stationDao, sectionDao);
    }

    @DisplayName("조회한 경로에 대해 최단 경로, 최단 거리, 요금 정보를 반환한다.")
    @Test
    void findShortestPath() {
        // given
        long stationId1 = stationDao.save(new Station(1L, "강남역"));
        long stationId2 = stationDao.save(new Station(2L, "삼성역"));
        long stationId3 = stationDao.save(new Station(3L, "역삼역"));

        sectionDao.save(1L, new Section(stationId1, stationId2, 10));
        sectionDao.save(1L, new Section(stationId2, stationId3, 10));

        // when
        PathResponse pathResponse = pathService.findShortestPath(stationId1, stationId3);

        final List<String> stationNames = pathResponse.getStationResponses().stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(20),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1450),
                () -> assertThat(stationNames).isEqualTo(List.of("강남역", "삼성역", "역삼역"))
        );
    }
}
