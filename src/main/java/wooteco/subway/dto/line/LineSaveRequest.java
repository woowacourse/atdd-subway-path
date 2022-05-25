package wooteco.subway.dto.line;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import wooteco.subway.domain.Line;

public class LineSaveRequest {

    @NotBlank(message = "{name.notBlank}")
    @Size(max = 10, message = "{name.tooLong}")
    private String name;

    @NotBlank(message = "{color.notBlank}")
    @Size(max = 255, message = "{string.tooLong}")
    private String color;

    private Long upStationId;

    private Long downStationId;

    @Positive(message = "{number.positive}")
    private int distance;

    @PositiveOrZero(message = "{extraFare.positiveOrZero}")
    private int extraFare;

    public LineSaveRequest() {
    }

    public LineSaveRequest(final String name, final String color, final Long upStationId, final Long downStationId,
                           final int distance, final int extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public LineSaveRequest(final String name, final String color, final Long upStationId, final Long downStationId,
                           final int distance) {
        this(name, color, upStationId, downStationId, distance, 0);
    }

    public Line toLine() {
        return new Line(name, color, extraFare);
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
