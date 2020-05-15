package wooteco.subway.admin.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Stations {
	private Map<Long, Station> stationsCache;

	public Stations(List<Station> sourceStations) {
		stationsCache = sourceStations.stream()
				.collect(Collectors.toMap(Station::getId, Function.identity()));
	}

	public Station findByKey(Long stationId) {
		return stationsCache.get(stationId);
	}

	public List<Station> getStations() {
		return new ArrayList<>(stationsCache.values());
	}
}
