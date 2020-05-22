package wooteco.subway.admin.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.exception.InvalidFindPathException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayGraphTest {
    private static Line line2;
    private static Line line7;
    private static Line lineB;

    private static Station 왕십리;
    private static Station 한양대;
    private static Station 뚝섬;
    private static Station 성수;
    private static Station 건대입구;
    private static Station 뚝섬유원지;
    private static Station 청담;
    private static Station 강남구청;
    private static Station 압구정로데오;
    private static Station 서울숲;
    private static Station 잠실;

    private static Lines lines;
    private static Stations stations;
    private static SubwayGraph subwayGraph;

    @BeforeAll
    static void setUp() {
        line2 = new Line("2호선", null, null, 0);
        line7 = new Line("7호선", null, null, 0);
        lineB = new Line("분당선", null, null, 0);

        왕십리 = new Station(1L, "왕십리");
        한양대 = new Station(2L, "한양대");
        뚝섬 = new Station(3L, "뚝섬");
        성수 = new Station(4L, "성수");
        건대입구 = new Station(5L, "건대입구");
        뚝섬유원지 = new Station(6L, "뚝섬유원지");
        청담 = new Station(7L, "청담");
        강남구청 = new Station(8L, "강남구청");
        압구정로데오 = new Station(9L, "압구정로데오");
        서울숲 = new Station(10L, "서울숲");
        잠실 = new Station(11L, "잠실");

        line2.addLineStation(new LineStation(null, 왕십리.getId(), 0, 0));
        line2.addLineStation(new LineStation(왕십리.getId(), 한양대.getId(), 5, 2));
        line2.addLineStation(new LineStation(한양대.getId(), 뚝섬.getId(), 5, 2));
        line2.addLineStation(new LineStation(뚝섬.getId(), 성수.getId(), 5, 2));
        line2.addLineStation(new LineStation(성수.getId(), 건대입구.getId(), 5, 2));

        line7.addLineStation(new LineStation(null, 건대입구.getId(), 0, 0));
        line7.addLineStation(new LineStation(건대입구.getId(), 뚝섬유원지.getId(), 7, 4));
        line7.addLineStation(new LineStation(뚝섬유원지.getId(), 청담.getId(), 7, 4));
        line7.addLineStation(new LineStation(청담.getId(), 강남구청.getId(), 7, 4));

        lineB.addLineStation(new LineStation(null, 강남구청.getId(), 0, 0));
        lineB.addLineStation(new LineStation(강남구청.getId(), 압구정로데오.getId(), 3, 1));
        lineB.addLineStation(new LineStation(압구정로데오.getId(), 서울숲.getId(), 3, 1));
        lineB.addLineStation(new LineStation(서울숲.getId(), 왕십리.getId(), 3, 1));

        lines = new Lines(Arrays.asList(line2, line7, lineB));
        stations = new Stations(Arrays.asList(왕십리, 한양대, 뚝섬, 성수, 건대입구, 뚝섬유원지, 청담,
                강남구청, 압구정로데오, 서울숲, 잠실));

        subwayGraph = SubwayGraph.makeGraph(PathType.DISTANCE, stations, lines.makeLineStation());
    }

    @Test
    void findShortestPath() {
        assertThat(subwayGraph.findShortestPath(강남구청.getId(), 왕십리.getId()).size()).isEqualTo(4);
    }

    @Test
    void findNoExistStationPath() {
        assertThatThrownBy(() -> subwayGraph.findShortestPath(강남구청.getId(), 잠실.getId()))
                .isInstanceOf(InvalidFindPathException.class)
                .hasMessage(InvalidFindPathException.NO_PATH_ERROR_MSG);
    }

    @Test
    void getPathWeight() {
        assertThat(subwayGraph.getPathWeight(강남구청.getId(), 왕십리.getId())).isEqualTo(9);
    }
}