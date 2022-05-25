package wooteco.subway.dto.line;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public class LineUpdateRequest {

    @NotBlank(message = "{name.notBlank}")
    @Size(max = 10, message = "{name.tooLong}")
    private String name;

    @NotBlank(message = "{color.notBlank}")
    @Size(max = 255, message = "{string.tooLong}")
    private String color;

    @PositiveOrZero(message = "{extraFare.positiveOrZero}")
    private int extraFare;

    public LineUpdateRequest() {
    }

    public LineUpdateRequest(final String name, final String color, final int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public LineUpdateRequest(String name, String color) {
        this(name, color, 0);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
