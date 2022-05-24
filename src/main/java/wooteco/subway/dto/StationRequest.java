package wooteco.subway.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class StationRequest {
    @NotBlank(message = "역 이름을 입력해야 합니다.")
    @Length(max = 255)
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
