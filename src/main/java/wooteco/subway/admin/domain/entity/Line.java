package wooteco.subway.admin.domain.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

public class Line {
	@Id
	private Long id;
	private String name;
	private LocalTime startTime;
	private LocalTime endTime;
	private int intervalTime;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@Embedded.Nullable
	private LineStations lineStations = new LineStations();

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

	public static LineStations toLineStations(List<Line> allLines) {
		// todo : start가 없는걸로 보내줘야함.
		return allLines.stream()
			.map(Line::getLineStations)
			.map(LineStations::removeFirstStations)
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("Linestations을 찾을 수 없습니다."));
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

	public LineStations getLineStations() {
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
		lineStations.add(lineStation);
	}

	public void removeLineStationById(Long stationId) {
		lineStations.remove(stationId);

	}

	public List<Long> getLineStationsId() {
		return lineStations.getLineStationsId();
	}
}
