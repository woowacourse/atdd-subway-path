package wooteco.subway;

import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class Fixtures {

    public static final String STATION_1 = "첫번째역";
    public static final String STATION_2 = "두번째역";
    public static final String STATION_3 = "세번째역";
    public static final String STATION_4 = "네번째역";

    public static final String LINE_1 = "1호선";
    public static final String LINE_2 = "2호선";

    public static final String RED = "bg-red-600";
    public static final String BLUE = "bg-blue-500";

    public static final Section SECTION_1_2 = new Section(new Station(1L, STATION_1), new Station(2L, STATION_2), 10);
    public static final Section SECTION_2_3 = new Section(new Station(2L, STATION_2), new Station(3L, STATION_3), 10);
    public static final Section SECTION_3_4 = new Section(new Station(3L, STATION_3), new Station(4L, STATION_4), 10);
    public static final Section SECTION_1_3 = new Section(new Station(1L, STATION_1), new Station(3L, STATION_3), 10);

    public static final Section SECTION_1_2_SHORT = new Section(new Station(1L, STATION_1), new Station(2L, STATION_2), 5);
    public static final Section SECTION_2_3_SHORT = new Section(new Station(2L, STATION_2), new Station(3L, STATION_3), 5);

    public static Station UP = new Station("U");
    public static Station LEFT = new Station("L");
    public static Station CENTER = new Station("C");
    public static Station RIGHT = new Station("R");
    public static Station DOWN = new Station("D");

}
