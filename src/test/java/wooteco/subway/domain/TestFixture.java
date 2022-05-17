package wooteco.subway.domain;

import java.util.List;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class TestFixture {

    public static final Station 교대역 = new Station(2L, "교대역");
    public static final Station 강남역 = new Station(1L, "강남역");
    public static final Station 역삼역 = new Station(4L, "역삼역");
    public static final Station 선릉역 = new Station(3L, "선릉역");
    public static final Station 광교역 = new Station(5L, "광교역");
    public static final List<Station> STATIONS = List.of(교대역, 역삼역, 강남역, 선릉역, 광교역);
    public static final Section 교대_강남 = new Section(1L, 교대역, 강남역, 5);
    public static final Section 강남_역삼 = new Section(2L, 강남역, 역삼역, 6);
    public static final Section 역삼_선릉 = new Section(3L, 역삼역, 선릉역, 7);
    public static final List<Section> SECTIONS = List.of(교대_강남, 강남_역삼, 역삼_선릉);
}
