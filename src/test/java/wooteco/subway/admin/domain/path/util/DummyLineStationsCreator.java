package wooteco.subway.admin.domain.path.util;

import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.LineStations;

import java.util.Arrays;
import java.util.List;

public class DummyLineStationsCreator {
    public static LineStations createLineStations() {
        LineStation lineStation1 = new LineStation(null, 1L, 0, 0);
        LineStation lineStation2 = new LineStation(1L, 2L, 10, 10);
        LineStation lineStation3 = new LineStation(2l, 3L, 10, 10);

        LineStation lineStation4 = new LineStation(null, 3L, 0, 0);
        LineStation lineStation5 = new LineStation(3L, 4L, 10, 10);
        LineStation lineStation6 = new LineStation(4L, 5L, 10, 10);
        LineStation lineStation7 = new LineStation(5L, 6L, 10, 10);
        LineStation lineStation8 = new LineStation(6L, 1L, 10, 10);

        List<LineStation> lineStations = Arrays.asList(lineStation1, lineStation2, lineStation3, lineStation4, lineStation5,
                lineStation6, lineStation7, lineStation8);
        return new LineStations(lineStations);
    }
}
