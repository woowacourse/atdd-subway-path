package wooteco.subway.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

class PathFinderTest {

    private Multigraph<Station, ShortestPathEdge> graph;
    private Station 강남역 = new Station(1L, "강남역");
    private Station 역삼역 = new Station(2L, "역삼역");
    private Station 선릉역 = new Station(3L, "선릉역");
    private Station 삼성역 = new Station(4L, "삼성역");
    private Station 성수역 = new Station(1L, "성수역");
    private Station 건대입구 = new Station(2L, "건대입구");
    private Station 강남구청 = new Station(3L, "강남구청");
    private Station 홍대 = new Station(4L, "홍대");

    @BeforeEach
    void setUp() {
        graph = new WeightedMultigraph<>(ShortestPathEdge.class);
    }

    @Test
    @DisplayName("최단경로 및 거리를 계산한다.")
    void createShortestPath() {
        List<Section> sections = new ArrayList<>();
        Line line = new Line("2호선", "bg-red-200", 900);
        sections.add(new Section(line, 강남역, 역삼역, 10));
        sections.add(new Section(line, 역삼역, 선릉역, 10));
        sections.add(new Section(line, 선릉역, 삼성역, 10));

        PathFinder pathFinder = new PathFinder(new DijkstraStrategy(sections));
        Path path = pathFinder.getPath(강남역, 선릉역);

        assertThat(path.getStations()).containsExactly(강남역, 역삼역, 선릉역);
        assertThat(path.getDistance()).isEqualTo(20);
    }

    @Test
    @DisplayName("환승을 고려하여 최단경로를 계산한다.")
    void createShortestPathWithTransferLine1() {
        List<Section> sections = new ArrayList<>();
        Line 이호선 = new Line("2호선", "bg-red-200", 900);
        Line 구호선 = new Line("9호선", "bg-blue-200", 900);

        sections.add(new Section(이호선, 성수역, 건대입구, 10));
        sections.add(new Section(구호선, 건대입구, 강남구청, 10));

        PathFinder pathFinder = new PathFinder(new DijkstraStrategy(sections));
        Path path = pathFinder.getPath(성수역, 강남구청);

        assertThat(path.getDistance()).isEqualTo(20);
        assertThat(path.getStations()).containsExactly(성수역, 건대입구, 강남구청);
    }

    @Test
    @DisplayName("환승을 고려하여 최단경로를 계산한다.")
    void createShortestPathWithTransferLine2() {
        List<Section> sections = new ArrayList<>();
        Line 이호선 = new Line("2호선", "bg-red-200", 900);
        Line 구호선 = new Line("9호선", "bg-blue-200", 900);
        Line 삼호선 = new Line("3호선", "bg-pink-200", 900);

        sections.add(new Section(이호선, 성수역, 건대입구, 10));
        sections.add(new Section(구호선, 건대입구, 강남구청, 10));
        sections.add(new Section(삼호선, 성수역, 홍대, 5));
        sections.add(new Section(삼호선, 홍대, 강남구청, 10));

        PathFinder pathFinder = new PathFinder(new DijkstraStrategy(sections));
        Path path = pathFinder.getPath(성수역, 강남구청);

        assertThat(path.getDistance()).isEqualTo(15);
        assertThat(path.getStations()).containsExactly(성수역, 홍대, 강남구청);
    }

    @Test
    @DisplayName("환승을 고려하여 최단경로를 계산한다.")
    void createShortestPathWithTransferLine3() {
        List<Section> sections = new ArrayList<>();
        Line 이호선 = new Line("2호선", "bg-red-200", 900);
        Line 구호선 = new Line("9호선", "bg-blue-200", 900);
        Line 삼호선 = new Line("3호선", "bg-pink-200", 900);

        sections.add(new Section(이호선, 성수역, 건대입구, 10));
        sections.add(new Section(구호선, 건대입구, 강남구청, 10));
        sections.add(new Section(삼호선, 강남구청, 홍대, 10));

        PathFinder pathFinder = new PathFinder(new DijkstraStrategy(sections));
        Path path = pathFinder.getPath(성수역, 홍대);

        assertThat(path.getDistance()).isEqualTo(30);
        assertThat(path.getStations()).containsExactly(성수역, 건대입구, 강남구청, 홍대);
    }

    @Test
    @DisplayName("추가 요금이 있는 노선을 이용 할 경우 추가요금을 계산한다.")
    void calculateFare1() {
        List<Section> sections = new ArrayList<>();
        Line 이호선 = new Line("2호선", "bg-red-200", 900);
        Line 구호선 = new Line("9호선", "bg-blue-200", 0);
        Line 삼호선 = new Line("3호선", "bg-pink-200", 0);

        sections.add(new Section(이호선, 성수역, 건대입구, 10));
        sections.add(new Section(구호선, 건대입구, 강남구청, 10));
        sections.add(new Section(삼호선, 강남구청, 홍대, 10));

        PathFinder pathFinder = new PathFinder(new DijkstraStrategy(sections));
        Path path = pathFinder.getPath(성수역, 홍대);

        assertThat(path.getExtraFare()).isEqualTo(900);
    }

    @Test
    @DisplayName("경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만을 계산한다.")
    void calculateFare2() {
        List<Section> sections = new ArrayList<>();
        Line 이호선 = new Line("2호선", "bg-red-200", 900);
        Line 구호선 = new Line("9호선", "bg-blue-200", 500);
        Line 삼호선 = new Line("3호선", "bg-pink-200", 0);

        sections.add(new Section(이호선, 성수역, 건대입구, 10));
        sections.add(new Section(구호선, 건대입구, 강남구청, 10));
        sections.add(new Section(삼호선, 강남구청, 홍대, 10));

        PathFinder pathFinder = new PathFinder(new DijkstraStrategy(sections));
        Path path = pathFinder.getPath(성수역, 홍대);

        assertThat(path.getExtraFare()).isEqualTo(900);
    }
}
