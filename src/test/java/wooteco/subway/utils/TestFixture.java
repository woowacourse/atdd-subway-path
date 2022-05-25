package wooteco.subway.utils;

import wooteco.subway.domain.station.Station;

@SuppressWarnings("NonAsciiCharacters")
public class TestFixture {

    private TestFixture() {
    }

    public static final Station 역삼역 = new Station(1L, "역삼역");
    public static final Station 정자역 = new Station(2L, "정자역");
    public static final Station 판교역 = new Station(3L, "판교역");
    public static final Station 서현역 = new Station(4L, "서현역");
    public static final Station 잠실역 = new Station(5L, "잠실역");
    public static final Station 미금역 = new Station(6L, "미금역");
}
