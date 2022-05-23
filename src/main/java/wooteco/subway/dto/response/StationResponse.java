package wooteco.subway.dto.response;

import lombok.*;
import wooteco.subway.domain.Station;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StationResponse {

    private Long id;
    private String name;

    public StationResponse(Station station) {
        this.id = station.getId();
        this.name = station.getName();
    }
}
