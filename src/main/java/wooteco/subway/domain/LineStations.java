package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LineStations {
	private Set<LineStation> lineStations;

	LineStations() {
		lineStations = new HashSet<>();
	}

	List<LineStation> getStationsExcludeFirst() {
		return lineStations.stream()
			.filter(LineStation::isNotFirstStation)
			.collect(Collectors.toList());
	}

	public void addLineStation(LineStation lineStation) {
		lineStations.stream()
			.filter(it -> Objects.equals(it.getPreStationId(), lineStation.getPreStationId()))
			.findAny()
			.ifPresent(it -> it.updatePreLineStation(lineStation.getStationId()));

		lineStations.add(lineStation);
	}

	void removeLineStationById(Long stationId) {
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

	private LineStation getFirstNode() {
		return lineStations.stream()
			.filter(LineStation::isFirstStation)
			.findFirst()
			.orElseThrow(AssertionError::new);
	}

	private Optional<LineStation> getNextNodeOf(LineStation station) {
		return lineStations.stream()
			.filter(station::isPreStationOf)
			.findFirst();
	}

	public List<Long> getIds() {
		List<Long> sortedStationsId = new ArrayList<>();
		if (lineStations.isEmpty()) {
			return sortedStationsId;
		}

		LineStation preNode = getFirstNode();
		sortedStationsId.add(preNode.getStationId());

		while (getNextNodeOf(preNode).isPresent()) {
			LineStation currentNode = getNextNodeOf(preNode).get();
			sortedStationsId.add(currentNode.getStationId());
			preNode = currentNode;
		}

		return sortedStationsId;
	}

	public Set<LineStation> getLineStations() {
		return lineStations;
	}
}
