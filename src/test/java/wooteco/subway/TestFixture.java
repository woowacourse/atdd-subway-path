package wooteco.subway;

import wooteco.subway.domain.line.section.Section;
import wooteco.subway.domain.station.Station;

public class TestFixture {

    public static final Station 강남역 = new Station("강남역");
    public static final Station 역삼역 = new Station("역삼역");

    public static final long 강남역_ID = 1;
    public static final long 역삼역_ID = 2;
    public static final long 선릉역_ID = 3;
    public static final long 삼성역_ID = 4;

    public static final Section 강남_역삼 = new Section(강남역_ID, 역삼역_ID, 10);
    public static final Section 역삼_선릉 = new Section(역삼역_ID, 선릉역_ID, 10);
    public static final Section 선릉_삼성 = new Section(선릉역_ID, 삼성역_ID, 10);
}
