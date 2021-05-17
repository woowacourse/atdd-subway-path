package wooteco.subway.web.dto;

import javax.validation.constraints.NotEmpty;
import wooteco.subway.domain.Station;

public class StationRequest {
    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Station toStation() {
        return new Station(name);
    }
}
