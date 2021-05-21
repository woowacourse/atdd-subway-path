package wooteco.subway.path.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.section.dao.SectionDao;
import wooteco.subway.section.domain.Section;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.domain.Stations;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.service.StationService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    private PathService pathService;

    @Mock
    private SectionDao sectionDao;

    @Mock
    private StationService stationService;

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void findShortestPath() {
        Station firstStation = new Station(1L, "잠원역");
        Station secondStation = new Station(2L, "강남역");
        Station thirdStation = new Station(3L, "역삼역");
        Station fourthStation = new Station(4L, "회기역");
        Stations stations = new Stations(Arrays.asList(firstStation, secondStation, thirdStation, fourthStation));

        Section firstSection = new Section(firstStation, secondStation, 5);
        Section secondSection = new Section(secondStation, fourthStation, 31);
        Section thirdSection = new Section(firstStation, thirdStation, 4);
        Section fourthSection = new Section(thirdStation, fourthStation, 3);

        when(sectionDao.findAll()).thenReturn(Arrays.asList(firstSection, secondSection, thirdSection, fourthSection));
        when(stationService.findAllStations()).thenReturn(stations);

        PathResponse shortestPath = pathService.findShortestPath(1L, 4L);
        List<String> names = shortestPath.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertThat(shortestPath.getDistance()).isEqualTo(7);
        assertThat(names).containsExactly("잠원역", "역삼역", "회기역");
        verify(sectionDao, times(1)).findAll();
        verify(stationService, times(1)).findAllStations();
    }
}
