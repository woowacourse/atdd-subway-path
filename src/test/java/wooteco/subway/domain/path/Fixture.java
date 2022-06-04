package wooteco.subway.domain.path;

import java.util.Map;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.domain.section.Section;

public class Fixture {
    static final Station 강남 = new Station(1L, "강남");
    static final Station 역삼 = new Station(2L, "역삼");
    static final Station 선릉 = new Station(3L, "선릉");
    static final Map<Section, Fare> 강남_역삼_선릉 = Map.of(
            new Section(강남, 역삼, Distance.fromMeter(10)), new Fare(100),
            new Section(역삼, 선릉, Distance.fromMeter(10)), new Fare(200),
            new Section(선릉, 강남, Distance.fromMeter(300)), new Fare(0)
    );
}
