package wooteco.subway;

import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class Fixtures {
    public static String COLOR1 = "bg-blue-600";
    public static String COLOR2 = "bg-green-600";
    public static String COLOR3 = "bg-orange-600";

    public static Station 강남역 = new Station(1L, "강남역");
    public static Station 역삼역 = new Station(2L, "역삼역");
    public static Station 선릉역 = new Station(3L, "선릉역");
    public static Station 삼성역 = new Station(4L, "삼성역");
    public static Station 종합운동장역 = new Station(5L, "종합운동장역");
    public static Station 잠실새내역 = new Station(6L, "잠실새내역");
    public static Station 잠실역 = new Station(7L, "잠실역");
    public static Station 부평역 = new Station(8L, "부평역");
    public static Station 부개역 = new Station(9L, "부개역");

    public static Section 강남_역삼_구간 = new Section(1L, 강남역, 역삼역, new Distance(5));
    public static Section 역삼_선릉_구간 = new Section(2L, 역삼역, 선릉역, new Distance(10));
    public static Section 선릉_삼성_구간 = new Section(3L, 선릉역, 삼성역, new Distance(15));
    public static Section 삼성_종합운동장_구간 = new Section(4L, 삼성역, 종합운동장역, new Distance(20));
    public static Section 종합운동장_잠실새내_구간 = new Section(5L, 종합운동장역, 잠실새내역, new Distance(25));
    public static Section 잠실새내_잠실_구간 = new Section(6L, 잠실새내역, 잠실역, new Distance(30));

    public static Line 이호선 = new Line("2호선", COLOR2, new Fare(600), 강남_역삼_구간);
    public static Line 삼호선 = new Line("3호선", COLOR3, new Fare(600), 강남_역삼_구간);
}
