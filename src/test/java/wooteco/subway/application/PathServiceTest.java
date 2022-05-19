package wooteco.subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.application.exception.NotFoundStationException;
import wooteco.subway.application.exception.UnreachablePathException;
import wooteco.subway.application.path.PathService;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.path.PathSummary;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.AddSectionRequest;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.StationRequest;

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

    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Station station6;
    private Line line1;
    private Line line2;
    private Line line3;

    @BeforeEach
    void setUp() {
        station1 = stationService.save(new StationRequest("강남역"));
        station2 = stationService.save(new StationRequest("역삼역"));
        station3 = stationService.save(new StationRequest("잠실역"));
        station4 = stationService.save(new StationRequest("선릉역"));
        station5 = stationService.save(new StationRequest("부산역"));
        station6 = stationService.save(new StationRequest("서면역"));

        line1 = lineService.save(
            new LineRequest("신분당선", "bg-red-600", station1.getId(), station2.getId(), 5));
        sectionService.addSection(line1.getId(), new AddSectionRequest(station2.getId(), station3.getId(), 4));

        line2 = lineService.save(
            new LineRequest("분당선", "bg-green-600", station2.getId(), station4.getId(), 3));

        line3 = lineService.save(
            new LineRequest("1호선", "bg-yellow-600", station5.getId(), station6.getId(), 6));
    }

    @DisplayName("source와 target이 같은 경우 예외 발생")
    @Test
    void throwExceptionWhenSourceSameAsTarget() {
        assertThatThrownBy(() -> pathService.searchPath(station1.getId(), station1.getId()))
            .isInstanceOf(UnreachablePathException.class);
    }

    @DisplayName("source에 존재하지 않는 역인 경우에 예외 발생")
    @Test
    void throwExceptionWhenSourceIsNotFoundStation() {
        assertThatThrownBy(() -> pathService.searchPath(notFoundStationId(), 2L))
            .isInstanceOf(NotFoundStationException.class);
    }

    private long notFoundStationId() {
        return Stream.of(station1, station2, station3, station4, station5, station6)
            .map(Station::getId)
            .max(Long::compareTo).get() + 1L;
    }

    @DisplayName("target에 존재하지 않는 역인 경우에 예외 발생")
    @Test
    void throwExceptionWhenTargetIsNotFoundStation() {
        assertThatThrownBy(() -> pathService.searchPath(station1.getId(), notFoundStationId()))
            .isInstanceOf(NotFoundStationException.class);
    }

    @DisplayName("한 같옆에 있는 지하철역 경로 찾기")
    @Test
    void searchAdjacentPath() {
        PathSummary pathResponse = pathService.searchPath(station1.getId(), station2.getId());

        assertThat(pathResponse.getPath()).containsExactly(
            station1, station2);
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getFare()).isEqualTo(1250);
    }


    @DisplayName("두 칸옆에 있는 지하철역 경로 찾기")
    @Test
    void searchTwoBlockPath() {
        PathSummary pathResponse = pathService.searchPath(station1.getId(), station3.getId());

        assertThat(pathResponse.getPath()).containsExactly(
            station1, station2, station3);
        assertThat(pathResponse.getDistance()).isEqualTo(9);
        assertThat(pathResponse.getFare()).isEqualTo(1250);
    }

    @DisplayName("환승 구간이 있는 지하철역 경로 찾기")
    @Test
    void searchTransferLinePath() {
        PathSummary pathResponse = pathService.searchPath(station1.getId(), station4.getId());

        assertThat(pathResponse.getPath()).containsExactly(
            station1, station2, station4);
        assertThat(pathResponse.getDistance()).isEqualTo(8);
        assertThat(pathResponse.getFare()).isEqualTo(1250);
    }

    @Test
    void searchUnreachablePath() {
        assertThatThrownBy(() -> pathService.searchPath(station1.getId(), station6.getId()))
            .isInstanceOf(UnreachablePathException.class);
    }
}