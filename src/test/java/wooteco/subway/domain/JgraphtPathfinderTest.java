package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.subway.domain.pathfinder.PathFinder;

@SpringBootTest
class JgraphtPathfinderTest {

    @Autowired
    private PathFinder pathFinder;
    
    @DisplayName("여러 노선을 지나쳐 올 때 제일 큰 추가 요금을 부과한다")
    @Test
    void path_passedMultipleLine_mostExpensiveExtraFare() {
        /*
        2호선 : 합정 -1- 당산 -10- 대림 / 100원
        4호선 : 삼각지 -10- 동작 -1- 사당 -10- 과천 / 200원
        6호선 : 합정 -10- 삼각지 -10- 이태원 / 300원
        9호선 : 당산 -1- 여의도 -1- 동작 / 1000원

        합정 -> 사당

        경로 : 합정 - 당산 - 동작 - 사당
        경유 : 2호선, 9호선, 4호선
        추가요금 : 1000원
         */
        Station 합정 = new Station("홍대");
        Station 당산 = new Station("당산");
        Station 대림 = new Station("대림");

        Station 삼각지 = new Station("삼각지");
        Station 동작 = new Station("동작");
        Station 사당 = new Station("사당");
        Station 과천 = new Station("과천");

        Station 이태원 = new Station("이태원");
        Station 여의도 = new Station("여의도");

        Section 이호선_합정_당산 = new Section(합정, 당산, 1);
        Section 이호선_당산_대림 = new Section(당산, 대림, 10);

        Section 사호선_삼각지_동작 = new Section(삼각지, 동작, 10);
        Section 사호선_동작_사당 = new Section(동작, 사당, 1);
        Section 사호선_사당_과천 = new Section(사당, 과천, 10);

        Section 육호선_합정_삼각지 = new Section(합정, 삼각지, 10);
        Section 육호선_삼각지_이태원 = new Section(삼각지, 이태원, 10);

        Section 구호선_당산_여의도 = new Section(당산, 여의도, 1);
        Section 구호선_여의도_동작 = new Section(여의도, 동작, 1);

        Line 이호선 = new Line("이호선", "green",
                new Sections(List.of(이호선_합정_당산, 이호선_당산_대림)), 100);
        Line 사호선 = new Line("사호선", "skyblue",
                new Sections(List.of(사호선_삼각지_동작, 사호선_동작_사당, 사호선_사당_과천)), 200);
        Line 육호선 = new Line("육호선", "brown",
                new Sections(List.of(육호선_합정_삼각지, 육호선_삼각지_이태원)), 300);
        Line 구호선 = new Line("구호선", "gold",
                new Sections(List.of(구호선_당산_여의도, 구호선_여의도_동작)), 1000);

        Path path = pathFinder.findShortest(List.of(이호선, 사호선, 육호선, 구호선), 합정, 사당);

        assertThat(path.getLineExtraFare()).isEqualTo(1000);
    }
    
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

        int fare = path.finalFare(25);

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

        int fare = path.finalFare(25);

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

        int fare = path.finalFare(25);

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

        int distance = path.getDistance();

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

        List<Station> stationsOnPath = path.getStations();

        assertThat(stationsOnPath).contains(강남, 선릉, 홍대);
    }
}
