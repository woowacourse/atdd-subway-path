package wooteco.subway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wooteco.subway.domain.Station;

import java.util.List;

@Getter
@AllArgsConstructor
public class PathResponse {

    private List<Station> stations;
    private int distance;
    private int fare;

    private PathResponse() {
    }
}
