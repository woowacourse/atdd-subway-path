package wooteco.subway.admin.dto;


import javax.validation.constraints.NotBlank;

import wooteco.subway.admin.domain.Station;

public class StationCreateRequest {

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    public String getName() {
        return name;
    }

    public Station toStation() {
        return new Station(name);
    }
}
