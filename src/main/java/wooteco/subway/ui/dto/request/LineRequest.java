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
    private Long distance;
    @PositiveOrZero
    private long extraFare;

    private LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, Long distance, long extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, Long distance) {
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

    public Long getDistance() {
        return distance;
    }

    public long getExtraFare() {
        return extraFare;
    }
}
