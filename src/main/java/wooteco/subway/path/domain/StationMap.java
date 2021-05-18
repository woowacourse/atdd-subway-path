package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class StationMap {
    private final SearchPathAlgorithm<Station> searchPathAlgorithm;

    public StationMap(SearchPathAlgorithm<Station> searchPathAlgorithm, List<Line> lines) {
        this.searchPathAlgorithm = searchPathAlgorithm;
        add(lines);
    }

    private void add(List<Line> lines) {
        lines.stream()
                .map(Line::getSections)
                .forEach(this::add);
    }

    private void add(Sections sections) {
        sections.getSections().forEach(this::add);
    }

    private void add(Section section) {
        searchPathAlgorithm.add(section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    public ShortestPath findShortedPath(Station from, Station to) {
        return new ShortestPath(searchPathAlgorithm.getShortestPath(from, to),
                searchPathAlgorithm.getShortedDistance(from, to));
    }
}
