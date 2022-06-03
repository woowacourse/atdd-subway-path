package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

public class PathFixture {

    public static final Station 강남역 = new Station(1L, "강남역");
    public static final Station 선릉역 = new Station(2L, "선릉역");
    public static final Station 잠실역 = new Station(3L, "잠실역");
    public static final Station 신촌역 = new Station(4L, "신촌역");
    public static final Station 역삼역 = new Station(5L, "역삼역");
    public static final Station 삼성역 = new Station(6L, "삼성역");
    public static final Station 신림역 = new Station(7L, "신림역");
    public static final Station 동작역 = new Station(8L, "동작역");

    public static final Section 강남_선릉 = new Section(1L, 강남역, 선릉역, 5);
    public static final Section 선릉_삼성 = new Section(1L, 선릉역, 삼성역, 5);
    public static final Section 삼성_역삼 = new Section(1L, 삼성역, 역삼역, 5);
    public static final Section 신촌_강남 = new Section(2L, 신촌역, 강남역, 5);
    public static final Section 강남_잠실 = new Section(2L, 강남역, 잠실역, 5);
    public static final Section 잠실_역삼 = new Section(2L, 잠실역, 역삼역, 5);
    public static final Section 신림_동작 = new Section(3L, 신림역, 동작역, 5);

    public static final Line 신분당선 = new Line(
            1L, "신분당선", "red", 100, new Sections(List.of(강남_선릉, 선릉_삼성, 삼성_역삼)));
    public static final Line 분당선 = new Line(
            2L, "분당선", "green", 1000, new Sections(List.of(신촌_강남, 강남_잠실, 잠실_역삼)));

    public static final Line 이호선 = new Line(
            3L, "2호선", "blue", 500, new Sections(List.of(신림_동작)));
}
