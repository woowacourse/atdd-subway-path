package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.LocalTime;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Path;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.repository.LineRepository;

@ExtendWith(MockitoExtension.class)
public class GraphServiceTest {
    @Mock
    private LineRepository lineRepository;

    private GraphService graphService;

    @BeforeEach
    void setUp() {
        graphService = new SubwayGraphService(lineRepository);

        Line line2 = new Line(1L, "8호선", "bg-pink-500", LocalTime.of(5, 30), LocalTime.of(22, 30),
                5);
        line2.addLineStation(new LineStation(null, 1L, 0, 0));
        line2.addLineStation(new LineStation(1L, 2L, 10, 5));
        line2.addLineStation(new LineStation(2L, 3L, 10, 3));
        line2.addLineStation(new LineStation(3L, 4L, 10, 7));
        line2.addLineStation(new LineStation(4L, 5L, 10, 2));
        line2.addLineStation(new LineStation(5L, 6L, 10, 3));
        line2.addLineStation(new LineStation(6L, 7L, 10, 8));

        Line bundang = new Line(2L, "분당선", "bg-yellow-500", LocalTime.of(5, 30),
                LocalTime.of(22, 30),
                5);
        bundang.addLineStation(new LineStation(null, 1L, 0, 0));
        bundang.addLineStation(new LineStation(1L, 8L, 10, 20));
        bundang.addLineStation(new LineStation(8L, 9L, 10, 10));
        bundang.addLineStation(new LineStation(9L, 7L, 10, 30));
        bundang.addLineStation(new LineStation(7L, 10L, 10, 10));

        given(lineRepository.findAll()).willReturn(Lists.newArrayList(line2, bundang));
    }

    @Test
    @DisplayName("최단 거리")
    void findDistancePath() {
        Station moran = new Station(1L, "모란역");
        Station suseo = new Station(10L, "수서역");

        Path path = graphService.findPath(moran, suseo, PathType.DISTANCE).get();

        assertThat(path.vertices()).startsWith(1L, 8L, 9L, 7L, 10L);
        assertThat(path.distance()).isEqualTo(40);
        assertThat(path.duration()).isEqualTo(70);
    }

    @Test
    @DisplayName("최소 시간")
    void findDurationPath() {
        Station moran = new Station(1L, "모란역");
        Station suseo = new Station(10L, "수서역");

        Path path = graphService.findPath(moran, suseo, PathType.DURATION).get();

        assertThat(path.vertices()).startsWith(1L, 2L, 3L, 4L, 5L, 6L, 7L, 10L);
        assertThat(path.distance()).isEqualTo(70);
        assertThat(path.duration()).isEqualTo(38);
    }

    @Test
    @DisplayName("경로를 찾을 수 없는 경우")
    void NotFoundPath() {
        Station moran = new Station(1L, "모란역");
        Station suseo = new Station(11L, "반월당역");

        Optional<Path> path = graphService.findPath(moran, suseo, PathType.DURATION);

        assertThat(path.isPresent()).isFalse();
    }
}
