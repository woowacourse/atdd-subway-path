package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.station.domain.Station;

public class Path {

    private final List<Station> paths;
    private final int distance;

    public Path(List<Station> paths, int distance) {
        this.paths = paths;
        this.distance = distance;
    }

    public List<Station> getPaths() {
        return paths;
    }

    public int getDistance() {
        return distance;
    }
}
