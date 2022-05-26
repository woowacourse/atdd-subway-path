package wooteco.subway;

import java.util.List;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;

public class TestFixtures {

    public static final Station 신당역 = new Station(1L, "신당역");
    public static final Station 동묘앞역 = new Station(2L, "동묘앞역");
    public static final Station 창신역 = new Station(3L, "창신역");
    public static final Station 보문역 = new Station(4L, "보문역");
    public static final Station 선릉역 = new Station(5L, "선릉역");
    public static final Station 한티역 = new Station(6L, "한티역");

    public static final String LINE_SIX = "6호선";
    public static final String LINE_SIX_COLOR = "bg-red-500";
    public static final String LINE_TWO = "2호선";
    public static final String LINE_TWO_COLOR = "bg-green-700";
    public static final String LINE_분당 = "분당선";
    public static final String LINE_분당_COLOR = "bg-yellow-100";

    public static final List<Line> 분당선_6호선_노선 = List.of(new Line(LINE_분당, LINE_분당_COLOR), new Line(LINE_SIX, LINE_SIX_COLOR));

    public static final int STANDARD_DISTANCE = 10;
    public static final int STANDARD_FARE = 1250;
    public static final int STANDARD_EXTRA_FARE = 0;
}
