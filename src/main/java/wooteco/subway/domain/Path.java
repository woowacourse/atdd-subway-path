package wooteco.subway.domain;

import java.util.List;

public class Path {

    public static Path EMPTY = new Path(List.of(), List.of(), 0);

    private final List<Station> stations;
    private final List<Section> sections;
    private final int distance;

    public Path(List<Station> stations, List<Section> sections, int distance) {
        this.stations = stations;
        this.sections = sections;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Section> getSections() {
        return sections;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
