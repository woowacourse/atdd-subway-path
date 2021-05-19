package wooteco.subway.path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.apllication.PathService;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class PathServiceTest {

    @InjectMocks
    private PathService pathService;
    @Mock
    private StationService stationService;
    @Mock
    private LineService lineService;

    private final Long 강남역_ID = 1L;
    private final Long 양재역_ID = 2L;
    private final Long 교대역_ID = 3L;
    private final Long 남부터미널역_ID = 4L;

    @Test
    void testFindShortestPath() {
        // given
        Station 강남역 = new Station(강남역_ID, "강남역");
        Station 양재역 = new Station(양재역_ID, "양재역");
        Station 교대역 = new Station(교대역_ID, "교대역");
        Station 남부터미널역 = new Station(남부터미널역_ID, "남부터미널역");

        Line 신분당선 = new Line(1L,"신분당선", "빨강");
        Line 이호선 = new Line(2L, "이호선", "초록");
        Line 삼호선 = new Line(3L, "삼호선", "주황");

        Section 강남에서양재 = new Section(강남역, 양재역, 10);
        Section 교대에서강남 = new Section(교대역, 강남역, 10);
        Section 교대에서양재 = new Section(교대역, 양재역, 5);

        Section 교대에서남부터미널 = new Section(교대역, 남부터미널역, 3);

        신분당선.addSection(강남에서양재);
        이호선.addSection(교대에서강남);
        삼호선.addSection(교대에서양재);
        삼호선.addSection(교대에서남부터미널);

        given(stationService.findStationById(강남역_ID)).willReturn(강남역);
        given(stationService.findStationById(양재역_ID)).willReturn(양재역);
        given(stationService.findStationById(교대역_ID)).willReturn(교대역);
        given(stationService.findStationById(남부터미널역_ID)).willReturn(남부터미널역);
        given(lineService.findLines()).willReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        // then
        PathResponse pathResponse = pathService.findShortestPath(교대역_ID, 양재역_ID);
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getStations())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(Arrays.asList(교대역, 남부터미널역, 양재역));
    }
}
