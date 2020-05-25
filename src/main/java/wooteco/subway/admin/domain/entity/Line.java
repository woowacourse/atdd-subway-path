package wooteco.subway.admin.domain.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

public class Line {
	@Id
	private Long id;
	private String name;
	private LocalTime startTime;
	private LocalTime endTime;
	private int intervalTime;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	@MappedCollection(idColumn = "line", keyColumn = "sequence")
	private List<LineStation> lineStations = new ArrayList<>();

	public Line() {
	}

	public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.intervalTime = intervalTime;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public Line(String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
		this(null, name, startTime, endTime, intervalTime);
	}

	public static List<LineStation> toLineStations(List<Line> allLines) {
		List<LineStation> lineStations = new ArrayList<>();
		for (Line line : allLines) {
			for (LineStation lineStation : line.getLineStations()) {
				if (lineStation.isStart()) {
					continue;
				}
				lineStations.add(lineStation);
			}
		}
		return lineStations;
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

	public List<LineStation> getLineStations() {
		return lineStations;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void update(Line line) {
		if (line.getName() != null) {
			this.name = line.getName();
		}
		if (line.getStartTime() != null) {
			this.startTime = line.getStartTime();
		}
		if (line.getEndTime() != null) {
			this.endTime = line.getEndTime();
		}
		if (line.getIntervalTime() != 0) {
			this.intervalTime = line.getIntervalTime();
		}

		this.updatedAt = LocalDateTime.now();
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
			.filter(it -> it.isSameStation(stationId))
			.findFirst()
			.orElseThrow(RuntimeException::new);

		lineStations.stream()
			.filter(it -> it.isPreStation(stationId)) // todo :
			.findFirst()
			.ifPresent(it -> it.updatePreLineStation(targetLineStation.getPreStationId()));

		lineStations.remove(targetLineStation);
	}

	public List<Long> getLineStationsId() {
		if (lineStations.isEmpty()) {
			return new ArrayList<>();
		}

		LineStation firstLineStation = lineStations.stream()
			.filter(station -> station.isStart())
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("출발역을 찾을 수 없습니다."));

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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Line line = (Line)o;
		return Objects.equals(id, line.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
