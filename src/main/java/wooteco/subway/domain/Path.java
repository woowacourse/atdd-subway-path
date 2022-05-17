package wooteco.subway.domain;

import java.util.List;

public class Path {

    private final List<Station> stations;
    private final int distance;

    public Path(final List<Station> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }
}
