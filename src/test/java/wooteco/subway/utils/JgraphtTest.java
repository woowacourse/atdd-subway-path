package wooteco.subway.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.BasicFareStrategy;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

class JgraphtTest {

    @Test
    @DisplayName("구간 리스트를 받아 그래프를 생성한다.")
    void create() {
        List<Section> sections = new ArrayList<>();
        assertThatNoException().isThrownBy(() -> Jgrapht.initSectionGraph(sections));
    }

    @Test
    @DisplayName("최단경로를 계산한다.")
    void createShortestPath() {
        List<Section> sections = new ArrayList<>();
        Line line = new Line("2호선", "bg-red-200", 900);
        Station 강남역 = new Station(1L, "강남역");
        Station 역삼역 = new Station(2L, "역삼역");
        Station 선릉역 = new Station(3L, "선릉역");
        Station 삼성역 = new Station(4L, "삼성역");
        sections.add(new Section(line, 강남역, 역삼역, 10));
        sections.add(new Section(line, 역삼역, 선릉역, 10));
        sections.add(new Section(line, 선릉역, 삼성역, 10));

        DijkstraShortestPath dijkstraShortestPath = Jgrapht.initSectionGraph(sections);
        List<Station> shortestPath = Jgrapht.createShortestPath(dijkstraShortestPath, 강남역, 선릉역);

        assertThat(shortestPath).containsExactly(강남역, 역삼역, 선릉역);
    }

    @Test
    @DisplayName("최단거리를 계산한다.")
    void calculateDistance() {
        List<Section> sections = new ArrayList<>();
        Line line = new Line("2호선", "bg-red-200", 900);
        Station upStation = new Station(1L, "강남역");
        Station downStation = new Station(3L, "선릉역");
        sections.add(new Section(line, upStation, new Station(2L, "역삼역"), 10));
        sections.add(new Section(line, new Station(2L, "역삼역"), new Station(3L, "선릉역"), 10));
        sections.add(new Section(line, new Station(3L, "선릉역"), new Station(4L, "삼성역"), 10));

        DijkstraShortestPath dijkstraShortestPath = Jgrapht.initSectionGraph(sections);
        Jgrapht.createShortestPath(dijkstraShortestPath, upStation, downStation);

        assertThat(Jgrapht.calculateDistance(dijkstraShortestPath, upStation, downStation)).isEqualTo(20);

    }

    @Test
    @DisplayName("환승을 고려하여 최단경로를 계산한다.")
    void createShortestPathWithTransferLine1() {
        List<Section> sections = new ArrayList<>();
        Line 이호선 = new Line("2호선", "bg-red-200", 900);
        Line 구호선 = new Line("9호선", "bg-blue-200", 900);
        Station 성수역 = new Station(1L, "성수역");
        Station 건대입구 = new Station(2L, "건대입구");
        Station 강남구청 = new Station(3L, "강남구청");

        sections.add(new Section(이호선, 성수역, 건대입구, 10));
        sections.add(new Section(구호선, 건대입구, 강남구청, 10));

        DijkstraShortestPath dijkstraShortestPath = Jgrapht.initSectionGraph(sections);
        List<Station> shortestPath = Jgrapht.createShortestPath(dijkstraShortestPath, 성수역, 강남구청);
        int distance = Jgrapht.calculateDistance(dijkstraShortestPath, 성수역, 강남구청);

        assertThat(distance).isEqualTo(20);
        assertThat(shortestPath).containsExactly(성수역, 건대입구, 강남구청);
    }

    @Test
    @DisplayName("환승을 고려하여 최단경로를 계산한다.")
    void createShortestPathWithTransferLine2() {
        List<Section> sections = new ArrayList<>();
        Line 이호선 = new Line("2호선", "bg-red-200", 900);
        Line 구호선 = new Line("9호선", "bg-blue-200", 900);
        Line 삼호선 = new Line("3호선", "bg-pink-200", 900);

        Station 성수역 = new Station(1L, "성수역");
        Station 건대입구 = new Station(2L, "건대입구");
        Station 강남구청 = new Station(3L, "강남구청");
        Station 서울숲 = new Station(4L, "서울숲");

        sections.add(new Section(이호선, 성수역, 건대입구, 10));
        sections.add(new Section(구호선, 건대입구, 강남구청, 10));
        sections.add(new Section(삼호선, 성수역, 서울숲, 5));
        sections.add(new Section(삼호선, 서울숲, 강남구청, 10));

        DijkstraShortestPath dijkstraShortestPath = Jgrapht.initSectionGraph(sections);
        List<Station> shortestPath = Jgrapht.createShortestPath(dijkstraShortestPath, 성수역, 강남구청);
        int distance = Jgrapht.calculateDistance(dijkstraShortestPath, 성수역, 강남구청);

        assertThat(distance).isEqualTo(15);

        assertThat(shortestPath).containsExactly(성수역, 서울숲, 강남구청);
    }

