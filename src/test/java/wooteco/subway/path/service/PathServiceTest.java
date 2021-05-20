package wooteco.subway.path.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("지하철 경로 비즈니스 로직 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private StationService stationService;

    @Mock
    private LineService lineService;

    @InjectMocks
    private PathService pathService;

    @Test
    @DisplayName("최단 경로 찾기")
    void findShortestPath() {
        // given
        Station 왕십리역 = new Station(1L, "왕십리역");
        Station 천호역 = new Station(2L, "천호역");
        Station 잠실역 = new Station(3L, "잠실역");

        Line 이호선 = new Line(1L, "2호선", "green", new Sections(
                Collections.singletonList(new Section(1L, 왕십리역, 잠실역, 15))
        ));
        Line 오호선 = new Line(2L, "5호선", "purple", new Sections(
                Collections.singletonList(new Section(2L, 왕십리역, 천호역, 10))
        ));
        Line 팔호선 = new Line(3L, "8호선", "pink", new Sections(
                Collections.singletonList(new Section(3L, 천호역, 잠실역, 6))
        ));
        given(stationService.findStationById(1L)).willReturn(왕십리역);
        given(stationService.findStationById(3L)).willReturn(잠실역);
        given(lineService.findLines()).willReturn(
                Arrays.asList(이호선, 오호선, 팔호선)
        );

        // when
        PathResponse response = pathService.findShortestPath(1L, 3L);

        // then
        assertThat(response.getStations())
                .usingRecursiveFieldByFieldElementComparator()
                .containsAll(
                        Arrays.asList(
                                new StationResponse(1L, "왕십리역"),
                                new StationResponse(3L, "잠실역")
                        )
                );
        assertThat(response.getDistance()).isEqualTo(15);
    }
}