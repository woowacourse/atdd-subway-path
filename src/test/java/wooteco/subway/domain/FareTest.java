package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @DisplayName("한 노선만 이동하는 경우, 해당 노선의 추가 요금을 적용한 경우의 요금을 계산한다.")
    @Test
    void calculateAdditionalFare() {
        Station 잠실 = new Station("잠실역");
        Station 홍대 = new Station("홍대입구역");
        Station 신촌 = new Station("신촌역");

        Section 잠실_홍대 = new Section(잠실, 홍대, 3);
        Section 홍대_신촌 = new Section(홍대, 신촌, 5);

        Sections sections = new Sections(List.of(잠실_홍대, 홍대_신촌));

        Line 일호선 = new Line("1호선", "green", 1000, sections);
        List<Line> lines = List.of(일호선);

        Path path = new Path(sections.findShortestPath(잠실, 신촌, lines));

        int fare = Fare.chargeFare(path, 22);

        assertThat(fare).isEqualTo(2250);
    }

    @DisplayName("환승하는 경우 비싼 노선의 추가금액을 이용한다.")
    @Test
    void calculateTransferAdditionalFare() {
        Station 잠실 = new Station("잠실역");
        Station 홍대 = new Station("홍대입구역");
        Station 신촌 = new Station("신촌역");

        Section 잠실_홍대 = new Section(잠실, 홍대, 3);
        Section 홍대_신촌 = new Section(홍대, 신촌, 5);

        Sections 일호선구간 = new Sections(List.of(잠실_홍대));
        Sections 이호선구간 = new Sections(List.of(홍대_신촌));
        Sections 전체구간 = new Sections(List.of(잠실_홍대, 홍대_신촌));

        Line 일호선 = new Line("1호선", "green", 1000, 일호선구간);
        Line 이호선 = new Line("2호선", "blue", 1500, 이호선구간);
        List<Line> lines = List.of(일호선, 이호선);

        Path path = new Path(전체구간.findShortestPath(잠실, 신촌, lines));

        int fare = Fare.chargeFare(path, 22);

        assertThat(fare).isEqualTo(2750);
    }

    @DisplayName("5세 이하인 경우 요금 무료")
    @Test
    void freeForInfant() {
        Station 잠실 = new Station("잠실역");
        Station 홍대 = new Station("홍대입구역");
        Station 신촌 = new Station("신촌역");

        Section 잠실_홍대 = new Section(잠실, 홍대, 3);
        Section 홍대_신촌 = new Section(홍대, 신촌, 5);

        Sections sections = new Sections(List.of(잠실_홍대, 홍대_신촌));

        Line 일호선 = new Line("1호선", "green", 1000, sections);
        List<Line> lines = List.of(일호선);

        Path path = new Path(sections.findShortestPath(잠실, 신촌, lines));

        int fare = Fare.chargeFare(path, 5);

        assertThat(fare).isEqualTo(0);
    }

    @DisplayName("어린이(6세~12세)인 경우 요금 350 공제 후 50프로 할인")
    @Test
    void discountForChild() {
        Station 잠실 = new Station("잠실역");
        Station 홍대 = new Station("홍대입구역");
        Station 신촌 = new Station("신촌역");

        Section 잠실_홍대 = new Section(잠실, 홍대, 3);
        Section 홍대_신촌 = new Section(홍대, 신촌, 5);

        Sections sections = new Sections(List.of(잠실_홍대, 홍대_신촌));

        Line 일호선 = new Line("1호선", "green", 1000, sections);
        List<Line> lines = List.of(일호선);

        Path path = new Path(sections.findShortestPath(잠실, 신촌, lines));

        int fare = Fare.chargeFare(path, 6);

        assertThat(fare).isEqualTo(950);
    }

    @DisplayName("청소년(13세~18세)인 경우 요금 350 공제 후 20프로 할인")
    @Test
    void discountForTeen() {
        Station 잠실 = new Station("잠실역");
        Station 홍대 = new Station("홍대입구역");
        Station 신촌 = new Station("신촌역");

        Section 잠실_홍대 = new Section(잠실, 홍대, 3);
        Section 홍대_신촌 = new Section(홍대, 신촌, 5);

        Sections sections = new Sections(List.of(잠실_홍대, 홍대_신촌));

        Line 일호선 = new Line("1호선", "green", 1000, sections);
        List<Line> lines = List.of(일호선);

        Path path = new Path(sections.findShortestPath(잠실, 신촌, lines));

        int fare = Fare.chargeFare(path, 13);

        assertThat(fare).isEqualTo(1520);
    }

    @DisplayName("어르신(65세 이상)인 경우 요금 무료")
    @Test
    void discountForOld() {
        Station 잠실 = new Station("잠실역");
        Station 홍대 = new Station("홍대입구역");
        Station 신촌 = new Station("신촌역");

        Section 잠실_홍대 = new Section(잠실, 홍대, 3);
        Section 홍대_신촌 = new Section(홍대, 신촌, 5);

        Sections sections = new Sections(List.of(잠실_홍대, 홍대_신촌));

        Line 일호선 = new Line("1호선", "green", 1000, sections);
        List<Line> lines = List.of(일호선);

        Path path = new Path(sections.findShortestPath(잠실, 신촌, lines));

        int fare = Fare.chargeFare(path, 65);

        assertThat(fare).isEqualTo(0);
    }


}
