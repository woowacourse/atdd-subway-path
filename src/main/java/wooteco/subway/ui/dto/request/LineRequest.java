package wooteco.subway.ui.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

public class LineRequest {

    @NotBlank
    @Length(min = 1, max = 255)
    private String name;
    @NotBlank
    @Length(min = 1, max = 20)
    private String color;
    @Positive
    private Long upStationId;
    @Positive
    private Long downStationId;
    @Positive
    private int distance;
    @PositiveOrZero
    private Long extraFare;

    private LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, Long extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(name, color, upStationId, downStationId, distance, null);
    }

    public LineRequest(String name, String color) {
        this(name, color, null, null, 0);
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

    public Long getExtraFare() {
        return extraFare;
    }
}
