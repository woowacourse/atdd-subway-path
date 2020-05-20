package wooteco.subway.admin.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 *   class description
 *
 *   @author ParkDooWon
 */
public class LineStations {
	private Set<LineStation> stations = new HashSet<>();

	public void addLineStation(LineStation lineStation) {
		stations.stream()
			.filter(lineStation1 -> LineStation.isSameId(lineStation1.getPreStationId(), lineStation.getPreStationId()))
			.findAny()
			.ifPresent(lineStation1 -> lineStation1.updatePreLineStation(lineStation.getStationId()));

		stations.add(lineStation);
	}

	public void removeLineStationById(Long stationId) {
		LineStation targetLineStation = stations.stream()
			.filter(lineStation -> LineStation.isSameId(lineStation.getStationId(), stationId))
			.findFirst()
			.orElseThrow(RuntimeException::new);

		stations.stream()
			.filter(lineStation -> LineStation.isSameId(lineStation.getPreStationId(), stationId))
			.findFirst()
			.ifPresent(lineStation -> lineStation.updatePreLineStation(targetLineStation.getPreStationId()));

		stations.remove(targetLineStation);
	}

	public List<Long> getLineStationsId() {
		if (stations.isEmpty()) {
			return new ArrayList<>();
		}

		LineStation firstLineStation = stations.stream()
			.filter(LineStation::isFirstStation)
			.findFirst()
			.orElseThrow(RuntimeException::new);

		List<Long> stationIds = new ArrayList<>();
		stationIds.add(firstLineStation.getStationId());

		while (true) {
			Long lastStationId = stationIds.get(stationIds.size() - 1);
			Optional<LineStation> nextLineStation = stations.stream()
				.filter(lineStation -> LineStation.isSameId(lineStation.getPreStationId(), lastStationId))
				.findFirst();

			if (!nextLineStation.isPresent()) {
				break;
			}

			stationIds.add(nextLineStation.get().getStationId());
		}

		return stationIds;
	}

	public Set<LineStation> getStations() {
		return stations;
	}
}
