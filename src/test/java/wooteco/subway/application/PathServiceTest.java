package wooteco.subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.application.exception.NotFoundStationException;
import wooteco.subway.application.exception.RidiculousAgeException;
import wooteco.subway.application.exception.UnreachablePathException;
import wooteco.subway.application.path.PathService;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.AddSectionRequest;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

@SpringBootTest
@Transactional
class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Autowired
    private StationService stationService;

    @Autowired
    private LineService lineService;

    @Autowired
    private SectionService sectionService;

    private Station 강남역;
    private Station 역삼역;
    private Station 잠실역;
    private Station 선릉역;
    private Station 부산역;
    private Station 서면역;
    private Line 신분당선;
    private Line 분당선;
    private Line 일호선;

    @BeforeEach
    void setUp() {
        강남역 = stationService.save(new StationRequest("강남역"));
        역삼역 = stationService.save(new StationRequest("역삼역"));
        잠실역 = stationService.save(new StationRequest("잠실역"));
        선릉역 = stationService.save(new StationRequest("선릉역"));
        부산역 = stationService.save(new StationRequest("부산역"));
        서면역 = stationService.save(new StationRequest("서면역"));

        신분당선 = lineService.save(
                new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 역삼역.getId(), 5, 1000));
        sectionService.addSection(신분당선.getId(), new AddSectionRequest(역삼역.getId(), 잠실역.getId(), 4));

        분당선 = lineService.save(
                new LineRequest("분당선", "bg-green-600", 역삼역.getId(), 선릉역.getId(), 3, 900));

        일호선 = lineService.save(
                new LineRequest("1호선", "bg-yellow-600", 부산역.getId(), 서면역.getId(), 6, 0));
    }

    @DisplayName("source와 target이 같은 경우 예외 발생")
    @Test
    void throwExceptionWhenSourceSameAsTarget() {
        assertThatThrownBy(() -> pathService.searchPath(강남역.getId(), 강남역.getId(), 20))
                .isInstanceOf(UnreachablePathException.class);
    }

    @DisplayName("source에 존재하지 않는 역인 경우에 예외 발생")
    @Test
    void throwExceptionWhenSourceIsNotFoundStation() {
        assertThatThrownBy(() -> pathService.searchPath(notFoundStationId(), 2L, 20))
                .isInstanceOf(NotFoundStationException.class);
    }

    private long notFoundStationId() {
        return Stream.of(강남역, 역삼역, 잠실역, 선릉역, 부산역, 서면역)
                .map(Station::getId)
                .max(Long::compareTo).get() + 1L;
    }

    @DisplayName("target에 존재하지 않는 역인 경우에 예외 발생")
    @Test
    void throwExceptionWhenTargetIsNotFoundStation() {
        assertThatThrownBy(() -> pathService.searchPath(강남역.getId(), notFoundStationId(), 20))
                .isInstanceOf(NotFoundStationException.class);
    }

    @DisplayName("한 같옆에 있는 지하철역 경로 찾기")
    @Test
    void searchAdjacentPath() {
        PathResponse pathResponse = pathService.searchPath(부산역.getId(), 서면역.getId(), 20);

        assertThat(pathResponse.getStations()).containsExactly(
                new StationResponse(부산역), new StationResponse(서면역));
        assertThat(pathResponse.getDistance()).isEqualTo(6);
        assertThat(pathResponse.getFare()).isEqualTo(1250);
    }

    @DisplayName("두 칸옆에 있는 지하철역 경로 찾기")
    @Test
    void searchTwoBlockPath() {
        PathResponse pathResponse = pathService.searchPath(부산역.getId(), 서면역.getId(), 20);

        assertThat(pathResponse.getStations()).containsExactly(
                new StationResponse(부산역), new StationResponse(서면역));
        assertThat(pathResponse.getDistance()).isEqualTo(6);
        assertThat(pathResponse.getFare()).isEqualTo(1250);
    }

    @DisplayName("환승 구간이 있는 지하철역 경로 찾기")
    @Test
    void searchTransferLinePath() {
        PathResponse pathResponse = pathService.searchPath(강남역.getId(), 선릉역.getId(), 20);

        assertThat(pathResponse.getStations()).containsExactly(
                new StationResponse(강남역), new StationResponse(역삼역), new StationResponse(선릉역));
        assertThat(pathResponse.getDistance()).isEqualTo(8);
        assertThat(pathResponse.getFare()).isEqualTo(1250 + 1000);
    }

    @Test
    void searchUnreachablePath() {
        assertThatThrownBy(() -> pathService.searchPath(강남역.getId(), 서면역.getId(), 20))
                .isInstanceOf(UnreachablePathException.class);
    }

    @DisplayName("나이가 음수이거나 150초과일 경우 예외 발생")
    @ParameterizedTest
    @CsvSource({"151", "1000", "0", "-1", " -1000"})
    void searchPathWithRidiculousException(int age) {
        assertThatThrownBy(() -> pathService.searchPath(강남역.getId(), 잠실역.getId(), age))
                .isInstanceOf(RidiculousAgeException.class);
    }

    @DisplayName("노선에 추가 요금이 있는 경우 경로 찾기")
    @Test
    void searchPathWithExtraFare() {
        PathResponse pathResponse = pathService.searchPath(강남역.getId(), 잠실역.getId(), 20);

        assertThat(pathResponse.getStations()).containsExactly(
                new StationResponse(강남역), new StationResponse(역삼역), new StationResponse(잠실역));
        assertThat(pathResponse.getDistance()).isEqualTo(9);
        assertThat(pathResponse.getFare()).isEqualTo(1250 + 1000);
    }

    @DisplayName("환승했을 경우 노선에 추가 요금이 있는 경우 경로 찾기")
    @Test
    void searchPathWithTransferAndExtraFare() {
        PathResponse pathResponse = pathService.searchPath(강남역.getId(), 선릉역.getId(), 20);

        assertThat(pathResponse.getStations()).containsExactly(
                new StationResponse(강남역), new StationResponse(역삼역), new StationResponse(선릉역));
        assertThat(pathResponse.getDistance()).isEqualTo(8);
        assertThat(pathResponse.getFare()).isEqualTo(1250 + 1000);
    }
}
