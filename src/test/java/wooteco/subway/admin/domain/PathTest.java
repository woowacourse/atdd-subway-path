package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {
    private static final String STATION_NAME_KANGNAM = "강남역";
    private static final String STATION_NAME_YEOKSAM = "역삼역";
    private static final String STATION_NAME_SEOLLEUNG = "선릉역";

    private static final List<Line> lines = new ArrayList<>();
    private static final Map<Long, Station> stations = new HashMap<>();

    @BeforeEach
    void getGraphPath() {
        Line line2 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-red-300");
        Station kangNamStation = new Station(1L, STATION_NAME_KANGNAM);
        Station yeokSamStation = new Station(2L, STATION_NAME_YEOKSAM);
        Station seolLeungStation = new Station(3L, STATION_NAME_SEOLLEUNG);

        line2.addLineStation(new LineStation(null, 1L, 0, 0));
        line2.addLineStation(new LineStation(1L, 2L, 10, 10));
        line2.addLineStation(new LineStation(2L, 3L, 10, 10));

        lines.add(line2);

        stations.put(kangNamStation.getId(), kangNamStation);
        stations.put(yeokSamStation.getId(), yeokSamStation);
        stations.put(seolLeungStation.getId(), seolLeungStation);
    }

    @DisplayName("최단 거리 테스트")
    @Test
    void getDistanceByWeight() {
        Path path = new Path(1L, 3L, PathType.DISTANCE, stations, lines);
        assertThat(path.getDistanceByWeight()).isEqualTo(20);
    }

    @DisplayName("최단 시간 테스트")
    @Test
    void getDurationByWeight() {
        Path path = new Path(1L, 3L, PathType.DURATION, stations, lines);
        assertThat(path.getDistanceByWeight()).isEqualTo(20);
    }
}