package wooteco.subway.domain;

public class domainTestFixture {
    private static Station station1 = new Station(1L, "1");
    private static Station station2 = new Station(2L, "2");
    private static Station station3 = new Station(3L, "3");
    private static Station station4 = new Station(4L, "4");
    private static Station station5 = new Station(5L, "5");
    private static Station station6 = new Station(6L, "6");


    public static Section section1to2 = new Section(station1, station2, 10);
    public static Section section2to3 = new Section(station2, station3, 10);
    public static Section section1to3 = new Section(station1, station3, 20);
    public static Section section2to6 = new Section(station2, station6, 10);
    public static Section section5to4 = new Section(station5, station4, 10);
}
