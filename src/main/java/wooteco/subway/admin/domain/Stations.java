package wooteco.subway.admin.domain;

import java.util.List;

import wooteco.subway.admin.exception.NonExistentDataException;

/**
 *   class description
 *
 *   @author ParkDooWon
 */
public class Stations {
	private List<Station> stations;

	private Stations(List<Station> stations) {
		this.stations = stations;
	}

	public static Stations of(List<Station> stations) {
		return new Stations(stations);
	}

	public Station findById(Long id) {
		return stations.stream()
			.filter(station -> station.isSameId(id))
			.findFirst()
			.orElseThrow(() -> new NonExistentDataException("존재하지 않는 역입니다."));
	}

	public List<Station> getStations() {
		return stations;
	}
}
