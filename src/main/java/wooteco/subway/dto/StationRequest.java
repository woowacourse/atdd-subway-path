package wooteco.subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class StationRequest {

    @NotBlank(message = "지하철역 이름이 공백일 수 없습니다.")
    @Size(max = 255, message = "지하철역 이름은 255자 보다 클 수 없습니다.")
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
