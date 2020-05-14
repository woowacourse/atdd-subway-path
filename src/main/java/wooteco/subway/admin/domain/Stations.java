package wooteco.subway.admin.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Stations {
	private List<Station> stations;
	private Map<Long, Station> stationsCache;

	public Stations(List<Station> sourceStations) {
		this.stations = sourceStations;
		stationsCache = sourceStations.stream()
				.collect(Collectors.toMap(Station::getId, Function.identity()));
	}

	public Station findByKey(Long stationId) {
		return stationsCache.get(stationId);
	}

	public List<Station> getStations() {
		return Collections.unmodifiableList(stations);
	}
}
