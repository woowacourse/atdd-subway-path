package wooteco.subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdateLineRequest {

    @NotBlank(message = "노선의 이름이 입력되지 않았습니다.")
    private String name;

    @NotBlank(message = "노선의 색상이 입력되지 않았습니다.")
    private String color;

    @NotNull(message = "노선의 추가 금액이 입력되지 않았습니다.")
    private int extraFare;

    public UpdateLineRequest() {
    }

    public UpdateLineRequest(String name, String color, int extraFare) {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "UpdateLineRequest{" + "name='" + name + '\'' + ", color='" + color + '\'' + '}';
    }
}
