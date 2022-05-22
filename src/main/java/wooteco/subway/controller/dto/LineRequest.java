package wooteco.subway.controller.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Range;

import wooteco.subway.domain.Line;

public class LineRequest {

	@NotBlank
	private String name;
	@NotBlank
	private String color;
	@NotNull
	private Long upStationId;
	@NotNull
	private Long downStationId;
	@Positive
	private int distance;
	@Min(value = 0)
	private int extraFare;

	private LineRequest() {
	}

	public LineRequest(
		String name, String color,
		Long upStationId, Long downStationId,
		int distance, int extraFare) {
		this.name = name;
		this.color = color;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
		this.extraFare = extraFare;
	}

	public Line toEntity(Long id) {
		return new Line(id, name, color, extraFare);
	}

	public SectionRequest toSectionRequest() {
		return new SectionRequest(upStationId, downStationId, distance);
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public Long getUpStationId() {
		return upStationId;
	}

	public Long getDownStationId() {
		return downStationId;
	}

	public int getDistance() {
		return distance;
	}

	public int getExtraFare() {
		return extraFare;
	}
}
