package wooteco.subway;

import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.vo.LineId;
import wooteco.subway.domain.vo.SectionDistance;
import wooteco.subway.domain.vo.SectionId;
import wooteco.subway.domain.vo.StationId;
import wooteco.subway.domain.vo.StationName;
import wooteco.subway.ui.dto.request.LineRequest;
import wooteco.subway.ui.dto.request.StationRequest;

public class SubwayFixtures {

    public static final Station STATION_FIXTURE1 = new Station(StationId.from(1L), StationName.from("선릉역"));
    public static final Station STATION_FIXTURE2 = new Station(StationId.from(2L), StationName.from("대림역"));
    public static final Station 강남역 = new Station(StationId.from(1L), StationName.from("강남역"));
    public static final Station 역삼역 = new Station(StationId.from(2L), StationName.from("역삼역"));
    public static final Station 선릉역 = new Station(StationId.from(3L), StationName.from("선릉역"));
    public static final Station 삼성역 = new Station(StationId.from(4L), StationName.from("삼성역"));
    public static final Station 성담빌딩 = new Station(StationId.from(5L), StationName.from("성담빌딩"));
    public static final Station 서초역 = new Station(StationId.from(6L), StationName.from("서초역"));
    public static final Station 대림역 = new Station(StationId.from(7L), StationName.from("대림역"));
    public static final Station 청담역 = new Station(StationId.from(8L), StationName.from("청담역"));
    public static final Station 압구정역 = new Station(StationId.from(9L), StationName.from("압구정역"));

    public static Section 역삼에서_선릉_구간 = new Section(SectionId.from(1L), LineId.from(2L), 선릉역, 역삼역,
            SectionDistance.from(10L));
    public static Section 강남에서_역삼_구간 = new Section(SectionId.from(2L), LineId.from(2L), 역삼역, 강남역,
            SectionDistance.from(10L));
    public static final Section 서초에서_강남_구간 = new Section(SectionId.from(6L), LineId.from(2L), 강남역, 서초역,
            SectionDistance.from(10L));
    public static final Section 대림에서_서초_구간 = new Section(SectionId.from(7L), LineId.from(2L), 서초역, 대림역,
            SectionDistance.from(10L));
    public static final Section 선릉에서_성담빌딩_구간 = new Section(SectionId.from(8L), LineId.from(2L), 성담빌딩, 선릉역,
            SectionDistance.from(10L));

    public static final StationRequest 강남역_요청 = new StationRequest("강남역");
    public static final StationRequest 역삼역_요청 = new StationRequest("역삼역");
    public static final LineRequest 신분당선_요청 = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L, 10000L);
    public static final LineRequest 이호선_요청 = new LineRequest("2호선", "bg-green-600", 2L, 3L, 10L, 20000L);

    public static final String STATIONS_URI = "/stations";
    public static final String LINES_URI = "/lines";
    public static final String SECOND_LINE_SECTIONS_URI = "/lines/1/sections";
}
