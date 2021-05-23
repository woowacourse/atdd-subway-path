package wooteco.subway.path.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.PathException;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathTest {

    private Path path;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 구간에존재하지않는역;

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
        구간에존재하지않는역 = new Station("존재하지않는역");

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
    @DisplayName("역 사이의 최단 경로 찾기")
    void findPath() {
        List<Station> findPath = path.findPath(교대역, 양재역);

        assertThat(findPath).containsExactly(교대역, 남부터미널역, 양재역);
    }

    @Test
    @DisplayName("역 사이의 최단 거리 찾기")
    void findDistance() {
        int distance = path.findDistance(교대역, 양재역);

        assertThat(distance).isEqualTo(5);
    }

    @Test
    @DisplayName("path가 null이거나 비어있을 경우")
    void pathIsNull() {
        List<Section> emptyList = new ArrayList();
        Sections emptySections = new Sections(emptyList);

        assertThatThrownBy(() -> path = new Path(null))
                .isInstanceOf(PathException.class);
        assertThatThrownBy(() -> path = new Path(emptySections))
                .isInstanceOf(PathException.class);

    }

    @Test
    @DisplayName("출발역 또는 도착역이 path에 존재하지 않을 경우")
    void notExistStation() {
        assertThatThrownBy(() -> path.findPath(구간에존재하지않는역, 양재역))
                .isInstanceOf(PathException.class);
    }
}