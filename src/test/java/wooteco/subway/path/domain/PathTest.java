package wooteco.subway.path.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    private Path path;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        Section section1 = new Section(강남역, 교대역, 10);
        Section section2 = new Section(강남역, 양재역, 10);
        Section section3 = new Section(양재역, 남부터미널역, 2);
        Section section4 = new Section(교대역, 남부터미널역, 3);

        List<Section> sectionList = new ArrayList<>();

        sectionList.add(section1);
        sectionList.add(section2);
        sectionList.add(section3);
        sectionList.add(section4);

        Sections sections = new Sections(sectionList);

        path = new Path(sections);
    }

    @Test
    void findPath() {
        List<Station> findPath = path.findPath(교대역, 양재역);

        assertThat(findPath).containsExactly(교대역, 남부터미널역, 양재역);
    }

    @Test
    void findDistance() {
        int distance = path.findDistance(교대역, 양재역);

        assertThat(distance).isEqualTo(5);
    }
}