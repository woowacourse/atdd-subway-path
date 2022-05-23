package wooteco.subway.dto.request;

import javax.validation.constraints.NotBlank;

public class CreateStationRequest {

    @NotBlank(message = "역이름은 공백일 수 없습니다.")
    private String name;

    private CreateStationRequest() {
    }

    public CreateStationRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
