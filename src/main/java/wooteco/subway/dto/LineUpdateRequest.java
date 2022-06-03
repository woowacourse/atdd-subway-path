package wooteco.subway.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineUpdateRequest {

    @NotBlank(message = "이름이 공백일 수 없습니다")
    private String name;

    @NotBlank(message = "색깔을 선택해야 합니다")
    private String color;

    @NotNull
    @Min(value = 0, message = "추가요금은 0 이상이여야 합니다.")
    private int extraFare;

    public LineUpdateRequest() {
    }

    public LineUpdateRequest(String name, String color, int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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
