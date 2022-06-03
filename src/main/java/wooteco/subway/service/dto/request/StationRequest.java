package wooteco.subway.service.dto.request;

import wooteco.subway.domain.Station;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "지하철역의 이름을 입력해주세요.")
    private String name;

    private StationRequest() {
    }

    public Station toStation() {
        return new Station(name);
    }

    public StationRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
