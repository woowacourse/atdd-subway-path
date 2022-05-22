package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JgraphtPathfinderTest {

    @Autowired
    private PathFinder pathFinder;

    @DisplayName("10km 이하인 경우 요금 1250원을 부과한다.")
    @Test
    void under10_1250() {
        Station 강남 = new Station("강남역");
        Station 선릉 = new Station("선릉역");
        Station 잠실 = new Station("잠실역");
        Station 홍대 = new Station("홍대입구역");

        Section 강남_선릉 = new Section(강남, 선릉, 3);
        Section 선릉_홍대 = new Section(선릉, 홍대, 2);
        Line 일호선 = new Line("일호선", "blue", new Sections(List.of(강남_선릉, 선릉_홍대)), 0);

        Section 강남_홍대 = new Section(강남, 홍대, 10);
        Line 이호선 = new Line("이호선", "red", new Sections(강남_홍대), 0);

        Section 강남_잠실 = new Section(강남, 잠실, 3);
        Section 잠실_홍대 = new Section(잠실, 홍대, 5);
        Line 삼호선 = new Line("삼호선", "orange", new Sections(List.of(강남_잠실, 잠실_홍대)), 0);

        Path path = pathFinder.findShortest(List.of(일호선, 이호선, 삼호선), 강남, 홍대);

        int fare = path.chargeFare();

        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("10km 초과 50km 이하인 경우 요금 1250원 + 5km당 100원을 부과한다.")
    @Test
    void over10_under50_1250_additional_fare_100_per_5km() {
        Station 강남 = new Station("강남역");
        Station 선릉 = new Station("선릉역");
        Station 잠실 = new Station("잠실역");
        Station 홍대 = new Station("홍대입구역");

        Section 강남_선릉 = new Section(강남, 선릉, 30);
        Section 선릉_홍대 = new Section(선릉, 홍대, 20);
        Line 일호선 = new Line("일호선", "blue", new Sections(List.of(강남_선릉, 선릉_홍대)), 0);

        Section 강남_홍대 = new Section(강남, 홍대, 100);
        Line 이호선 = new Line("이호선", "red", new Sections(강남_홍대), 0);

        Section 강남_잠실 = new Section(강남, 잠실, 30);
        Section 잠실_홍대 = new Section(잠실, 홍대, 50);
        Line 삼호선 = new Line("삼호선", "orange", new Sections(List.of(강남_잠실, 잠실_홍대)), 0);

        Path path = pathFinder.findShortest(List.of(일호선, 이호선, 삼호선), 강남, 홍대);

        int fare = path.chargeFare();

        assertThat(fare).isEqualTo(2050);
    }

    @DisplayName("50km 초과인 경우 요금 1250원 + 5km당 100원 + 8km당 100원을 부과한다.")
    @Test
    void over50_1250_additional_fare_100_per_8km() {
        Station 강남 = new Station("강남역");
        Station 선릉 = new Station("선릉역");
        Station 잠실 = new Station("잠실역");
        Station 홍대 = new Station("홍대입구역");

        Section 강남_선릉 = new Section(강남, 선릉, 100);
        Section 선릉_홍대 = new Section(선릉, 홍대, 78);
        Line 일호선 = new Line("일호선", "blue", new Sections(List.of(강남_선릉, 선릉_홍대)), 0);

        Section 강남_홍대 = new Section(강남, 홍대, 200);
        Line 이호선 = new Line("이호선", "red", new Sections(강남_홍대), 0);

        Section 강남_잠실 = new Section(강남, 잠실, 300);
        Section 잠실_홍대 = new Section(잠실, 홍대, 5);
        Line 삼호선 = new Line("삼호선", "orange", new Sections(List.of(강남_잠실, 잠실_홍대)), 0);

        Path path = pathFinder.findShortest(List.of(일호선, 이호선, 삼호선), 강남, 홍대);

        int fare = path.chargeFare();

        assertThat(fare).isEqualTo(3650);
    }

    @DisplayName("경로의 최단 거리를 계산한다.")
    @Test
    void calculateShortestDistance() {
        Station 강남 = new Station("강남역");
        Station 선릉 = new Station("선릉역");
        Station 잠실 = new Station("잠실역");
        Station 홍대 = new Station("홍대입구역");

        Section 강남_선릉 = new Section(강남, 선릉, 3);
        Section 선릉_홍대 = new Section(선릉, 홍대, 2);
        Line 일호선 = new Line("일호선", "blue", new Sections(List.of(강남_선릉, 선릉_홍대)), 0);

        Section 강남_홍대 = new Section(강남, 홍대, 10);
        Line 이호선 = new Line("이호선", "red", new Sections(강남_홍대), 0);

        Section 강남_잠실 = new Section(강남, 잠실, 3);
        Section 잠실_홍대 = new Section(잠실, 홍대, 5);
        Line 삼호선 = new Line("삼호선", "orange", new Sections(List.of(강남_잠실, 잠실_홍대)), 0);

        Path path = pathFinder.findShortest(List.of(일호선, 이호선, 삼호선), 강남, 홍대);

        int distance = path.calculateShortestDistance();

        assertThat(distance).isEqualTo(5);
    }

    @DisplayName("최단 경로의 역 리스트를 반환한다.")
    @Test
    void findStationsOnPath() {
        Station 강남 = new Station("강남역");
        Station 선릉 = new Station("선릉역");
        Station 잠실 = new Station("잠실역");
        Station 홍대 = new Station("홍대입구역");

        Section 강남_선릉 = new Section(강남, 선릉, 3);
        Section 선릉_홍대 = new Section(선릉, 홍대, 2);
        Line 일호선 = new Line("일호선", "blue", new Sections(List.of(강남_선릉, 선릉_홍대)), 0);

        Section 강남_홍대 = new Section(강남, 홍대, 10);
        Line 이호선 = new Line("이호선", "red", new Sections(강남_홍대), 0);

        Section 강남_잠실 = new Section(강남, 잠실, 3);
        Section 잠실_홍대 = new Section(잠실, 홍대, 5);
        Line 삼호선 = new Line("삼호선", "orange", new Sections(List.of(강남_잠실, 잠실_홍대)), 0);

        Path path = pathFinder.findShortest(List.of(일호선, 이호선, 삼호선), 강남, 홍대);

        List<Station> stationsOnPath = path.findStationsOnPath();

        assertThat(stationsOnPath).contains(강남, 선릉, 홍대);
    }
}
