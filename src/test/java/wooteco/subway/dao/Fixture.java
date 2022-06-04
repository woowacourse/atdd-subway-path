package wooteco.subway.dao;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Fare;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.domain.section.Section;

class Fixture {
    final static Station TERMINATION_UP = new Station(1L, "상행종점역");
    final static Station TERMINATION_DOWN = new Station(2L, "하행종점역");
    final static Section SECTION_TERMINATIONS = new Section(TERMINATION_UP, TERMINATION_DOWN, Distance.fromMeter(10));
    final static Line LINE_신분당선 = new Line("신분당선", "bg-red-600", SECTION_TERMINATIONS);
    final static Fare FARE_1000 = new Fare(1000);
}
