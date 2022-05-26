package wooteco.subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import wooteco.subway.domain.Line;

public class UpdateLineRequest {

    @NotBlank(message = "이름은 무조건 입력해야 합니다.")
    private String name;
    @NotBlank(message = "색상은 무조건 입력해야 합니다.")
    private String color;
    @NotNull(message = "추가 금액은 무조건 입력해야 합니다.")
    private Integer extraFare;

    private UpdateLineRequest() {
    }

    public UpdateLineRequest(final String name, final String color, final int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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

    public int getExtraFare() {
        return extraFare;
    }
}
