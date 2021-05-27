package wooteco.subway.path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.*;
import wooteco.subway.service.LineService;
import wooteco.subway.service.PathService;
import wooteco.subway.service.StationService;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class PathServiceTest {
    @Autowired
    private PathService pathService;
    @Autowired
    private StationService stationService;
    @Autowired
    private LineService lineService;

    private Station 고속터미널역;
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;

    @BeforeAll
    void setUp() {
        /**
         * 고속터미널역
         * |
         * 5 *3호선*
         * |          *2호선*
         * 교대역     ---  5   ---   강남역
         * |                          |
         * 10 *3호선*                 10  *신분당선*
         * |                          |
         * 남부터미널역  --- 15 ---    양재
         *             *3호선*
         */

        고속터미널역 = 지하철역_등록("고속터미널역");
        교대역 = 지하철역_등록("교대역");
        강남역 = 지하철역_등록("강남역");
        양재역 = 지하철역_등록("양재역");
        남부터미널역 = 지하철역_등록("남부터미널역");

        // 2호선
        이호선 = 노선_등록("2호선", "bg-green-600", 교대역, 강남역, 5);

        // 3호선
        삼호선 = 노선_등록("3호선", "bg-orange-600", 고속터미널역, 교대역, 5);
        삼호선 = 구간_추가(삼호선, 교대역, 남부터미널역, 10);
        삼호선 = 구간_추가(삼호선, 남부터미널역, 양재역, 15);

        // 신분당선
        신분당선 = 노선_등록("신분당선", "bg-red-600", 강남역, 양재역, 10);
    }

    @AfterAll
    void flush() {
        노선_삭제(이호선);
        노선_삭제(삼호선);
        노선_삭제(신분당선);

        역_삭제(고속터미널역);
        역_삭제(교대역);
        역_삭제(강남역);
        역_삭제(양재역);
        역_삭제(남부터미널역);
    }

    Stream<Arguments> findShortestPathTest() {
        return Stream.of(
                Arguments.of(
                        고속터미널역, 양재역,
                        new PathResponse(StationResponse.listOf(Arrays.asList(고속터미널역, 교대역, 강남역, 양재역)), 20),
                        "고속터미널 -> 양재역의 경로를 찾는다."
                ),
                Arguments.of(
                        강남역, 남부터미널역,
                        new PathResponse(StationResponse.listOf(Arrays.asList(강남역, 교대역, 남부터미널역)), 15),
                        "강남역 -> 남부터미널역의 경로를 찾는다."
                ),
                Arguments.of(
                        양재역, 교대역,
                        new PathResponse(StationResponse.listOf(Arrays.asList(양재역, 강남역, 교대역)), 15),
                        "양재역 -> 교대역의 경로를 찾는다."
                )

        );
    }

    @DisplayName("출발, 도착역의 id를 받아 최단 경로의 PathResponse 객체를 반환한다.")
    @ParameterizedTest(name="{3}")
    @MethodSource
    void findShortestPathTest(Station source, Station target, PathResponse expected, String testCaseName) {
        PathResponse shortestPath = pathService.findShortestPath(source.getId(), target.getId());

        assertThat(shortestPath.getStations()).isEqualTo(expected.getStations());
        assertThat(shortestPath.getDistance()).isEqualTo(expected.getDistance());
    }

    private Station 지하철역_등록(String name) {
        StationRequest stationRequest = new StationRequest(name);
        StationResponse stationResponse = stationService.saveStation(stationRequest);
        return stationService.findStationById(stationResponse.getId());
    }

    private Line 노선_등록(String name, String color, Station upEndStation, Station downEndStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upEndStation.getId(), downEndStation.getId(), distance);
        LineResponse lineResponse = lineService.saveLine(lineRequest);
        return lineService.findLineById(lineResponse.getId());
    }

    private Line 구간_추가(Line line, Station upStation, Station downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);
        lineService.addLineStation(line.getId(), sectionRequest);
        return lineService.findLineById(line.getId());
    }

    private void 노선_삭제(Line line) {
        lineService.deleteLineById(line.getId());
    }

    private void 역_삭제(Station station) {
        stationService.deleteStationById(station.getId());
    }


}
