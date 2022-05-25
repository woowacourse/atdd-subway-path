package wooteco.subway.acceptance;

import wooteco.subway.controller.dto.line.LineRequest;
import wooteco.subway.controller.dto.section.SectionRequest;
import wooteco.subway.controller.dto.station.StationRequest;

public class AcceptanceFixture {

    public static final LineRequest 일호선 = new LineRequest("1호선", "bg-blue-200", 1L, 2L, 5, 100);
    public static final LineRequest 이호선 = new LineRequest("2호선", "bg-green-300", 1L, 2L, 10, 200);
    public static final LineRequest 삼호선 = new LineRequest("3호선", "bg-orange-400", 7L, 8L, 10, 300);

    public static final StationRequest 낙성대 = new StationRequest("낙성대");
    public static final StationRequest 사당 = new StationRequest("사당");
    public static final StationRequest 방배 = new StationRequest("방배");
    public static final StationRequest 서초 = new StationRequest("서초");
    public static final StationRequest 서울대입구 = new StationRequest("서울대입구");
    public static final StationRequest 봉천 = new StationRequest("봉천");
    public static final StationRequest 에덴 = new StationRequest("에덴");
    public static final StationRequest 제로 = new StationRequest("제로");

    public static final SectionRequest 낙성대_사당 = new SectionRequest(1L, 2L, 10);
    public static final SectionRequest 사당_서초 = new SectionRequest(2L, 4L, 30);
    public static final SectionRequest 사당_방배 = new SectionRequest(2L, 3L, 20);
    public static final SectionRequest 방배_서초 = new SectionRequest(3L, 4L, 10);
    public static final SectionRequest 봉천_낙성대 = new SectionRequest(6L, 1L, 50);
    public static final SectionRequest 봉천_사당 = new SectionRequest(6L, 2L, 100);

}
