package wooteco.subway.admin.domain.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.relational.core.mapping.MappedCollection;

public class LineStations {
	@MappedCollection(idColumn = "line", keyColumn = "sequence")
	private List<LineStation> lineStations = new ArrayList<>();

	public LineStations() {
	}

	public LineStations(List<LineStation> lineStations) {
		this.lineStations = lineStations;
	}

	public void add(LineStation lineStation) {
		lineStations.stream()
			.filter(it -> Objects.equals(it.getPreStationId(), lineStation.getPreStationId()))
			.findAny()
			.ifPresent(it -> it.updatePreLineStation(lineStation.getStationId()));

		lineStations.add(lineStation);
	}

	public void remove(Long stationId) {
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

		List<Long> stationIds = new ArrayList<>(Arrays.asList(findFirstStation().getStationId()));

		Map<Long, LineStation> lineStationByPreId = lineStations.stream()
			.collect(Collectors.toMap(LineStation::getPreStationId, lineStation -> lineStation));

		while (true) {
			Long lastStationId = stationIds.get(stationIds.size() - 1);

			if (!lineStationByPreId.containsKey(lastStationId)) {
				break;
			}

			stationIds.add(lineStationByPreId.get(lastStationId).getStationId());
		}

		return stationIds;
	}

	public LineStations removeFirstEdges() {
		return lineStations.stream()
			.filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
			.collect(Collectors.collectingAndThen(Collectors.toList(), LineStations::new));
	}

	private LineStation findFirstStation() {
		return lineStations.stream()
			.filter(LineStation::isStart)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("첫 역을 찾을 수 없습니다."));
	}

	public List<LineStation> get() {
		return Collections.unmodifiableList(lineStations);
	}
}
