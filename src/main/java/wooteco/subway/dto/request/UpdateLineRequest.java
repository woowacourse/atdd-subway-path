package wooteco.subway.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class UpdateLineRequest {

    @NotBlank(message = "노선의 이름이 입력되지 않았습니다.")
    private String name;

    @NotBlank(message = "노선의 색상이 입력되지 않았습니다.")
    private String color;

    @Min(value = 0, message = "추가 요금은 0원 이상이어야합니다.")
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

    public void setExtraFare(int extraFare) {
        this.extraFare = extraFare;
    }

    @Override
    public String toString() {
        return "UpdateLineRequest{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", extraFare=" + extraFare +
                '}';
    }
}
