package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PathTest {

    @Test
    void create() {
        List<Section> sections = new ArrayList<>();

        assertThatNoException().isThrownBy(() -> new Path(sections));
    }

    @Test
    void createShortestPath() {
        List<Section> sections = new ArrayList<>();
        Line line = new Line("2호선", "bg-red-200");
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(3L, "선릉역");
        sections.add(new Section(line, 강남역, new Station(2L, "역삼역"), 10));
        sections.add(new Section(line, new Station(2L, "역삼역"), 선릉역, 10));
        sections.add(new Section(line, 선릉역, new Station(4L, "삼성역"), 10));

        Path path = new Path(sections);
        List<Long> shortestPath = path.createShortestPath(강남역.getId(), 선릉역.getId());

        assertThat(shortestPath).containsExactly(1L, 2L, 3L);
    }

    @Test
    void calculateDistance() {
        List<Section> sections = new ArrayList<>();
        Line line = new Line("2호선", "bg-red-200");
        Station upStation = new Station(1L, "강남역");
        Station downStation = new Station(3L, "선릉역");
        sections.add(new Section(line, upStation, new Station(2L, "역삼역"), 10));
        sections.add(new Section(line, new Station(2L, "역삼역"), new Station(3L, "선릉역"), 10));
        sections.add(new Section(line, new Station(3L, "선릉역"), new Station(4L, "삼성역"), 10));

        Path path = new Path(sections);
        List<Long> shortestPath = path.createShortestPath(upStation.getId(), downStation.getId());

        assertThat(path.calculateDistance(upStation.getId(), downStation.getId())).isEqualTo(20);

    }

    @Test
    void createShortestPathWithTransferLine() {
        List<Section> sections = new ArrayList<>();
        Line 이호선 = new Line("2호선", "bg-red-200");
        Line 구호선 = new Line("9호선", "bg-blue-200");
        Station 성수역 = new Station(1L, "성수역");
        Station 건대입구 = new Station(2L, "건대입구");
        Station 강남구청 = new Station(3L, "강남구청");

        sections.add(new Section(이호선, 성수역, 건대입구, 10));
        sections.add(new Section(구호선, 건대입구, 강남구청, 10));

        Path path = new Path(sections);
        List<Long> shortestPath = path.createShortestPath(성수역.getId(), 강남구청.getId());

        assertThat(path.calculateDistance(성수역.getId(), 강남구청.getId())).isEqualTo(20);

        assertThat(shortestPath).containsExactly(성수역.getId(), 건대입구.getId(), 강남구청.getId());
    }

    @Test
    void createShortestPathsWithTransferLine() {
        List<Section> sections = new ArrayList<>();
        Line 이호선 = new Line("2호선", "bg-red-200");
        Line 구호선 = new Line("9호선", "bg-blue-200");
        Line 삼호선 = new Line("3호선", "bg-pink-200");

        Station 성수역 = new Station(1L, "성수역");
        Station 건대입구 = new Station(2L, "건대입구");
        Station 강남구청 = new Station(3L, "강남구청");
        Station 서울숲 = new Station(4L, "서울숲");

        sections.add(new Section(이호선, 성수역, 건대입구, 10));
        sections.add(new Section(구호선, 건대입구, 강남구청, 10));
        sections.add(new Section(삼호선, 성수역, 서울숲, 5));
        sections.add(new Section(삼호선, 서울숲, 강남구청, 10));

        Path path = new Path(sections);
        List<Long> shortestPath = path.createShortestPath(성수역.getId(), 강남구청.getId());

        assertThat(path.calculateDistance(성수역.getId(), 강남구청.getId())).isEqualTo(15);

        assertThat(shortestPath).containsExactly(성수역.getId(), 서울숲.getId(), 강남구청.getId());
    }

    @Test
    void createShortestPathsWithTransferLines() {
        List<Section> sections = new ArrayList<>();
        Line 이호선 = new Line("2호선", "bg-red-200");
        Line 구호선 = new Line("9호선", "bg-blue-200");
        Line 삼호선 = new Line("3호선", "bg-pink-200");

        Station 성수역 = new Station(1L, "성수역");
        Station 건대입구 = new Station(2L, "건대입구");
        Station 강남구청 = new Station(3L, "강남구청");
        Station 홍대 = new Station(4L, "홍대");

        sections.add(new Section(이호선, 성수역, 건대입구, 10));
        sections.add(new Section(구호선, 건대입구, 강남구청, 10));
        sections.add(new Section(삼호선, 강남구청, 홍대, 10));

        Path path = new Path(sections);
        List<Long> shortestPath = path.createShortestPath(성수역.getId(), 홍대.getId());

        assertThat(path.calculateDistance(성수역.getId(), 홍대.getId())).isEqualTo(30);

        assertThat(shortestPath).containsExactly(성수역.getId(), 건대입구.getId(), 강남구청.getId(),
                홍대.getId());
    }

    @Test
    void calculateFare() {
        List<Section> sections = new ArrayList<>();
        Line 이호선 = new Line("2호선", "bg-red-200");
        Line 구호선 = new Line("9호선", "bg-blue-200");
        Line 삼호선 = new Line("3호선", "bg-pink-200");

        Station 성수역 = new Station(1L, "성수역");
        Station 건대입구 = new Station(2L, "건대입구");
        Station 강남구청 = new Station(3L, "강남구청");
        Station 홍대 = new Station(4L, "홍대");

        sections.add(new Section(이호선, 성수역, 건대입구, 10));
        sections.add(new Section(구호선, 건대입구, 강남구청, 10));
        sections.add(new Section(삼호선, 강남구청, 홍대, 10));

        Path path = new Path(sections);
        List<Long> shortestPath = path.createShortestPath(성수역.getId(), 홍대.getId());
        int distance = path.calculateDistance(성수역.getId(), 홍대.getId());
        Fare fare = new Fare();
        int actual = fare.calculateFare(distance, new BasicFareStrategy());

        assertThat(actual).isEqualTo(1650);
    }
}
