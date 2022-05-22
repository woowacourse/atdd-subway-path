package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.element.Line;
import wooteco.subway.domain.element.Section;
import wooteco.subway.domain.element.Station;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.service.dto.request.PathsRequest;
import wooteco.subway.service.dto.request.SectionRequest;
import wooteco.subway.service.dto.response.PathResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private LineService lineService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.update("delete from section");
        jdbcTemplate.update("delete from line");
        jdbcTemplate.update("delete from station");
    }

    @Test
    @DisplayName("두 역 사이의 최단 경로를 조회한다. 강남 - 판교")
    void getPathsResponse() {
        Station 강남 = stationRepository.save(new Station("강남"));
        Station 양재 = stationRepository.save(new Station("양재"));
        Station 판교 = stationRepository.save(new Station("판교"));
        Station 광교 = stationRepository.save(new Station("광교"));
        Line 신분당선 = lineRepository.save(new Line("신분당선", "red", 0));
        sectionRepository.save(new Section(신분당선, 강남, 광교, 10));
        SectionRequest sectionRequest1 = new SectionRequest(강남.getId(), 양재.getId(), 4);
        lineService.createSection(신분당선.getId(), sectionRequest1);
        SectionRequest sectionRequest2 = new SectionRequest(양재.getId(), 판교.getId(), 4);
        lineService.createSection(신분당선.getId(), sectionRequest2);

        PathResponse actual = pathService.showPaths(new PathsRequest(강남.getId(), 판교.getId(), 15));

        assertThat(actual.getStations())
                .extracting("name")
                .isEqualTo(List.of("강남", "양재", "판교"));
        assertThat(actual.getDistance()).isEqualTo(8);
        assertThat(actual.getFare()).isEqualTo(1250);
    }
}
