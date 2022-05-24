package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static wooteco.subway.domain.fixture.LineFixture.*;
import static wooteco.subway.domain.fixture.SectionFixture.*;
import static wooteco.subway.domain.fixture.StationFixture.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.vo.Path;
import wooteco.subway.exception.EmptyResultException;

public class SubwayTest {
    private static Subway subway;

    @BeforeAll
    public static void setUp() {
        Line line1 = createLine1();
        Line line2 = createLine2();
        Line line3 = createLine3();
        Line line4 = createLine4();
        subway = Subway.from(List.of(line1, line2, line3, line4));
    }

    private static Line createLine1() {
        List<Section> sections1 = new ArrayList<>();
        sections1.add(LINE1_SECTION1);
        sections1.add(LINE1_SECTION2);
        sections1.add(LINE1_SECTION3);
        return Line.from(LINE1, sections1);
    }

    private static Line createLine2() {
        List<Section> sections2 = new ArrayList<>();
        sections2.add(LINE2_SECTION1);
        sections2.add(LINE2_SECTION2);
        sections2.add(LINE2_SECTION3);
        return Line.from(LINE2, sections2);
    }

    private static Line createLine3() {
        List<Section> sections3 = new ArrayList<>();
        sections3.add(LINE3_SECTION1);
        sections3.add(LINE3_SECTION2);
        sections3.add(LINE3_SECTION3);
        return Line.from(LINE3, sections3);
    }

    private static Line createLine4() {
        List<Section> sections4 = new ArrayList<>();
        sections4.add(LINE4_SECTION1);
        return Line.from(LINE4, sections4);
    }

    @Test
    @DisplayName("A -> B의 경로는 A B, 거리는 5가 반환되어야 한다.")
    void findShortestPath1() {
        Path path = subway.findShortestPath(STATION_A, STATION_B);

        assertAll(
            () -> assertThat(path.getStations())
                .containsExactly(STATION_A, STATION_B),
            () -> assertThat(path.getDistance()).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("A -> G의 경로는 A B E F G, 거리는 20이 반환되어야 한다.")
    void findShortestPath2() {
        Path path = subway.findShortestPath(STATION_A, STATION_G);

        assertAll(
            () -> assertThat(path.getStations())
                .containsExactly(STATION_A, STATION_B, STATION_E, STATION_F, STATION_G),
            () -> assertThat(path.getDistance()).isEqualTo(20)
        );
    }

    @Test
    @DisplayName("A -> I의 경로는 A B C H I, 거리는 58이 반환되어야 한다.")
    void findShortestPath3() {
        Path path = subway.findShortestPath(STATION_A, STATION_I);

        assertAll(
            () -> assertThat(path.getStations())
                .containsExactly(STATION_A, STATION_B, STATION_C, STATION_H, STATION_I),
            () -> assertThat(path.getDistance()).isEqualTo(58)
        );
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어있지 않으면 예외를 던져야 한다.")
    void findInvalidPath() {
        assertThatThrownBy(() -> subway.findShortestPath(STATION_E, STATION_J))
            .hasMessage("출발역과 도착역 사이에 연결된 경로가 없습니다.")
            .isInstanceOf(EmptyResultException.class);
    }

    @Test
    @DisplayName("출발역과 도착역이 같으면 예외를 던져야 한다.")
    void findSameStationsPath() {
        assertThatThrownBy(() -> subway.findShortestPath(STATION_A, STATION_A))
            .hasMessage("출발역과 도착역이 동일합니다.")
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("역이 존재하지 않으면 예외를 던져야 한다.")
    void findNonStationPath() {
        assertThatThrownBy(() -> subway.findShortestPath(new Station("없는역"), STATION_A))
            .hasMessage("해당 역을 찾지 못했습니다.")
            .isInstanceOf(IllegalArgumentException.class);
    }
}
