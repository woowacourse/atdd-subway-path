package wooteco.subway.dto.request;

import javax.validation.constraints.NotBlank;

public class CreateStationRequest {

    @NotBlank(message = "이름은 무조건 입력해야 합니다.")
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
