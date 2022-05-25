package wooteco.subway.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import wooteco.subway.domain.Station;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "역 이름은 공백일 수 없습니다.")
    private final String name;

    @JsonCreator
    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Station toEntity() {
        return new Station(this.name);
    }
}
