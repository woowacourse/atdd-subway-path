package wooteco.subway.service.dto.request;

import wooteco.subway.domain.Line;

import javax.validation.constraints.NotBlank;

public class LineUpdateRequest {

    @NotBlank(message = "지하철 노선의 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "지하철 노선의 색상을 선택해주세요.")
    private String color;

    private int extraFare;

    private LineUpdateRequest() {
    }

    public LineUpdateRequest(String name, String color, int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line toLine() {
        return new Line(getName(), getColor(), getExtraFare());
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
