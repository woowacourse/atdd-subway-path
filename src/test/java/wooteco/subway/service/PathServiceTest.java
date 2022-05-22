package wooteco.subway.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.fixture.StationFixture;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql("classpath:schema.sql")
public class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Autowired
    private StationService stationService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private LineService lineService;

    @BeforeEach
    void setUp() {
        stationService.save(new StationRequest("A역"));
        stationService.save(new StationRequest("B역"));
        stationService.save(new StationRequest("C역"));

        lineService.save(new LineRequest("1호선", "green", 1L, 2L, 5,0));

        sectionService.enroll(1L, new SectionRequest(2L, 3L, 2));
    }

    @Test
    @DisplayName("경로를 조회한다.")
    void findPath() {
        //given & when
        PathResponse path = pathService.findPath(1L, 3L, 20);

        //then
        assertThat(path.getStations()).hasSize(3);
        assertThat(path.getDistance()).isEqualTo(7);
        assertThat(path.getFare()).isEqualTo(1250);
    }
}
