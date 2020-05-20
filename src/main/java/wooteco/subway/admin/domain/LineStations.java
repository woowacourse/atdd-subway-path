package wooteco.subway.admin.domain;

import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.*;

public class LineStations {
	@MappedCollection(idColumn = "line", keyColumn = "line_key")
	private List<LineStation> lineStations;

	private LineStations(List<LineStation> lineStations) {
		this.lineStations = lineStations;
	}

	public static LineStations getInstance() {
		return new LineStations(new LinkedList<>());
	}

	public void addLineStation(LineStation lineStation) {
		lineStations.stream()
				.filter(it -> Objects.equals(it.getPreStationId(), lineStation.getPreStationId()))
				.findAny()
				.ifPresent(it -> it.updatePreLineStation(lineStation.getStationId()));

		lineStations.add(lineStation);
	}

	public void removeLineStationById(Long stationId) {
		LineStation targetLineStation = lineStations.stream()
				.filter(it -> Objects.equals(it.getStationId(), stationId))
				.findFirst()
				.orElseThrow(RuntimeException::new);

		lineStations.stream()
				.filter(it -> Objects.equals(it.getPreStationId(), stationId))
				.findFirst()
				.ifPresent(it -> it.updatePreLineStation(targetLineStation.getPreStationId()));

		lineStations.remove(targetLineStation);
	}

	public List<Long> getLineStationsId() {
		if (lineStations.isEmpty()) {
			return new ArrayList<>();
		}

		LineStation firstLineStation = lineStations.stream()
				.filter(it -> it.getPreStationId() == null)
				.findFirst()
				.orElseThrow(RuntimeException::new);

		List<Long> stationIds = new ArrayList<>();
		stationIds.add(firstLineStation.getStationId());

		while (true) {
			Long lastStationId = stationIds.get(stationIds.size() - 1);
			Optional<LineStation> nextLineStation = lineStations.stream()
					.filter(it -> Objects.equals(it.getPreStationId(), lastStationId))
					.findFirst();

			if (!nextLineStation.isPresent()) {
				break;
			}

			stationIds.add(nextLineStation.get().getStationId());
		}

		return stationIds;
	}

	public List<LineStation> getLineStations() {
		return lineStations;
	}
}
