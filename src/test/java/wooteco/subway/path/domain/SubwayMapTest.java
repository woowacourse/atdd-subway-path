package wooteco.subway.path.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayMapTest {
    private static final long 강남역_ID = 1L;
    private static final long 양재역_ID = 2L;
    private static final long 교대역_ID = 3L;
    private static final long 남부터미널역_ID = 4L;
    private SubwayMap subwayMap;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    void setUp() {
        강남역 = new Station(강남역_ID, "강남역");
        양재역 = new Station(양재역_ID, "양재역");
        교대역 = new Station(교대역_ID, "교대역");
        남부터미널역 = new Station(남부터미널역_ID, "남부터미널역");

        Line 신분당선 = new Line("신분당선", "빨강");
        Line 이호선 = new Line("이호선", "초록");
        Line 삼호선 = new Line("삼호선", "주황");

        Section 강남에서양재 = new Section(강남역, 양재역, 10);
        Section 교대에서강남 = new Section(교대역, 강남역, 10);
        Section 교대에서양재 = new Section(교대역, 양재역, 5);

        Section 교대에서남부터미널 = new Section(교대역, 남부터미널역, 3);

        신분당선.addSection(강남에서양재);
        이호선.addSection(교대에서강남);
        삼호선.addSection(교대에서양재);
        삼호선.addSection(교대에서남부터미널);

        List<Line> 지하철노선 = Arrays.asList(신분당선, 이호선, 삼호선);

        subwayMap = new SubwayMap(지하철노선);
    }

    @Test
    void testGetShortestPath() {
        assertThat(subwayMap.getShortestDistance(교대역_ID, 양재역_ID)).isEqualTo(5);
    }

    @Test
    void testGetStationsOnPath() {
        assertThat(subwayMap.getShortestPathIds(교대역_ID, 양재역_ID))
                .isEqualTo(Arrays.asList(교대역_ID, 남부터미널역_ID, 양재역_ID));
    }
}