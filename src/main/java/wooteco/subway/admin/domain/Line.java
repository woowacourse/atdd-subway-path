package wooteco.subway.admin.domain;

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
	@Embedded(onEmpty = Embedded.OnEmpty.USE_EMPTY)
	private LineStations stations = LineStations.ofEmpty();

	public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
		LocalDateTime createdTime = LocalDateTime.now();
		this.id = id;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.intervalTime = intervalTime;
		this.createdAt = createdTime;
		this.updatedAt = createdTime;
	}

	public void update(Line line) {
		this.name = line.getName();
		this.startTime = line.getStartTime();
		this.endTime = line.getEndTime();
		this.intervalTime = line.getIntervalTime();
		this.updatedAt = LocalDateTime.now();
	}

	public void addLineStation(LineStation lineStation) {
		stations.addLineStation(lineStation);
	}

	public void removeLineStationById(Long stationId) {
		stations.removeLineStationById(stationId);
	}

	public List<Long> getLineStationsId() {
		return stations.getLineStationsId();
	}

	public List<Station> findStationsFrom(List<Station> stations) {
		return this.stations.findStationsFrom(stations);
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

	public LineStations getStations() {
		return stations;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
