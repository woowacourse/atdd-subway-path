package wooteco.subway.path.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @Mock
    private LineDao mockLineDao;
    @Mock
    private StationDao mockStationDao;

    @InjectMocks
    private PathService pathService;

    @Test
    @DisplayName("최적의 경로 반환")
    void findPath() {
        //when
        Station oneStation = new Station(1L, "v1");
        Station twoStation = new Station(2L, "v2");
        Station threeStation = new Station(3L, "v3");
        Section oneSection = new Section(1L, oneStation, twoStation, 2);
        Section twoSection = new Section(2L, twoStation, threeStation, 2);
        Sections newSections = new Sections(Arrays.asList(oneSection, twoSection));
        Line line = new Line(1L, "1호선", "파란색", newSections);
        Path path = new Path(Arrays.asList(line));
        when(mockStationDao.findById(1L)).thenReturn(oneStation);
        when(mockStationDao.findById(3L)).thenReturn(threeStation);
        when(mockLineDao.findAll()).thenReturn(Arrays.asList(line));
        PathRequest pathRequest = new PathRequest(1L, 3L);

        //given
        PathResponse pathResponse = pathService.optimalPath(pathRequest);

        //then
        assertThat(pathResponse.getDistance()).isEqualTo(4);
        assertThat(pathResponse.getStations().size()).isEqualTo(3);
    }
}
