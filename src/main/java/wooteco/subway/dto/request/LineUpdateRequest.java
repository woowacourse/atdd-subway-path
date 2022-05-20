package wooteco.subway.dto.request;

import javax.validation.constraints.NotBlank;

public class LineUpdateRequest {

    @NotBlank(message = "지하철 노선의 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "지하철 노선의 색상을 선택해주세요.")
    private String color;

    private LineUpdateRequest() {
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
