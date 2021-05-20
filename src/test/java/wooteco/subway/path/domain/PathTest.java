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
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @BeforeEach
    void setUp() {
        station1 = new Station("강남역");
        station2 = new Station("양재역");
        station3 = new Station("교대역");
        station4 = new Station("남부터미널역");

        Section section1 = new Section(station1, station3, 10);
        Section section2 = new Section(station1, station2, 10);
        Section section3 = new Section(station2, station4, 2);
        Section section4 = new Section(station3, station4, 3);

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
        List<Station> findPath = path.findPath(station3, station2);

        assertThat(findPath).containsExactly(station3, station4, station2);
    }

    @Test
    void findDistance() {
        int distance = path.findDistance(station3, station2);

        assertThat(distance).isEqualTo(5);
    }
}