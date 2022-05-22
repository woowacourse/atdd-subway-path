package wooteco.subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class LineUpdateRequest {

    @NotBlank(message = "노선 이름은 빈 값일 수 없습니다.")
    private final String name;
    @NotBlank(message = "노선 색상은 빈 값일 수 없습니다.")
    private final String color;
    @PositiveOrZero(message = "추가 요금은 음수일 수 없습니다.")
    private final int extraFare;


    public LineUpdateRequest() {
        this(null, null, 0);
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