    @Test
    @DisplayName("환승을 고려하여 최단경로를 계산한다.")
    void createShortestPathWithTransferLine3() {
        List<Section> sections = new ArrayList<>();
        Line 이호선 = new Line("2호선", "bg-red-200", 900);
        Line 구호선 = new Line("9호선", "bg-blue-200", 900);
        Line 삼호선 = new Line("3호선", "bg-pink-200", 900);

        Station 성수역 = new Station(1L, "성수역");
        Station 건대입구 = new Station(2L, "건대입구");
        Station 강남구청 = new Station(3L, "강남구청");
        Station 홍대 = new Station(4L, "홍대");

        sections.add(new Section(이호선, 성수역, 건대입구, 10));
        sections.add(new Section(구호선, 건대입구, 강남구청, 10));
        sections.add(new Section(삼호선, 강남구청, 홍대, 10));

        DijkstraShortestPath dijkstraShortestPath = Jgrapht.initSectionGraph(sections);
        List<Station> shortestPath = Jgrapht.createShortestPath(dijkstraShortestPath, 성수역, 홍대);
        int distance = Jgrapht.calculateDistance(dijkstraShortestPath, 성수역, 홍대);

        assertThat(distance).isEqualTo(30);
        assertThat(shortestPath).containsExactly(성수역, 건대입구, 강남구청, 홍대);
    }

    @Test
    @DisplayName("추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가한다.")
    void calculateFare1() {
        List<Section> sections = new ArrayList<>();
        Line 이호선 = new Line("2호선", "bg-red-200", 900);
        Line 구호선 = new Line("9호선", "bg-blue-200", 0);
        Line 삼호선 = new Line("3호선", "bg-pink-200", 0);

        Station 성수역 = new Station(1L, "성수역");
        Station 건대입구 = new Station(2L, "건대입구");
        Station 강남구청 = new Station(3L, "강남구청");
        Station 홍대 = new Station(4L, "홍대");

        sections.add(new Section(이호선, 성수역, 건대입구, 10));
        sections.add(new Section(구호선, 건대입구, 강남구청, 10));
        sections.add(new Section(삼호선, 강남구청, 홍대, 10));

        DijkstraShortestPath dijkstraShortestPath = Jgrapht.initSectionGraph(sections);
        List<Station> shortestPath = Jgrapht.createShortestPath(dijkstraShortestPath, 성수역, 홍대);
        int distance = Jgrapht.calculateDistance(dijkstraShortestPath, 성수역, 홍대);
        int extraFare = Jgrapht.calculateExtraFare(dijkstraShortestPath, 성수역, 홍대);
        Fare fare = new Fare();
        int actual = fare.calculateFare(distance, extraFare, new BasicFareStrategy());

        assertThat(actual).isEqualTo(2550);
    }

    @Test
    @DisplayName("경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용한다.")
    void calculateFare2() {
        List<Section> sections = new ArrayList<>();
        Line 이호선 = new Line("2호선", "bg-red-200", 900);
        Line 구호선 = new Line("9호선", "bg-blue-200", 500);
        Line 삼호선 = new Line("3호선", "bg-pink-200", 0);

        Station 성수역 = new Station(1L, "성수역");
        Station 건대입구 = new Station(2L, "건대입구");
        Station 강남구청 = new Station(3L, "강남구청");
        Station 홍대 = new Station(4L, "홍대");

        sections.add(new Section(이호선, 성수역, 건대입구, 10));
        sections.add(new Section(구호선, 건대입구, 강남구청, 10));
        sections.add(new Section(삼호선, 강남구청, 홍대, 10));

        DijkstraShortestPath dijkstraShortestPath = Jgrapht.initSectionGraph(sections);
        List<Station> shortestPath = Jgrapht.createShortestPath(dijkstraShortestPath, 성수역, 홍대);
        int distance = Jgrapht.calculateDistance(dijkstraShortestPath, 성수역, 홍대);
        int extraFare = Jgrapht.calculateExtraFare(dijkstraShortestPath, 성수역, 홍대);
        Fare fare = new Fare();
        int actual = fare.calculateFare(distance, extraFare, new BasicFareStrategy());

        assertThat(actual).isEqualTo(2550);
    }
}
