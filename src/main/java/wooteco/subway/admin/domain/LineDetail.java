package wooteco.subway.admin.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class LineDetail {
	private Long id;
	private String name;
	private LocalTime startTime;
	private LocalTime endTime;
	private int intervalTime;
	private String color;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<Station> stations;


	public LineDetail(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime, String color,
					  LocalDateTime createdAt, LocalDateTime updatedAt, List<Station> stations) {
		this.id = id;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.intervalTime = intervalTime;
		this.color = color;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.stations = stations;
	}

	public static LineDetail of(Line line, List<Station> stations) {
		return new LineDetail(line.getId(), line.getName(), line.getStartTime(), line.getEndTime(),
				line.getIntervalTime(), line.getColor(), line.getCreatedAt(), line.getUpdatedAt(), stations);
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

	public String getColor() {
		return color;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public List<Station> getStations() {
		return stations;
	}
}
