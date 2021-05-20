package wooteco.subway.path.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("최단 경로 도메인 테스트")
class ShortestPathTest {
    private Station 왕십리역;
    private Station 천호역;
    private Station 잠실역;
    private Line 이호선;
    private Line 오호선;
    private Line 팔호선;

    @BeforeEach
    void setUp() {
        왕십리역 = new Station(1L, "왕십리역");
        천호역 = new Station(2L, "천호역");
        잠실역 = new Station(3L, "잠실역");

        이호선 = new Line(1L, "2호선", "green", new Sections(
                Collections.singletonList(new Section(1L, 왕십리역, 잠실역, 15))
        ));
        오호선 = new Line(2L, "5호선", "purple", new Sections(
                Collections.singletonList(new Section(2L, 왕십리역, 천호역, 10))
        ));
        팔호선 = new Line(3L, "8호선", "pink", new Sections(
                Collections.singletonList(new Section(3L, 천호역, 잠실역, 3))
        ));
    }

    @Test
    @DisplayName("경로 찾기")
    void getPath() {
        // given
        List<Line> lines = Arrays.asList(이호선, 오호선, 팔호선);
        ShortestPath path = new ShortestPath(lines);

        // when
        List<Long> stations = path.getPath(왕십리역.getId(), 잠실역.getId());

        // then
        assertThat(stations)
                .hasSize(3)
                .hasSameElementsAs(Arrays.asList(왕십리역.getId(), 천호역.getId(), 잠실역.getId()));

    }

    @Test
    @DisplayName("거리 찾기")
    void distance() {
        // given
        List<Line> lines = Arrays.asList(이호선, 오호선, 팔호선);
        ShortestPath path = new ShortestPath(lines);

        // when
        int distance = path.distance(왕십리역.getId(), 잠실역.getId());

        // then
        assertThat(distance).isEqualTo(13);
    }
}