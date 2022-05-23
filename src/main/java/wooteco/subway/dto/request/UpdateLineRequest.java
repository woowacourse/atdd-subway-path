package wooteco.subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import wooteco.subway.domain.Line;

public class UpdateLineRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @PositiveOrZero
    private int extraFare;

    private UpdateLineRequest() {
    }

    public UpdateLineRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public Line toLine(final Long id) {
        return new Line(id, name, color, extraFare);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
