package wooteco.subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineBasicRequest {

    @NotBlank(message = "이름이 공백일 수 없습니다")
    private String name;

    @NotBlank(message = "색깔을 선택해야 합니다")
    private String color;

    @NotNull(message = "추가 요금을 입력해야 합니다.")
    private int extraFare;

    public LineBasicRequest() {
    }

    public LineBasicRequest(String name, String color, int extraFare) {
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
