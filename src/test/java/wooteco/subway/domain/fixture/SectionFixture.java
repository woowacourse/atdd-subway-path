package wooteco.subway.domain.fixture;

import static wooteco.subway.domain.fixture.StationFixture.*;

import wooteco.subway.domain.Section;

public class SectionFixture {
    public static final Section LINE1_SECTION1 = new Section(STATION_A, STATION_B, 5);
    public static final Section LINE1_SECTION2 = new Section(STATION_B, STATION_C, 15);
    public static final Section LINE1_SECTION3 = new Section(STATION_C, STATION_D, 10);

    public static final Section LINE2_SECTION1 = new Section(STATION_B, STATION_E, 4);
    public static final Section LINE2_SECTION2 = new Section(STATION_E, STATION_F, 7);
    public static final Section LINE2_SECTION3 = new Section(STATION_F, STATION_G, 4);

    public static final Section LINE3_SECTION1 = new Section(STATION_G, STATION_C, 10);
    public static final Section LINE3_SECTION2 = new Section(STATION_C, STATION_H, 15);
    public static final Section LINE3_SECTION3 = new Section(STATION_H, STATION_I, 23);

    public static final Section LINE4_SECTION1 = new Section(STATION_J, STATION_K, 10);
}
