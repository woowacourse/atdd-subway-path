package wooteco.subway.domain.fixtures;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class TestFixtures {

    public static final Station 강남 = new Station(1L, "강남");
    public static final Station 삼성 = new Station(2L, "삼성");
    public static final Station 잠실 = new Station(3L, "잠실");
    public static final Station 건대 = new Station(4L, "건대");
    public static final Station 성수 = new Station(5L, "성수");
    public static final Station 왕십리 = new Station(6L, "왕십리");
    public static final Station 합정 = new Station(7L, "합정");
    public static final Station 창동 = new Station(8L, "창동");
    public static final Station 당고개 = new Station(9L, "당고개");
    public static final Station 사당 = new Station(10L, "사당");

    public static final Line 이호선 = new Line(1L, "2호선", "green");
    public static final Line 분당선 = new Line(2L, "분당선", "yellow", 1000);
    public static final Line 사호선 = new Line(3L, "4호선", "blue");

    public static final Section 강남_삼성 = new Section(10L, 이호선, 강남, 삼성, 10);
    public static final Section 삼성_건대 = new Section(20L, 이호선, 삼성, 건대, 15);
    public static final Section 건대_성수 = new Section(30L, 이호선, 건대, 성수, 20);
    public static final Section 합정_왕십리 = new Section(30L, 이호선, 합정, 왕십리, 50);


    public static final Section 왕십리_합정 = new Section(40L, 분당선, 왕십리, 합정, 50);
    public static final Section 합정_성수 = new Section(50L, 분당선, 합정, 성수, 10);
    public static final Section 성수_강남 = new Section(60L, 분당선, 성수, 강남, 10);

    public static final Section 창동_당고개 = new Section(70L, 사호선, 창동, 당고개, 58);
    public static final Section 왕십리_당고개 = new Section(80L, 사호선, 왕십리, 당고개, 64);
    public static final Section 사당_당고개 = new Section(90L, 사호선, 사당, 당고개, 178);
    public static final Section 잠실_당고개 = new Section(100L, 사호선, 잠실, 당고개, 200);
}
