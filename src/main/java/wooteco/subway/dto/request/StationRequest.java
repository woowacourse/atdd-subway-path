package wooteco.subway.dto.request;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "이름은 비워둘 수 없습니다.")
    private String name;

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "StationRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}
