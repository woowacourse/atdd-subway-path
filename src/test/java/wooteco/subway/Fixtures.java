package wooteco.subway;

import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class Fixtures {

    public static final String HYEHWA = "혜화역";
    public static final String SUNGSHIN = "성신여대입구역";
    public static final String GANGNAM = "강남역";
    public static final String JAMSIL = "잠실역";

    public static final String LINE_2 = "2호선";
    public static final String LINE_4 = "4호선";

    public static final String SKY_BLUE = "bg-sky-600";
    public static final String GREEN = "bg-green-500";

    public static final Section SECTION_1_2_10 = new Section(new Station(1L, HYEHWA), new Station(2L, SUNGSHIN), 10);
    public static final Section SECTION_2_3_10 = new Section(new Station(2L, SUNGSHIN), new Station(3L, GANGNAM), 10);
    public static final Section SECTION_3_4_10 = new Section(new Station(3L, GANGNAM), new Station(4L, JAMSIL), 10);
    public static final Section SECTION_1_3_10 = new Section(new Station(1L, HYEHWA), new Station(3L, GANGNAM), 10);

    public static final Section SECTION_1_2_5 = new Section(new Station(1L, HYEHWA), new Station(2L, SUNGSHIN), 5);
    public static final Section SECTION_2_3_5 = new Section(new Station(2L, SUNGSHIN), new Station(3L, GANGNAM), 5);

    public static Station UP = new Station("U");
    public static Station LEFT = new Station("L");
    public static Station CENTER = new Station("C");
    public static Station RIGHT = new Station("R");
    public static Station DOWN = new Station("D");

}
