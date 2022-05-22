package wooteco.subway.ui.dto;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "지하철역 이름이 필요합니다.")
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
