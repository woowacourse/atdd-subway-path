package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathTest {

    private Section sectionOneToTwoInLine1;
    private Section sectionTwoToThreeInLine1;
    private Section sectionThreeToFourInLine1;
    private Section sectionThreeToFourInLine2;
    private Section sectionTwoToSixInLine2;
    private Section sectionSevenToTwoInLine2;
    private Section sectionTwoToThreeInLine2;

    @BeforeEach
    void init() {
        sectionOneToTwoInLine1 = new Section(1L, 1L, 2L, 1);
        sectionTwoToThreeInLine1 = new Section(1L, 2L, 3L, 2);
        sectionThreeToFourInLine1 = new Section(1L, 3L, 4L, 4);
        sectionTwoToThreeInLine2 = new Section(2L, 3L, 4L, 3);
        sectionThreeToFourInLine2 = new Section(2L, 3L, 4L, 8);
        sectionTwoToSixInLine2 = new Section(2L, 2L, 6L, 16);
        sectionSevenToTwoInLine2 = new Section(2L, 7L, 2L, 32);
    }

    @DisplayName("최단 거리를 정상적으로 찾는지 확인한다.")
    @Test
    void findShortestDistance() {
        final Sections sections = new Sections(List.of(sectionOneToTwoInLine1, sectionTwoToThreeInLine1, sectionThreeToFourInLine1));
        final Path path = Path.of(new Dijkstra(sections), 1L, 4L);
        final int result = path.getShortestDistance();

        assertThat(result).isEqualTo(7);
    }

    @DisplayName("최단 경로를 정상적으로 찾는지 확인한다.")
    @Test
    void findShortestPath() {
        final Sections sections = new Sections(List.of(sectionOneToTwoInLine1, sectionTwoToThreeInLine1, sectionThreeToFourInLine1));
        final Path path = Path.of(new Dijkstra(sections), 1L, 4L);
        final List<Long> shortestPath = path.getShortestPath();

        assertThat(shortestPath).containsExactlyElementsOf(List.of(1L, 2L, 3L, 4L));
    }

    @DisplayName("등록한 구간과 반대방향 일 때 최단 경로를 정상적으로 찾는지 확인한다.")
    @Test
    void findShortestPathReverse() {
        final Sections sections = new Sections(List.of(sectionOneToTwoInLine1, sectionTwoToThreeInLine1, sectionThreeToFourInLine1));
        final Path path = Path.of(new Dijkstra(sections), 4L, 1L);
        final List<Long> shortestPath = path.getShortestPath();

        assertThat(shortestPath).containsExactlyElementsOf(List.of(4L, 3L, 2L, 1L));
    }

    @DisplayName("노선이 다를 때 최단 거리를 정상적으로 찾는지 확인한다.")
    @Test
    void findShortestPathWithDifferenceLine() {
        final Sections sections = new Sections(List.of(sectionOneToTwoInLine1, sectionTwoToThreeInLine1, sectionThreeToFourInLine1, sectionThreeToFourInLine2));
        final Path path = Path.of(new Dijkstra(sections), 1L, 4L);
        final int result = path.getShortestDistance();

        assertThat(result).isEqualTo(7);
    }

    @DisplayName("환승 역이 있을 때 최단 거리를 정상적으로 찾는지 확인한다.")
    @Test
    void findShortestPathWithTransferStations() {
        final Sections sections = new Sections(List.of(sectionOneToTwoInLine1, sectionTwoToThreeInLine1, sectionThreeToFourInLine1, sectionSevenToTwoInLine2, sectionTwoToSixInLine2));
        final Path path = Path.of(new Dijkstra(sections), 7L, 6L);
        final int result = path.getShortestDistance();

        assertThat(result).isEqualTo(48);
    }

    @DisplayName("출발역과 도착역이 같을 때 예외를 발생시키는지 확인한다.")
    @Test
    void findShortestPathSameStationsException() {
        final Sections sections = new Sections(List.of(sectionOneToTwoInLine1, sectionTwoToThreeInLine1, sectionThreeToFourInLine1));
        assertThatThrownBy(() ->
                Path.of(new Dijkstra(sections), 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역이 같습니다.");
    }

    @DisplayName("구간에 등록되지 않은 역을 선택했을 때 예외를 발생시키는지 확인한다.")
    @Test
    void findShortestPathNoneStationException() {
        final Sections sections = new Sections(List.of(sectionOneToTwoInLine1, sectionTwoToThreeInLine1, sectionThreeToFourInLine1));
        assertThatThrownBy(() ->
                Path.of(new Dijkstra(sections), 5L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간에 없는 역은 출발지 또는 목적지로 선택할 수 없습니다.");
    }

    @DisplayName("갈수 없는 구간을 선택했을 때 예외를 발생시키는지 확인한다.")
    @Test
    void findShortestPathNoneSectionException() {
        final Sections sections = new Sections(List.of(sectionThreeToFourInLine1, sectionOneToTwoInLine1));
        assertThatThrownBy(() ->
                Path.of(new Dijkstra(sections), 1L, 3L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이동할 수 없는 경로입니다.");
    }

    @DisplayName("최단 경로의 노선 아이디를 올바르게 반환하는지 확인한다.")
    @Test
    void findLines() {
        final Sections sections = new Sections(List.of(sectionOneToTwoInLine1, sectionTwoToThreeInLine1, sectionThreeToFourInLine2));
        final Path path = Path.of(new Dijkstra(sections), 1L, 4L);
        final Set<Long> lineIds = path.getLineIds();

        assertThat(lineIds).contains(1L, 2L);
    }
}
