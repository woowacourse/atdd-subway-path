package wooteco.subway.admin.domain;

import java.time.LocalTime;

public class domainTest {
	Line createLine(String name, Long id) {
		return Line.of(name, LocalTime.of(05, 30), LocalTime.of(22, 30), 5).withId(id);
	}

	Station createStation(String name, Long id) {
		return new Station(id, name);
	}

	LineStation createLineStation(Long preStationId, Long stationId) {
		return new LineStation(preStationId, stationId, 10, 10);
	}
}
