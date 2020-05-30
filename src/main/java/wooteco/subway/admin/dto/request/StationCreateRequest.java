package wooteco.subway.admin.dto.request;

import javax.validation.constraints.NotEmpty;

import wooteco.subway.admin.domain.Station;

public class StationCreateRequest {
    @NotEmpty(message = "역의 이름을 입력해주세요.")
    private String name;

    public String getName() {
        return name;
    }

    public Station toStation() {
        return Station.of(name);
    }
}
