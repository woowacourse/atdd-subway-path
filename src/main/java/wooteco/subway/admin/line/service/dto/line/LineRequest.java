package wooteco.subway.admin.line.service.dto.line;

import java.time.LocalTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import wooteco.subway.admin.line.domain.line.Line;

public class LineRequest {

	@NotBlank
	private String name;

	@NotBlank
	private String color;

	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime startTime;

	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime endTime;

	@NotNull
	private Integer intervalTime;

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
