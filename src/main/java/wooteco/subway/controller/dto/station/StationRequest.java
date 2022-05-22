package wooteco.subway.controller.dto.station;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank
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
