package wooteco.subway.path.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.station.application.StationService;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    private PathService pathService;

    @Mock
    private StationService stationService;

    @Mock
    private PathRouter pathRouter;

    @DisplayName("출발역과 도착역이 같으면 예외가 발생한다.")
    @Test
    void findShortestPath_fail_sameStations() {
        assertThatThrownBy(() -> pathService.findShortestPath(1L, 1L))
            .isInstanceOf(ValidationFailureException.class)
            .hasMessageContaining("출발역과 도착역이 같습니다");

        then(stationService).should(never()).findExistentStationById(anyLong());
        then(pathRouter).should(never()).findByShortest(any(), any());
    }
}
