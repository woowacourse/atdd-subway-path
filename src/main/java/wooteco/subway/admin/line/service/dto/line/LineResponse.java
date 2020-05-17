package wooteco.subway.admin.line.service.dto.line;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.admin.line.domain.line.Line;

public class LineResponse {

	private Long id;
	private String name;
	private String color;
	private LocalTime startTime;
	private LocalTime endTime;
	private int intervalTime;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public LineResponse() {
	}

	public LineResponse(Long id, String name, String color, LocalTime startTime, LocalTime endTime,
		int intervalTime, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.startTime = startTime;
		this.endTime = endTime;
		this.intervalTime = intervalTime;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static LineResponse of(Line line) {
		return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getStartTime(),
			line.getEndTime(), line.getIntervalTime(), line.getCreatedAt(), line.getUpdatedAt());
	}

	public static List<LineResponse> listOf(List<Line> lines) {
		return lines.stream()
			.map(LineResponse::of)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

}
