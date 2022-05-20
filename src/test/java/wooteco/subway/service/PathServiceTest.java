package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.respones.PathResponse;
import wooteco.subway.reopository.LineRepository;
import wooteco.subway.reopository.SectionRepository;
import wooteco.subway.reopository.StationRepository;

@SpringBootTest
class PathServiceTest {

    @Autowired
    PathService pathService;
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("TRUNCATE table line");
        jdbcTemplate.update("TRUNCATE table station");
        jdbcTemplate.update("TRUNCATE table section");
    }

    @DisplayName("최단 경로를 구한다.")
    @Test
    void createShortestPath() {
        Station 미르역 = new Station("미르역");
        Station 호호역 = new Station("호호역");
        Station 수달역 = new Station("수달역");
        Line 우테코노선 = new Line("우테코노선", "노랑");
        Long 미르역_id = stationRepository.save(미르역);
        Long 호호역_id = stationRepository.save(호호역);
        Long 수달역_id = stationRepository.save(수달역);
        Long 노선_id = lineRepository.save(우테코노선);
        미르역 = new Station(미르역_id, "미르역");
        호호역 = new Station(호호역_id, "호호역");
        수달역 = new Station(수달역_id, "수달역");
        우테코노선 = new Line(노선_id, "우테코노선", "노랑");

        sectionRepository.save(new Section(우테코노선, 미르역, 호호역, 10));
        sectionRepository.save(new Section(우테코노선, 호호역, 수달역, 10));

        PathResponse shortestPath = pathService.createShortestPath(new PathRequest(미르역_id, 수달역_id, 10L));

        assertThat(shortestPath.getStations()).hasSize(3);
    }

    @DisplayName("최단 경로를 구할 때 등록되지 않은 상행역을 조회시, 에러가 발생한다.")
    @Test
    void createShortestPathNotFoundUpStation() {
        Station 미르역 = new Station("미르역");
        Station 호호역 = new Station("호호역");
        Station 수달역 = new Station("수달역");
        Line 우테코노선 = new Line("우테코노선", "노랑");

        Long 미르역_id = stationRepository.save(미르역);
        Long 호호역_id = stationRepository.save(호호역);
        Long 수달역_id = stationRepository.save(수달역);
        Long 노선_id = lineRepository.save(우테코노선);
        미르역 = new Station(미르역_id, "미르역");
        호호역 = new Station(호호역_id, "호호역");
        수달역 = new Station(수달역_id, "수달역");
        우테코노선 = new Line(노선_id, "우테코노선", "노랑");

        sectionRepository.save(new Section(우테코노선, 미르역, 호호역, 10));

        assertThatThrownBy(() ->
                pathService.createShortestPath(new PathRequest(미르역_id, 수달역_id, 10L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("최단 경로를 요청하신 역이 구간에 존재하지 않습니다.");
    }

    @DisplayName("최단 경로를 구할 때 등록되지 않은 하행역을 조회시, 에러가 발생한다.")
    @Test
    void createShortestPathNotFoundDownStation() {
        Station 미르역 = new Station("미르역");
        Station 호호역 = new Station("호호역");
        Station 수달역 = new Station("수달역");
        Line 우테코노선 = new Line("우테코노선", "노랑");

        Long 미르역_id = stationRepository.save(미르역);
        Long 호호역_id = stationRepository.save(호호역);
        Long 수달역_id = stationRepository.save(수달역);
        Long 노선_id = lineRepository.save(우테코노선);
        호호역 = new Station(호호역_id, "호호역");
        수달역 = new Station(수달역_id, "수달역");
        우테코노선 = new Line(노선_id, "우테코노선", "노랑");

        sectionRepository.save(new Section(우테코노선, 수달역, 호호역, 10));

        assertThatThrownBy(() ->
                pathService.createShortestPath(new PathRequest(수달역_id, 미르역_id, 10L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("최단 경로를 요청하신 역이 구간에 존재하지 않습니다.");
    }
}
