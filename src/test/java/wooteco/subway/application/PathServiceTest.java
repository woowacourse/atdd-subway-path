package wooteco.subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.application.exception.NotFoundStationException;
import wooteco.subway.domain.exception.UnreachablePathException;
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

    private List<Station> stations;
    private Station 강남역;
    private Station 역삼역;
    private Station 잠실역;
    private Station 선릉역;
    private Station 부산역;
    private Station 서면역;

    @BeforeEach
    void setUp() {
        강남역 = stationService.save(new StationRequest("강남역"));
        역삼역 = stationService.save(new StationRequest("역삼역"));
        잠실역 = stationService.save(new StationRequest("잠실역"));
        선릉역 = stationService.save(new StationRequest("선릉역"));
        부산역 = stationService.save(new StationRequest("부산역"));
        서면역 = stationService.save(new StationRequest("서면역"));

        stations = List.of(강남역, 역삼역, 잠실역, 선릉역, 부산역, 서면역);

        Line 신분당선 = lineService.save(
            new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 역삼역.getId(), 5, 0));
        sectionService.addSection(신분당선.getId(), new AddSectionRequest(역삼역.getId(), 잠실역.getId(), 4));

        lineService.save(
            new LineRequest("분당선", "bg-green-600", 역삼역.getId(), 선릉역.getId(), 3, 500));

        lineService.save(
            new LineRequest("1호선", "bg-yellow-600", 부산역.getId(), 서면역.getId(), 6, 0));
    }

    @DisplayName("source와 target이 같은 경우 예외 발생")
    @Test
    void throwExceptionWhenSourceSameAsTarget() {
        assertThatThrownBy(() -> pathService.searchPath(강남역.getId(), 강남역.getId(), 21))
            .isInstanceOf(UnreachablePathException.class);
    }

    @DisplayName("source에 존재하지 않는 역인 경우에 예외 발생")
    @Test
    void throwExceptionWhenSourceIsNotFoundStation() {
        assertThatThrownBy(() -> pathService.searchPath(notFoundStationId(), 2L, 21))
            .isInstanceOf(NotFoundStationException.class);
    }

    private long notFoundStationId() {
        return stations.stream()
            .map(Station::getId)
            .max(Long::compareTo).get() + 1L;
    }

    @DisplayName("target에 존재하지 않는 역인 경우에 예외 발생")
    @Test
    void throwExceptionWhenTargetIsNotFoundStation() {
        assertThatThrownBy(() -> pathService.searchPath(강남역.getId(), notFoundStationId(), 21))
            .isInstanceOf(NotFoundStationException.class);
    }

    @DisplayName("한 같옆에 있는 지하철역 경로 찾기")
    @Test
    void searchAdjacentPath() {
        PathResponse pathResponse = pathService.searchPath(강남역.getId(), 역삼역.getId(), 21);

        assertThat(pathResponse.getStations()).containsExactly(
            new StationResponse(강남역), new StationResponse(역삼역));
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getFare()).isEqualTo(1250);
    }


    @DisplayName("두 칸옆에 있는 지하철역 경로 찾기")
    @Test
    void searchTwoBlockPath() {
        PathResponse pathResponse = pathService.searchPath(강남역.getId(), 잠실역.getId(), 21);

        assertThat(pathResponse.getStations()).containsExactly(
            new StationResponse(강남역), new StationResponse(역삼역), new StationResponse(잠실역));
        assertThat(pathResponse.getDistance()).isEqualTo(9);
        assertThat(pathResponse.getFare()).isEqualTo(1250);
    }

    @DisplayName("환승 구간이 있는 지하철역 경로 찾기")
    @Test
    void searchTransferLinePath() {
        PathResponse pathResponse = pathService.searchPath(강남역.getId(), 선릉역.getId(), 21);

        assertThat(pathResponse.getStations()).containsExactly(
            new StationResponse(강남역), new StationResponse(역삼역), new StationResponse(선릉역));
        assertThat(pathResponse.getDistance()).isEqualTo(8);
        assertThat(pathResponse.getFare()).isEqualTo(1750);
    }

    @DisplayName("노선에 추가 요금이 있는 경우")
    @Test
    void searchHasExtraFareLine() {
        PathResponse pathResponse = pathService.searchPath(역삼역.getId(), 선릉역.getId(), 21);

        assertThat(pathResponse.getStations())
            .containsExactly(new StationResponse(역삼역), new StationResponse(선릉역));
        assertThat(pathResponse.getDistance()).isEqualTo(3);
        assertThat(pathResponse.getFare()).isEqualTo(1750);
    }

    @DisplayName("청소년이 지하철 경로 조회")
    @Test
    void searchPathByYouth() {
        PathResponse pathResponse = pathService.searchPath(역삼역.getId(), 선릉역.getId(), 15);

        assertThat(pathResponse.getStations())
            .containsExactly(new StationResponse(역삼역), new StationResponse(선릉역));
        assertThat(pathResponse.getDistance()).isEqualTo(3);
        assertThat(pathResponse.getFare()).isEqualTo(1120);
    }

    @DisplayName("어린이가 지하철 경로 조회")
    @Test
    void searchPathByChild() {
        PathResponse pathResponse = pathService.searchPath(역삼역.getId(), 선릉역.getId(), 6);

        assertThat(pathResponse.getStations())
            .containsExactly(new StationResponse(역삼역), new StationResponse(선릉역));
        assertThat(pathResponse.getDistance()).isEqualTo(3);
        assertThat(pathResponse.getFare()).isEqualTo(700);
    }

    @Test
    void searchUnreachablePath() {
        assertThatThrownBy(() -> pathService.searchPath(강남역.getId(), 서면역.getId(), 21))
            .isInstanceOf(UnreachablePathException.class);
    }
}