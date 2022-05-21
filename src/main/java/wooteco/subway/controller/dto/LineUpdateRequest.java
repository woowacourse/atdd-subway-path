package wooteco.subway.controller.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import wooteco.subway.domain.Line;

public class LineUpdateRequest {

    @NotNull(message = "노선 이름이 비었습니다.")
    private String name;

    @NotNull(message = "노선 색상이 비었습니다.")
    private String color;

    @Min(value = 0, message = "추가 요금 값은 음수일 수 없습니다.")
    private int extraFare;

    public LineUpdateRequest() {
    }

    public LineUpdateRequest(String name, String color, int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line toEntity(Long lineId) {
        return new Line(lineId, name, color, extraFare);
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
