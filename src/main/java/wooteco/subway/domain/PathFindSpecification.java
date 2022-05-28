package wooteco.subway.domain;

import java.util.List;
import wooteco.subway.util.NullChecker;

public class PathFindSpecification {

    private Station from;
    private Station to;
    private List<Station> stations;
    private List<Section> sections;

    public PathFindSpecification(Station from, Station to, List<Station> stations,
                                 List<Section> sections) {
        NullChecker.validateNonNull(from, to, stations, sections);
        this.from = from;
        this.to = to;
        this.stations = stations;
        this.sections = sections;
    }

    public Station getFrom() {
        return from;
    }

    public Station getTo() {
        return to;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Section> getSections() {
        return sections;
    }
}
