package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {

    private final List<Station> pathStations;
    private final int weight;

    public Path(List<Station> pathStations, int weight) {
        this.pathStations = pathStations;
        this.weight = weight;
    }

    public List<Station> getPathStations() {
        return pathStations;
    }

    public int getWeight() {
        return weight;
    }
}
