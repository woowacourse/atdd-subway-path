package wooteco.subway.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

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
	@Embedded.Empty
	private LineStations stations = new LineStations();

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

	public List<LineStation> getStationsExcludeFirst() {
		return stations.getStationsExcludeFirst();
	}

	public void addLineStation(LineStation lineStation) {
		stations.addLineStation(lineStation);
	}

	public void removeLineStationById(Long stationId) {
		stations.removeLineStationById(stationId);
	}

	public List<Long> getLineStationsId() {
		return stations.getIds();
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
		return stations.getLineStations();
	}
}
