package wooteco.subway.admin.line.service.dto.line;

import java.time.LocalTime;

import wooteco.subway.admin.line.domain.line.Line;

public class LineRequest {

	private String name;
	private String color;
	private LocalTime startTime;
	private LocalTime endTime;
	private int intervalTime;

	public LineRequest() {
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

	public Line toLine() {
		return new Line(name, color, startTime, endTime, intervalTime);
	}
}
