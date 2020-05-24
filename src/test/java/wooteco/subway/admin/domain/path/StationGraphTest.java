package wooteco.subway.admin.domain.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.LineStations;
import wooteco.subway.admin.domain.PathSearchType;
import wooteco.subway.admin.exception.CanNotCreateGraphException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StationGraphTest {

    @DisplayName("LineStation의 StationId에 null을 가진 경우 예외가 발생한다.")
    @Test
    void of() {
        // given
        LineStation lineStation1 = new LineStation(null, 1L, 0, 0);
        LineStation lineStation2 = new LineStation(1L, 2L, 10, 10);
        LineStation lineStation3 = new LineStation(2l, null, 10, 10);
        LineStation lineStation4 = new LineStation(3L, 4L, 0, 0);

        List<LineStation> lineStations = Arrays.asList(lineStation1, lineStation2, lineStation3, lineStation4);
        LineStations lineStationsExistNullStationId = new LineStations(lineStations);
        // when, then
        assertThatThrownBy(() -> StationGraph.of(lineStationsExistNullStationId, PathSearchType.DISTANCE))
                .isInstanceOf(CanNotCreateGraphException.class);

    }
}