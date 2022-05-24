package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

@SpringBootTest
@Sql(scripts = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PathServiceTest {

    @Autowired
    private StationService stationService;

    @Autowired
    private LineService lineService;

    @Autowired
    private PathService pathService;

    @Test
    @DisplayName("최단 경로를 찾는다.")
    void findShortestPath() {
        StationResponse firstStation = stationService.save(new StationRequest("역삼역"));
        StationResponse secondStation = stationService.save(new StationRequest("삼성역"));
        LineRequest line = new LineRequest("9호선", "red", firstStation.getId(), secondStation.getId(), 10, 100);
        lineService.save(line);

        PathResponse shortestPath = pathService.findShortestPath(firstStation.getId(), secondStation.getId(), 3L);

        assertThat(shortestPath.getDistance()).isEqualTo(10);
    }
}
