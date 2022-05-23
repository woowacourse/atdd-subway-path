package wooteco.subway.controller.dto.line;

import org.hibernate.validator.constraints.Length;
import wooteco.subway.service.dto.line.LineRequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class LineRequest {

    @NotBlank
    @Length(max = 255)
    private String name;
    @NotBlank
    @Length(max = 20)
    private String color;
    @Positive
    private Long upStationId;
    @Positive
    private Long downStationId;
    @Positive
    private int distance;
    @PositiveOrZero
    private int extraFare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(name, color, upStationId, downStationId, distance, 0);
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

    public LineRequestDto toServiceRequest() {
        return new LineRequestDto(name, color, upStationId, downStationId, distance, extraFare);
    }
}
