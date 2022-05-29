package wooteco.subway.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class StationRequest {

    @NotNull
    @Size(min = 2, max = 10, message = "역 이름은 2글자 이상 10글자 이하여야 합니다.")
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
