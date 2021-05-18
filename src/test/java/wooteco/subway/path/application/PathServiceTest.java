package wooteco.subway.path.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;
    @InjectMocks
    private PathService pathService;

    @Test
    @DisplayName("최단 경로와 거리 확인")
    public void shortestPathAndWeight() {
        Station station1 = new Station(1L, "가양");
        Station station2 = new Station(2L, "증미");
        Station station3 = new Station(3L, "등촌");
        Station station4 = new Station(4L, "염창");

        List<Section> sectionList = Arrays.asList(
                new Section(1L, station1, station2, 10),
                new Section(2L, station1, station3, 8),
                new Section(3L, station2, station3, 6),
                new Section(4L, station1, station4, 2),
                new Section(5L, station2, station4, 8),
                new Section(6L, station3, station4, 3)
        );

        when(stationDao.findById(3L)).thenReturn(station3);
        when(stationDao.findById(1L)).thenReturn(station1);
        when(sectionDao.findAll()).thenReturn(sectionList);

        PathResponse pathResponse = pathService.shortestDistancePath(3L, 1L);

        assertThat(pathResponse)
                .usingRecursiveComparison()
                .isEqualTo(new PathResponse(
                        Arrays.asList(
                                new StationResponse(3L, "등촌"),
                                new StationResponse(4L, "염창"),
                                new StationResponse(1L, "가양")
                        ), 5
                ));
    }
}