package wooteco.subway.admin.dto;

import java.time.LocalTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.format.annotation.DateTimeFormat;

import wooteco.subway.admin.domain.Line;

public class LineRequest {
	@NotBlank(message = "노선명을 입력해주세요.")
	private String name;

	@NotNull(message = "출발시간을 입력해주세요.")
	@DateTimeFormat(pattern = "HH:mm:ss")
	private LocalTime startTime;

	@NotNull(message = "도착시간을 입력해주세요.")
	@DateTimeFormat(pattern = "HH:mm:ss")
	private LocalTime endTime;

	@Positive(message = "배차 간격에 양수를 입력해주세요.")
	private int intervalTime;

	private LineRequest() {
	}

	public Line toLine() {
		return new Line(name, startTime, endTime, intervalTime);
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
}
