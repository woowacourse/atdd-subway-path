package wooteco.subway.dto;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class StationRequest {

    @NotBlank(message = "지하철 역 이름은 빈 문자열일 수 없습니다.")
    @Length(max = 255, message = "지하철 역 이름은 255자이하여야 합니다.")
    private String name;

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
