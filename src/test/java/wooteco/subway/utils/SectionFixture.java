package wooteco.subway.utils;

import wooteco.subway.domain.Section;

public class SectionFixture {

    public static final Section LINE1_SECTION1 = new Section(StationFixture.STATION1, StationFixture.STATION2, 5);
    public static final Section LINE1_SECTION2 = new Section(StationFixture.STATION2, StationFixture.STATION3, 15);
    public static final Section LINE1_SECTION3 = new Section(StationFixture.STATION3, StationFixture.STATION4, 10);

    public static final Section LINE2_SECTION1 = new Section(StationFixture.STATION2, StationFixture.STATION5, 4);
    public static final Section LINE2_SECTION2 = new Section(StationFixture.STATION5, StationFixture.STATION6, 7);
    public static final Section LINE2_SECTION3 = new Section(StationFixture.STATION6, StationFixture.STATION7, 4);

    public static final Section LINE3_SECTION1 = new Section(StationFixture.STATION7, StationFixture.STATION3, 10);
    public static final Section LINE3_SECTION2 = new Section(StationFixture.STATION3, StationFixture.STATION8, 15);
    public static final Section LINE3_SECTION3 = new Section(StationFixture.STATION8, StationFixture.STATION9, 23);

    public static final Section LINE4_SECTION1 = new Section(StationFixture.STATION10, StationFixture.STATION11, 10);
}
