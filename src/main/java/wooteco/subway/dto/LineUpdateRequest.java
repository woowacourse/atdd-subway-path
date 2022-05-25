package wooteco.subway.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class LineUpdateRequest {

    @NotBlank(message = "노선 이름은 빈 문자열일 수 없습니다.")
    @Length(max = 255, message = "노선 이름은 255자이하여야 합니다.")
    private String name;

    @NotBlank(message = "노선 색깔은 빈 문자열일 수 없습니다.")
    @Length(max = 20, message = "노선 색깔은 20자이하여야 합니다.")
    private String color;

    @Min(value = 0, message = "추가 요금은 0이상이여야 합니다.")
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
