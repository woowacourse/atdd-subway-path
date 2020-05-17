package wooteco.subway.admin.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.annotation.Id;

public class Line {
	@Id
	private final Long id;
	private final String name;
	private final LocalTime startTime;
	private final LocalTime endTime;
	private final int intervalTime;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;
	private final Set<LineStation> stations;

	Line(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime,
		LocalDateTime createdAt, LocalDateTime updatedAt, Set<LineStation> stations) {
		this.id = id;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.intervalTime = intervalTime;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.stations = stations;
	}

	public static Line of(String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
		LocalDateTime localDateTime = LocalDateTime.now();

		return new Line(null, name, startTime, endTime, intervalTime, localDateTime, localDateTime
			, new HashSet<>());
	}

	public Line withId(Long id) {
		return new Line(id, this.name, this.startTime, this.endTime, this.intervalTime, this.createdAt,
			this.updatedAt, this.stations);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public int getIntervalTime() {
		return intervalTime;
	}

	public Set<LineStation> getStations() {
		return stations;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public Line update(Line line) {
		return new Line(line.id, line.name, line.startTime, line.endTime, line.intervalTime,
			line.createdAt, line.updatedAt, line.stations);
	}

	public void addLineStation(LineStation lineStation) {
		stations.stream()
			.filter(it -> Objects.equals(it.getPreStationId(), lineStation.getPreStationId()))
			.findAny()
			.ifPresent(it -> {
				LineStation station = it.updatePreLineStation(lineStation.getStationId());
				stations.remove(it);
				stations.add(station);
			});

		stations.add(lineStation);
	}

	public void removeLineStationById(Long stationId) {
		LineStation targetLineStation = stations.stream()
			.filter(it -> Objects.equals(it.getStationId(), stationId))
			.findFirst()
			.orElseThrow(RuntimeException::new);

		stations.stream()
			.filter(it -> Objects.equals(it.getPreStationId(), stationId))
			.findFirst()
			.ifPresent(it -> {
				LineStation station = it.updatePreLineStation(targetLineStation.getStationId());
				stations.remove(it);
				stations.add(station);
			});

		stations.remove(targetLineStation);
	}

	public List<Long> getLineStationsId() {
		if (stations.isEmpty()) {
			return new ArrayList<>();
		}

		LineStation firstLineStation = stations.stream()
			.filter(it -> it.getPreStationId() == null)
			.findFirst()
			.orElseThrow(RuntimeException::new);

		List<Long> stationIds = new ArrayList<>();
		stationIds.add(firstLineStation.getStationId());

		while (true) {
			Long lastStationId = stationIds.get(stationIds.size() - 1);
			Optional<LineStation> nextLineStation = stations.stream()
				.filter(it -> Objects.equals(it.getPreStationId(), lastStationId))
				.findFirst();

			if (!nextLineStation.isPresent()) {
				break;
			}

			stationIds.add(nextLineStation.get().getStationId());
		}

		return stationIds;
	}
}
