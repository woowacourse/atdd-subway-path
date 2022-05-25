package wooteco.subway;

import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.vo.StationName;
import wooteco.subway.ui.dto.request.LineRequest;
import wooteco.subway.ui.dto.request.StationRequest;

public class SubwayFixtures {

    public static final Station STATION_FIXTURE1 = new Station(1L, StationName.from("선릉역"));
    public static final Station STATION_FIXTURE2 = new Station(2L, StationName.from("대림역"));
    public static final Station 강남역 = new Station(1L, StationName.from("강남역"));
    public static final Station 역삼역 = new Station(2L, StationName.from("역삼역"));
    public static final Station 선릉역 = new Station(3L, StationName.from("선릉역"));
    public static final Station 삼성역 = new Station(4L, StationName.from("삼성역"));
    public static final Station 성담빌딩 = new Station(5L, StationName.from("성담빌딩"));
    public static final Station 서초역 = new Station(6L, StationName.from("서초역"));
    public static final Station 대림역 = new Station(7L, StationName.from("대림역"));
    public static final Station 청담역 = new Station(8L, StationName.from("청담역"));
    public static final Station 압구정역 = new Station(9L, StationName.from("압구정역"));

    public static Section YEOKSAM_TO_SUNNEUNG = new Section(1L, 2L, 선릉역, 역삼역, 10L);
    public static Section GANGNAM_TO_YEOKSAM = new Section(2L, 2L, 역삼역, 강남역, 10L);
    public static final Section SEOCHO_TO_GANGNAM = new Section(6L, 2L, 강남역, 서초역, 10L);
    public static final Section DAELIM_TO_SEOCHO = new Section(7L, 2L, 서초역, 대림역, 10L);
    public static final Section SUNNEUNG_TO_SUNGDAM = new Section(8L, 2L, 성담빌딩, 선릉역, 10L);

    public static final StationRequest GANGNAM_REQUEST = new StationRequest("강남역");
    public static final StationRequest YEOKSAM_REQUEST = new StationRequest("역삼역");
    public static final LineRequest BOONDANGLINE_REQUEST = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L, 10000L);
    public static final LineRequest SECONDLINE_REQUEST = new LineRequest("2호선", "bg-green-600", 2L, 3L, 10L, 20000L);

    public static final String STATIONS_URI = "/stations";
    public static final String LINES_URI = "/lines";
    public static final String SECOND_LINE_SECTIONS_URI = "/lines/1/sections";
}
