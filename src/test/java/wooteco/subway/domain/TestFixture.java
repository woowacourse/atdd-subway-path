package wooteco.subway.domain;

import java.util.List;

import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class TestFixture {

    public static final Station 강남역 = new Station(1L, "강남역");
    public static final Station 역삼역 = new Station(2L, "역삼역");
    public static final Station 선릉역 = new Station(3L, "선릉역");
    public static final Station 삼성역 = new Station(4L, "삼성역");
    public static final Station 광교역 = new Station(5L, "광교역");
    public static final List<Station> STATIONS = List.of(강남역, 역삼역, 선릉역, 삼성역, 광교역);

    public static final Section 강남_역삼 = new Section(1L, 강남역, 역삼역, 6);
    public static final Section 역삼_선릉 = new Section(2L, 역삼역, 선릉역, 7);
    public static final Section 선릉_삼성 = new Section(3L, 선릉역, 삼성역, 8);
    public static final List<Section> 강남_역삼_선릉_삼성 = List.of(강남_역삼, 역삼_선릉, 선릉_삼성);
    public static final int 강남_선릉_거리 = 13;
}
