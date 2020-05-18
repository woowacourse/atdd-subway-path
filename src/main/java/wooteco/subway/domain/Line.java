package wooteco.subway.domain;

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
	private Long id;
	private String name;
	private LocalTime startTime;
	private LocalTime endTime;
	private int intervalTime;
	private String backgroundColor;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Set<LineStation> stations = new HashSet<>();

	public Line() {
	}

	public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime,
		String backgroundColor) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.intervalTime = intervalTime;
		this.backgroundColor = backgroundColor;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public Line(String name, LocalTime startTime, LocalTime endTime, int intervalTime,
		String backgroundColor) {
		this(null, name, startTime, endTime, intervalTime, backgroundColor);
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

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public Set<LineStation> getStations() {
		return stations;
	}

	public void update(Line line) {
		if (!Objects.isNull(line.name)) {
			this.name = line.name;
		}
		if (!Objects.isNull(line.startTime)) {
			this.startTime = line.startTime;
		}
		if (!Objects.isNull(line.endTime)) {
			this.endTime = line.endTime;
		}
		if (line.intervalTime != 0) {
			this.intervalTime = line.intervalTime;
		}

		this.updatedAt = LocalDateTime.now();
	}

	public void addLineStation(LineStation lineStation) {
		stations.stream()
			.filter(it -> Objects.equals(it.getPreStationId(), lineStation.getPreStationId()))
			.findAny()
			.ifPresent(it -> it.updatePreLineStation(lineStation.getStationId()));

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
			.ifPresent(it -> it.updatePreLineStation(targetLineStation.getPreStationId()));

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
