package wooteco.subway.ui.dto;

import static wooteco.subway.ui.dto.LineCreationRequest.OMISSION_MESSAGE;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "지하철 역 이름" + OMISSION_MESSAGE)
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
