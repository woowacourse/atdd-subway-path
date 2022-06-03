package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.PathDijkstraDecisionStrategy;
import wooteco.subway.service.dto.request.PathRequest;
import wooteco.subway.service.dto.response.PathResponse;
import wooteco.subway.service.dto.response.StationResponse;

@DisplayName("지하철 경로 관련 service 테스트")
@JdbcTest
class PathServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineDao lineDao;
    private PathService pathService;

    private long stationId1;
    private long stationId2;
    private long stationId3;

    @BeforeEach
    void sepUp() {
        stationDao = new StationDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        pathService = new PathService(stationDao, sectionDao, lineDao, new PathDijkstraDecisionStrategy());

        stationId1 = stationDao.save(new Station(1L, "강남역"));
        stationId2 = stationDao.save(new Station(2L, "삼성역"));
        stationId3 = stationDao.save(new Station(3L, "역삼역"));
    }

    @DisplayName("조회한 경로에 대해 최단 경로, 최단 거리, 요금 정보를 반환한다.")
    @Test
    void findShortestPath() {
        // given
        long lineId = lineDao.save(Line.of(1L, "신분당선", "bg-red-600", 0));
        sectionDao.save(lineId, Section.of(stationId1, stationId2, 10));
        sectionDao.save(lineId, Section.of(stationId2, stationId3, 10));

        // when
        PathResponse pathResponse = pathService.findShortestPath(new PathRequest(stationId1, stationId3, 20));

        final List<String> stationNames = pathResponse.getStations().stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(20),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1450),
                () -> assertThat(stationNames).isEqualTo(List.of("강남역", "삼성역", "역삼역"))
        );
    }

    @DisplayName("추가요금이 있는 노선을 거쳐 지나갈 경우 추가요금을 포함하여 계산한다.")
    @Test
    void findShortestPathWithExtraFare() {
        // given
        long lineId = lineDao.save(Line.of(1L, "신분당선", "bg-red-600", 500));
        sectionDao.save(lineId, Section.of(stationId1, stationId2, 10));
        sectionDao.save(lineId, Section.of(stationId2, stationId3, 10));

        // when
        PathResponse pathResponse = pathService.findShortestPath(new PathRequest(stationId1, stationId3, 20));

        final List<String> stationNames = pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(20),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1950),
                () -> assertThat(stationNames).isEqualTo(List.of("강남역", "삼성역", "역삼역"))
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    @DisplayName("나이가 6 세 이상 13 세 미만일 경우 350 원을 공제한 금액에서 50% 를 할인하여 계산한다.")
    void findShortestPathChild(int age) {
        // given
        long lineId = lineDao.save(Line.of(1L, "신분당선", "bg-red-600", 0));
        sectionDao.save(lineId, Section.of(stationId1, stationId2, 10));
        sectionDao.save(lineId, Section.of(stationId2, stationId3, 10));

        // when
        PathResponse pathResponse = pathService.findShortestPath(new PathRequest(stationId1, stationId3, age));

        final List<String> stationNames = pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(20),
                () -> assertThat(pathResponse.getFare()).isEqualTo(550),
                () -> assertThat(stationNames).isEqualTo(List.of("강남역", "삼성역", "역삼역"))
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    @DisplayName("나이가 13 세 이상 19 세 미만일 경우 350 원을 공제한 금액에서 20% 를 할인하여 계산한다.")
    void findShortestPathTeenager(int age) {
        // given
        long lineId = lineDao.save(Line.of(1L, "신분당선", "bg-red-600", 0));
        sectionDao.save(lineId, Section.of(stationId1, stationId2, 10));
        sectionDao.save(lineId, Section.of(stationId2, stationId3, 10));

        // when
        PathResponse pathResponse = pathService.findShortestPath(new PathRequest(stationId1, stationId3, age));

        final List<String> stationNames = pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(20),
                () -> assertThat(pathResponse.getFare()).isEqualTo(880),
                () -> assertThat(stationNames).isEqualTo(List.of("강남역", "삼성역", "역삼역"))
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {19, 20})
    @DisplayName("나이가 19 세 이사일 경우 할인하지 않고 계산한다.")
    void findShortestPathAdult(int age) {
        // given
        long lineId = lineDao.save(Line.of(1L, "신분당선", "bg-red-600", 0));
        sectionDao.save(lineId, Section.of(stationId1, stationId2, 10));
        sectionDao.save(lineId, Section.of(stationId2, stationId3, 10));

        // when
        PathResponse pathResponse = pathService.findShortestPath(new PathRequest(stationId1, stationId3, age));

        final List<String> stationNames = pathResponse.getStations().stream()
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
