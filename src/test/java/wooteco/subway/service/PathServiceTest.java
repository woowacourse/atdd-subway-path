package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.path.Path;

class PathServiceTest extends ServiceMockTest {

    @InjectMocks
    private PathService pathService;

    private Station 상계역;
    private Station 중계역;
    private Station 하계역;
    private List<Station> stations;

    private Line 일호선;
    private Line 이호선;
    private Line 삼호선;

    private Section 상계_중계;
    private Section 중계_하계;
    private Section 상계_하계;
    private List<Section> sections;

    @Test
    @DisplayName("경로 조회한다")
    void getPath() {
        // given
        providePathFixture();
        when(sectionRepository.findAll()).thenReturn(sections);
        when(stationRepository.findById(1L)).thenReturn(상계역);
        when(stationRepository.findById(3L)).thenReturn(하계역);
        when(pathGenerator.findPath(sections, 상계역, 하계역))
                .thenReturn(new Path(stations, 200, 10));

        // when
        Path path = pathService.getPath(1L, 3L);

        // then
        assertThat(path.getStations()).hasSize(3);
        assertThat(path.getDistance()).isEqualTo(10);
    }

    private void providePathFixture() {
        상계역 = new Station(1L, "상계역");
        중계역 = new Station(2L, "중계역");
        하계역 = new Station(3L, "하계역");
        일호선 = new Line(1L, "1호선", "red", 100L);
        이호선 = new Line(2L, "2호선", "blue", 200L);
        삼호선 = new Line(3L, "3호선", "green", 300L);

        상계_중계 = new Section(1L, 일호선, 상계역, 중계역, 5);
        중계_하계 = new Section(2L, 이호선, 중계역, 하계역, 5);
        상계_하계 = new Section(3L, 삼호선, 상계역, 하계역, 100);

        sections = List.of(상계_중계, 중계_하계, 상계_하계);
        stations = List.of(상계역, 중계역, 하계역);
    }

    @Test
    @DisplayName("경로 요금을 조회한다.")
    void getFare() {
        // given
        providePathFixture();
        when(sectionRepository.findAll()).thenReturn(sections);
        when(stationRepository.findById(1L)).thenReturn(상계역);
        when(stationRepository.findById(3L)).thenReturn(하계역);
        when(pathGenerator.findPath(sections, 상계역, 하계역))
                .thenReturn(new Path(List.of(상계역, 중계역, 하계역), 200, 10));

        // when
        Fare fare = pathService.getFare(1L, 3L, 20);

        // then
        assertThat(fare.getValue()).isEqualTo(1450L);
    }
}
