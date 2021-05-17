package wooteco.subway.path.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.path.domin.Path;
import wooteco.subway.path.domin.PathRepository;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PathServiceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    @Autowired
    private StationService stationService;

    @Autowired
    private LineService lineService;

    @Autowired
    private PathService pathService;

    @DisplayName("최단 거리 경로를 리턴한다")
    @Test
    public void testGenerateShortestDistancePath() {
        PathResponse pathResponse = pathService.getShortestDistancePath(교대역.getId(), 양재역.getId());
        assertThat(pathResponse.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
    }

    @BeforeEach
    public void setUp() {
        강남역 = stationService.saveStation(new StationRequest("강남역"));
        양재역 = stationService.saveStation(new StationRequest("양재역"));
        교대역 = stationService.saveStation(new StationRequest("교대역"));
        남부터미널역 = stationService.saveStation(new StationRequest("남부터미널역"));

        lineService.saveLine(new LineRequest("신분당선", "bg-red-400", 강남역.getId(), 양재역.getId(), 10));

        lineService.saveLine(new LineRequest("이호선", "bg-red-400", 교대역.getId(), 강남역.getId(), 10));

        LineResponse lineResponse = lineService.saveLine(new LineRequest("삼호선", "bg-red-400", 교대역.getId(), 양재역.getId(), 10));
        lineService.addLineStation(lineResponse.getId(), new SectionRequest(교대역.getId(), 남부터미널역.getId(), 3));
    }
}
