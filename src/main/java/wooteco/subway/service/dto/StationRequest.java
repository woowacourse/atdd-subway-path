package wooteco.subway.service.dto;

import javax.validation.constraints.NotEmpty;

public class StationRequest {

    @NotEmpty
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
