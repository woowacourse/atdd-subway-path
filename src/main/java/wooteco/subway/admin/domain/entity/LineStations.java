package wooteco.subway.admin.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

	public List<LineStation> get() {
		return lineStations;
	}

	public LineStations removeFirstStations() {
		return lineStations.stream()
			.filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
			.collect(Collectors.collectingAndThen(Collectors.toList(), LineStations::new));
	}
}
