package wooteco.subway.admin.domain;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import wooteco.subway.admin.exception.NoSuchStationException;

public class Stations implements Iterable<Station> {
	private final List<Station> stations;

	public Stations(List<Station> stations) {
		this.stations = Objects.requireNonNull(stations);
	}

	public Station findById(Long key) {
		return stations.stream()
			.filter(station -> station.hasSameId(key))
			.findFirst()
			.orElseThrow(NoSuchStationException::new);
	}

	public Station findByName(String name) {
		return stations.stream()
			.filter(station -> station.hasSameName(name))
			.findFirst()
			.orElseThrow(NoSuchStationException::new);
	}

	@Override
	public Iterator<Station> iterator() {
		return stations.iterator();
	}
}
