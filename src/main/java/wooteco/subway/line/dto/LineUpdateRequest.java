package wooteco.subway.line.dto;

import javax.validation.constraints.NotBlank;

public class LineUpdateRequest {

    @NotBlank(message = "이름이 비어있거나 공백입니다.")
    private String name;

    @NotBlank(message = "색깔이 비어있거나 공백입니다.")
    private String color;

    public LineUpdateRequest() {
    }

    public LineUpdateRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
