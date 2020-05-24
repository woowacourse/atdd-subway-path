package wooteco.subway.admin.domain;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import wooteco.subway.admin.exception.NotFoundResourceException;

public class Stations implements Iterable<Station> {
	private static final String NOT_FOUND_STATION_ID_MESSAGE = "역의 id %d가 존재하지 않습니다.";
	private static final String NOT_FOUND_STATION_NAME_MESSAGE = "%s역이 존재하지 않습니다.";

	private final List<Station> stations;

	public Stations(List<Station> stations) {
		this.stations = Objects.requireNonNull(stations);
	}

	public Station findById(Long key) {
		return stations.stream()
			.filter(station -> station.hasSameId(key))
			.findFirst()
			.orElseThrow(() -> new NotFoundResourceException(String.format(NOT_FOUND_STATION_ID_MESSAGE, key)));
	}

	public Station findByName(String name) {
		return stations.stream()
			.filter(station -> station.hasSameName(name))
			.findFirst()
			.orElseThrow(() -> new NotFoundResourceException(String.format(NOT_FOUND_STATION_NAME_MESSAGE, name)));
	}

	@Override
	public Iterator<Station> iterator() {
		return stations.iterator();
	}
}
