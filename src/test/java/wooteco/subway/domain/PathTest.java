package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    @DisplayName("10km 이하인 경우 요금 1250원을 부과한다.")
    @Test
    void under10_1250() {
        Station 강남 = new Station("강남역");
        Station 선릉 = new Station("선릉역");
        Station 잠실 = new Station("잠실역");
        Station 홍대 = new Station("홍대입구역");

        Section 강남_선릉 = new Section(강남, 선릉, 3);
        Section 강남_잠실 = new Section(강남, 잠실, 3);
        Section 강남_홍대 = new Section(강남, 홍대, 10);
        Section 선릉_홍대 = new Section(선릉, 홍대, 2);
        Section 잠실_홍대 = new Section(잠실, 홍대, 5);

        Sections sections = new Sections(List.of(강남_선릉, 강남_잠실, 강남_홍대, 선릉_홍대, 잠실_홍대));

        Path path = new Path(sections.findShortestPath(강남, 홍대));

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
        Section 강남_잠실 = new Section(강남, 잠실, 30);
        Section 강남_홍대 = new Section(강남, 홍대, 100);
        Section 선릉_홍대 = new Section(선릉, 홍대, 20);
        Section 잠실_홍대 = new Section(잠실, 홍대, 50);

        Sections sections = new Sections(List.of(강남_선릉, 강남_잠실, 강남_홍대, 선릉_홍대, 잠실_홍대));

        Path path = new Path(sections.findShortestPath(강남, 홍대));

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
        Section 강남_잠실 = new Section(강남, 잠실, 300);
        Section 강남_홍대 = new Section(강남, 홍대, 200);
        Section 선릉_홍대 = new Section(선릉, 홍대, 78);
        Section 잠실_홍대 = new Section(잠실, 홍대, 5);

        Sections sections = new Sections(List.of(강남_선릉, 강남_잠실, 강남_홍대, 선릉_홍대, 잠실_홍대));

        Path path = new Path(sections.findShortestPath(강남, 홍대));

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
        Section 강남_잠실 = new Section(강남, 잠실, 3);
        Section 강남_홍대 = new Section(강남, 홍대, 10);
        Section 선릉_홍대 = new Section(선릉, 홍대, 2);
        Section 잠실_홍대 = new Section(잠실, 홍대, 5);

        Sections sections = new Sections(List.of(강남_선릉, 강남_잠실, 강남_홍대, 선릉_홍대, 잠실_홍대));

        Path path = new Path(sections.findShortestPath(강남, 홍대));

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
        Section 강남_잠실 = new Section(강남, 잠실, 3);
        Section 강남_홍대 = new Section(강남, 홍대, 10);
        Section 선릉_홍대 = new Section(선릉, 홍대, 2);
        Section 잠실_홍대 = new Section(잠실, 홍대, 5);

        Sections sections = new Sections(List.of(강남_선릉, 강남_잠실, 강남_홍대, 선릉_홍대, 잠실_홍대));

        Path path = new Path(sections.findShortestPath(강남, 홍대));

        List<Station> stationsOnPath = path.findStationsOnPath();

        assertThat(stationsOnPath).contains(강남, 선릉, 홍대);
    }
}
