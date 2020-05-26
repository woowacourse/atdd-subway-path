package wooteco.subway.admin.domain;

import org.springframework.data.relational.core.mapping.MappedCollection;
import wooteco.subway.admin.exception.NoLineStationExistsException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class LineStations {
	@MappedCollection(idColumn = "line", keyColumn = "line_key")
	private List<LineStation> lineStations;

	private LineStations(List<LineStation> lineStations) {
		this.lineStations = lineStations;
	}

	public static LineStations getInstance() {
		return new LineStations(new LinkedList<>());
	}

	public void addLineStation(LineStation inputLineStation) {
		lineStations.stream()
				.filter(lineStation -> lineStation.hasEqualPreStationID(inputLineStation))
				.findAny()
				.ifPresent(lineStation -> lineStation.updatePreLineStation(inputLineStation.getStationId()));

		lineStations.add(inputLineStation);
	}

	public void removeLineStationById(Long stationId) {
		LineStation targetLineStation = lineStations.stream()
				.filter(lineStation -> lineStation.hasEqualStationID(stationId))
				.findFirst()
				.orElseThrow(NoLineStationExistsException::new);

		lineStations.stream()
				.filter(lineStation -> lineStation.hasEqualPreStationID(stationId))
				.findFirst()
				.ifPresent(lineStation -> lineStation.updatePreLineStation(targetLineStation.getPreStationId()));

		lineStations.remove(targetLineStation);
	}

	public List<Long> getLineStationsId() {
		if (lineStations.isEmpty()) {
			return new ArrayList<>();
		}

		List<Long> stationIds = addFirstLineStationId();
		addRestLineStationIds(stationIds);

		return stationIds;
	}

	private LineStation findFirstLineStation() {
		return lineStations.stream()
				.filter(LineStation::isFirstLineStation)
				.findFirst()
				.orElseThrow(NoLineStationExistsException::new);
	}

	private List<Long> addFirstLineStationId() {
		LineStation firstLineStation = findFirstLineStation();

		List<Long> stationIds = new ArrayList<>();
		stationIds.add(firstLineStation.getStationId());
		return stationIds;
	}

	private void addRestLineStationIds(List<Long> stationIds) {
		while (true) {
			Long lastStationId = stationIds.get(stationIds.size() - 1);
			Optional<LineStation> nextLineStation = findLineStation(lastStationId);

			if (!nextLineStation.isPresent()) {
				break;
			}

			stationIds.add(nextLineStation.get().getStationId());
		}
	}

	private Optional<LineStation> findLineStation(Long lastStationId) {
		return lineStations.stream()
				.filter(lineStation -> lineStation.hasEqualPreStationID(lastStationId))
				.findFirst();
	}

	public List<LineStation> getLineStations() {
		return lineStations;
	}
}
