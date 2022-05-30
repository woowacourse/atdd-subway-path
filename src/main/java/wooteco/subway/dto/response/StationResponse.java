package wooteco.subway.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.subway.domain.station.Station;

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
