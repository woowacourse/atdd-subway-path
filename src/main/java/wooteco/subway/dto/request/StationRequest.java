package wooteco.subway.dto.request;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "지하철역의 이름을 입력해주세요.")
    private String name;

    private StationRequest() {
    }

    public StationRequest(final String name) {
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
