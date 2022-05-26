package wooteco.subway.controller.dto;

import javax.validation.constraints.NotNull;

public class StationRequest {

    @NotNull(message = "역 이름이 비었습니다.")
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
