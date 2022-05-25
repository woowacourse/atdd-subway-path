package wooteco.subway.controller.dto.station;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "[ERROR] 역 이름은 공백일 수 없습니다.")
    @Length(max = 255, message = "[ERROR] 역 이름은 255자 이하입니다.")
    private String name;

    private StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
