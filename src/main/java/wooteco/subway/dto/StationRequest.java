package wooteco.subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class StationRequest {
    @NotBlank(message = "{name.notBlank}")
    @Size(max = 10, message = "{name.tooLong}")
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
