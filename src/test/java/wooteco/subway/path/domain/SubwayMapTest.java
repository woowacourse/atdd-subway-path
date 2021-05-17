package wooteco.subway.path.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayMapTest {
    private SubwayMap subwayMap;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        Line 신분당선 = new Line("신분당선", "빨강");
        Line 이호선 = new Line("이호선", "초록");
        Line 삼호선 = new Line("삼호선", "주황");

        Section 강남에서양재 = new Section(강남역, 양재역, 10);
        Section 교대에서강남 = new Section(교대역, 강남역, 5);
        Section 교대에서양재 = new Section(교대역, 양재역, 5);
        Section 교대에서남부터미널 = new Section(교대역, 남부터미널역, 3);

        신분당선.addSection(강남에서양재);
        이호선.addSection(교대에서강남);
        삼호선.addSection(교대에서양재);
        삼호선.addSection(교대에서남부터미널);

        subwayMap = new SubwayMap(삼호선.getSections());
    }

    @Test
    void testGetShortestPath() {
        assertThat(subwayMap.getShortestPath(교대역, 양재역)).isEqualTo(5);
    }
}