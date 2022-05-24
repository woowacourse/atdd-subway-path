package wooteco.subway.domain.fixture;

import java.util.List;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class StationFixture {

    public static Station 강남 = new Station(1L, "강남");
    public static Station 역삼 = new Station(2L, "역삼");
    public static Station 선릉 = new Station(3L, "선릉");
    public static Section 강남_역삼_10 = new Section(강남, 역삼, 10);
    public static Section 역삼_선릉_10 = new Section(역삼, 선릉, 10);
    public static Section 선릉_강남_300 = new Section(선릉, 강남, 300);
    public static Line 이호선 = new Line(1L, "2호선", "green", List.of(강남_역삼_10, 역삼_선릉_10, 선릉_강남_300),
            500);
}
