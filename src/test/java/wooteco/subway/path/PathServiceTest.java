package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.dto.SectionDto;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    private PathService pathService;

    @Mock
    private StationService stationService;

    @Mock
    private SectionDao sectionDao;

    @DisplayName("출발역과 도착역을 통해 경로를 찾는다.")
    @Test
    void findPath() {
        Long sourceId = 1L;
        Long targetId = 2L;
        List<SectionDto> sections = Arrays
            .asList(new SectionDto(1L, 1L, 3L, 3)
                , new SectionDto(2L, 3L, 2L, 5)
                , new SectionDto(3L, 4L, 1L, 2));
        Station 강남역 = new Station(sourceId, "강남역");
        Station 역삼역 = new Station(targetId, "역삼역");
        Station 도곡역 = new Station(3L, "도곡역");
        Station 개포역 = new Station(4L, "개포역");

        when(stationService.findStationById(sourceId))
            .thenReturn(강남역);
        when(stationService.findStationById(targetId))
            .thenReturn(역삼역);
        when(sectionDao.findAll())
            .thenReturn(sections);
        when(stationService.findStationsByIds(Arrays.asList(1L,3L)))
            .thenReturn(Arrays.asList(강남역, 도곡역));
        when(stationService.findStationsByIds(Arrays.asList(3L,2L)))
            .thenReturn(Arrays.asList(도곡역, 역삼역));
        when(stationService.findStationsByIds(Arrays.asList(4L,1L)))
            .thenReturn(Arrays.asList(개포역, 강남역));

        List<StationResponse> expectedStations = Arrays
            .asList(new StationResponse(1L, "강남역")
                , new StationResponse(3L, "도곡역")
                , new StationResponse(2L, "역삼역"));
        PathResponse expected = new PathResponse(expectedStations, 8);
        assertThat(pathService.findPath(sourceId, targetId).getDistance())
            .isEqualTo(expected.getDistance());
    }
}
