package wooteco.subway.helper;

import wooteco.subway.domain.Station;

public class TestFixtures {

    public static final Station 신당역 = new Station(1L, "신당역");
    public static final Station 동묘앞역 = new Station(2L, "동묘앞역");
    public static final Station 창신역 = new Station(3L, "창신역");
    public static final Station 보문역 = new Station(4L, "보문역");

    public static final String LINE_SIX_NAME = "6호선";
    public static final String LINE_COLOR = "bg-red-500";
    public static final int STANDARD_DISTANCE = 10;
    public static final int HALF_STANDARD_DISTANCE = 5;

    public static final int DEFAULT_AGE = 20;
    public static final int DEFAULT_FARE = 1250;
    public static final int TEEN_MAX_AGE = 18;
    public static final int TEEN_DEFAULT_FARE = 720;
    public static final int CHILD_MAX_AGE = 12;
    public static final int CHILD_DEFAULT_FARE = 450;
}
